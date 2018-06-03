package adriancardenas.com.ehealth;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static adriancardenas.com.ehealth.Utils.Constants.WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT;
import static adriancardenas.com.ehealth.Utils.GattManager.bluetoothGatt;
import static adriancardenas.com.ehealth.Utils.GattManager.device;
import static adriancardenas.com.ehealth.Utils.Utils.getPhotoCode;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.add_photo_iv)
    ImageView addPhotoIv;
    @BindView(R.id.constraint_layout_cell)
    ConstraintLayout constraintLayout;
    @BindView(R.id.personal_imc)
    TextView personalImc;
    @BindView(R.id.personal_weight)
    TextView personalWeight;
    @BindView(R.id.battery_lvl_tv)
    TextView batteryLvlTv;
    @BindView(R.id.battery_lvl_iv)
    ImageView batteryLvlIv;
    @BindView(R.id.steps_today_tv)
    TextView stepsTodayTv;
    @BindView(R.id.calorie_today_tv)
    TextView caloriesTodayTv;
    @BindView(R.id.distance_today_tv)
    TextView distanceTodayTv;
    @BindView(R.id.circular_progress)
    ProgressBar circularPb;
    @BindView(R.id.distance_today_iv)
    ImageView distanceTodayIv;
    @BindView(R.id.steps_today_iv)
    ImageView stepsTodayIv;


    private Uri uriPhoto;
    private File photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bluetoothGatt = device.getBluetoothDevice().connectGatt(this, true, bluetoothGattCallback);

        initProfilePhoto();

        updatePersonalData();
    }

    private void updatePersonalData() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        float weight = Float.parseFloat(sharedPref.getString(Constants.WEIGHT, ""));
        float height = Float.parseFloat(sharedPref.getString(Constants.HEIGHT, ""));

        personalWeight.setText(String.format("%.1f kg", weight));

        float imc = weight / (height / 100 * height / 100);

        personalImc.setText(String.format("IMC: %.1f", imc));
    }

    private void initProfilePhoto() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        String url = sharedPref.getString(Constants.PHOTO, "");

        if (!url.equals("")) {
            Glide.with(getApplicationContext()).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(profileImage);
        }

        addPhotoIv.setOnClickListener((View) -> {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);
                initCameraPhoto();
            } else {
                initCameraPhoto();
            }
        });
    }

    private void initCameraPhoto() {
        String photoPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + getPhotoCode() + ".jpg";
        photo = new File(photoPath);

        try {
            photo.createNewFile();
            uriPhoto = FileProvider.getUriForFile(this, "adriancardenas.com.ehealth", photo);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
            startActivityForResult(cameraIntent, Constants.REQUEST_TAKE_PHOTO_RESULT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("*ERROR*", "failed to create a file photo");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_TAKE_PHOTO_RESULT) {
            if (resultCode == RESULT_CANCELED) {
                Utils.showSnackbar(constraintLayout, getResources().getString(R.string.error_take_photo));
            } else {
                if (uriPhoto != null && uriPhoto.getPath().contains("jpg")) {
                    SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.PHOTO, uriPhoto.toString());
                    editor.apply();

                    Glide.with(getApplicationContext()).load(uriPhoto).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(profileImage);
                    uriPhoto = null;
                } else {
                    Utils.showSnackbar(constraintLayout, getResources().getString(R.string.error_take_photo));
                }
            }
        }
    }


    private void updateBatteryLevelIv(int batteryLevel) {
        if (batteryLevel < 10) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_10));
        } else if (batteryLevel < 20) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_20));
        } else if (batteryLevel < 30) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_30));
        } else if (batteryLevel < 50) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_50));
        } else if (batteryLevel < 60) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_60));
        } else if (batteryLevel < 80) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_80));
        } else if (batteryLevel < 90) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_80));
        } else if (batteryLevel < 100) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_90));
        } else if (batteryLevel == 100) {
            batteryLvlIv.setImageDrawable(getDrawable(R.drawable.ic_battery_100));
        }
    }

    void stateConnected() {
        bluetoothGatt.discoverServices();
    }

    void stateDisconnected() {
        bluetoothGatt.disconnect();
    }

    final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.v("test", "onConnectionStateChange");

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                stateConnected();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                stateDisconnected();
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.v("test", "onServicesDiscovered");
            getBatteryStatus();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.v("test", "onCharacteristicRead");
            byte[] data = characteristic.getValue();
            UUID uuid = characteristic.getUuid();
            if (data != null && uuid.equals(Constants.Basic.BATTERY_CHARASTERISTIC)) {
                runOnUiThread(() -> {
                    batteryLvlIv.setVisibility(View.VISIBLE);
                    batteryLvlTv.setVisibility(View.VISIBLE);
                    String str = String.valueOf(data[1]);
                    batteryLvlTv.setText(str + "%");
                    updateBatteryLevelIv(data[1]);
                    getRealTimeSteps();
                });
            } else if (data != null && characteristic.getUuid().equals(Constants.Basic.REALTIME_STEPS_CHARACTERISTIC)) {
                runOnUiThread(() -> updateDailyInfo(data));
            } else if (characteristic.getUuid().equals(Constants.Basic.ACTIVITY_DATA_CHARASTERISTIC)) {

            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.v("test", "onCharacteristicWrite");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.v("test", "onCharacteristicChanged");
            byte[] data = characteristic.getValue();
            if (characteristic.getUuid().equals(Constants.Basic.BATTERY_CHARASTERISTIC)) {
                runOnUiThread(() -> {
                    batteryLvlIv.setVisibility(View.VISIBLE);
                    batteryLvlTv.setVisibility(View.VISIBLE);
                    String str = String.valueOf(data[1]);
                    batteryLvlTv.setText(str + "%");
                    updateBatteryLevelIv(data[1]);
                    getRealTimeSteps();
                });
            } else if (characteristic.getUuid().equals(Constants.Basic.REALTIME_STEPS_CHARACTERISTIC)) {
                runOnUiThread(() -> updateDailyInfo(data));
            } else if (characteristic.getUuid().equals(Constants.Basic.ACTIVITY_DATA_CHARASTERISTIC)) {

            }
            //TODO INCLUDE MORE CHARACTERISTIC
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.v("test", "onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.v("test", "onDescriptorWrite");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.v("test", "onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.v("test", "onReadRemoteRssi");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.v("test", "onMtuChanged");
        }
    };

    private void updateDailyInfo(byte[] data) {
        String calories = getDataValue(data, 9, 11);
        String distance = getDataValue(data, 5, 8);
        String steps = getDataValue(data, 1, 4);
        SharedPreferences sharedPref = getSharedPreferences(Constants.LOCAL_APPLICATION_PATH, Context.MODE_PRIVATE);
        String goal = sharedPref.getString(Constants.STEPS_GOAL, "");
        if (!goal.equals("")) {
            circularPb.setMax(Integer.parseInt(goal));
            circularPb.setProgress(Integer.parseInt(steps));
            circularPb.setVisibility(View.VISIBLE);
        } else {
            circularPb.setVisibility(View.INVISIBLE);
        }

        stepsTodayTv.setText(steps);
        stepsTodayIv.setVisibility(View.VISIBLE);
        distanceTodayTv.setText(getFormatDistance(distance));
        distanceTodayIv.setVisibility(View.VISIBLE);
        caloriesTodayTv.setText(calories + "cal");
    }

    private String getFormatDistance(String distance) {
        String result = distance;
        float f = Float.parseFloat(distance);
        if (f > 1000) {
            f /= 1000;
            return String.format("%s km", String.format("%.2f", f));
        }
        return distance;
    }

    private String getDataValue(byte[] data, int initialPos, int finalPos) {
        int value = 0;
        if (data != null) {
            if (data[initialPos] < 0) {
                String thisByte = String.format("%x", data[initialPos]);
                value = Integer.parseInt(thisByte, 16);
            } else {
                value = Math.abs(data[initialPos]);
            }

        }

        if (data[initialPos + 1] != 0) {
            value += Math.abs(data[initialPos + 1]) * 256;
        }
        if (data[initialPos + 2] != 0) {
            value += Math.abs(data[initialPos + 1]) * 256 * 256;
        }

        return String.valueOf(value);
    }

    void getBatteryStatus() {
        BluetoothGattCharacteristic bchar = bluetoothGatt.getService(Constants.Basic.service)
                .getCharacteristic(Constants.Basic.BATTERY_CHARASTERISTIC);

        if (!bluetoothGatt.readCharacteristic(bchar)) {
            Utils.showSnackbar(constraintLayout, "Failed get info");
        }
    }

    void getRealTimeSteps() {
        BluetoothGattCharacteristic bchar = bluetoothGatt.getService(Constants.Basic.service)
                .getCharacteristic(Constants.Basic.REALTIME_STEPS_CHARACTERISTIC);
        if (!bluetoothGatt.readCharacteristic(bchar)) {
            Utils.showSnackbar(constraintLayout, "Failed get real time steps");
        }
    }

    void getActivityData() {
        BluetoothGattCharacteristic bchar = bluetoothGatt.getService(Constants.Basic.service)
                .getCharacteristic(Constants.Basic.ACTIVITY_DATA_CHARASTERISTIC);
        if (!bluetoothGatt.readCharacteristic(bchar)) {
            Utils.showSnackbar(constraintLayout, "Failed get activity data");
        }
    }
}
