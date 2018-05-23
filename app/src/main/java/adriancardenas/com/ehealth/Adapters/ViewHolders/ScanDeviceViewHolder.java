package adriancardenas.com.ehealth.Adapters.ViewHolders;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import adriancardenas.com.ehealth.MainActivity;
import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.GattManager;
import adriancardenas.com.ehealth.Utils.Utils;
import adriancardenas.com.ehealth.model.BluetoothLowEnergyDevice;

import static adriancardenas.com.ehealth.Utils.GattManager.bluetoothGatt;
import static adriancardenas.com.ehealth.Utils.GattManager.device;

/**
 * Created by Adrian on 21/04/2018.
 */

public class ScanDeviceViewHolder extends RecyclerView.ViewHolder {
    private ProgressBar signalBar;
    private TextView deviceName;
    private TextView addressDevice;
    private View rootCell;
    private Context context;

    public ScanDeviceViewHolder(View itemView) {
        super(itemView);
        deviceName = itemView.findViewById(R.id.name_device);
        addressDevice = itemView.findViewById(R.id.address_device);
        signalBar = itemView.findViewById(R.id.signal_progress_bar);
        rootCell = itemView.findViewById(R.id.root_cell);
        context = itemView.getContext();
    }

    public void bind(BluetoothLowEnergyDevice bluetoothDevice, BluetoothAdapter bluetoothAdapter) {
        device = bluetoothDevice;
        deviceName.setText(device.getName());
        addressDevice.setText(device.getAddress());
        signalBar.setVisibility(View.VISIBLE);
        signalBar.setProgress(getIntensity(device.getRssi()));
        rootCell.setOnClickListener((View) -> {
            Log.v("test", "Connecting to " + device.getAddress());
            Log.v("test", "Device name " + bluetoothDevice.getName());

            bluetoothGatt = device.getBluetoothDevice().connectGatt(context, true, bluetoothGattCallback);
        });
    }

    final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.v("test", "onConnectionStateChange");

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                stateConnected();
                Utils.showSnackbar(itemView, "Connected");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                stateDisconnected();
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.v("test", "onServicesDiscovered");
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.v("test", "onCharacteristicRead");
            byte[] data = characteristic.getValue();
            GattManager.battery = data;
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

    void stateConnected() {
        bluetoothGatt.discoverServices();
    }

    void stateDisconnected() {
        bluetoothGatt.disconnect();
    }

    private int getIntensity(int rssi) {
        return 5000 / (rssi * -1);
    }
}