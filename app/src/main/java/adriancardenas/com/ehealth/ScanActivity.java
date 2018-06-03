package adriancardenas.com.ehealth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adriancardenas.com.ehealth.Adapters.ScanAdapter;
import adriancardenas.com.ehealth.Utils.Constants;
import adriancardenas.com.ehealth.Utils.Utils;
import adriancardenas.com.ehealth.model.BluetoothLowEnergyDevice;
import adriancardenas.com.ehealth.model.ScanItemListener;
import butterknife.BindView;
import butterknife.ButterKnife;

import static adriancardenas.com.ehealth.Utils.Constants.REQUEST_BLUETOOTH_ENABLE_CODE;

public class ScanActivity extends AppCompatActivity implements ScanItemListener {

    //    @BindView(R.id.placeholder)
//    Placeholder placeholder;
    @BindView(R.id.empty_scan_tv)
    TextView emptyTvScan;
    @BindView(R.id.scan_button)
    Button scanButton;
    @BindView(R.id.progress_bar_scan)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.constraint_layout_scan)
    ConstraintLayout constraintLayout;

    ScanAdapter scanAdapter;
    List<BluetoothLowEnergyDevice> deviceList;
    List<String> deviceAddressList;
    private Boolean isScanning = false;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean isTurningOnBluetooth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ButterKnife.bind(this);

        deviceList = new ArrayList<>();
        deviceAddressList = new ArrayList<>();

        BluetoothManager mBluetoothManager = (BluetoothManager) this.getSystemService(this.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        Utils.checkBluetoothPermission(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scanAdapter = new ScanAdapter(deviceList, this, this);
        recyclerView.setAdapter(scanAdapter);

        scanButton.setOnClickListener((View v) -> {
            //swapItem(v);
            if (isScanning) {
                isScanning = false;
                progressBar.setVisibility(View.GONE);
                scanButton.setText(getResources().getString(R.string.start_scan));
                scanAdapter.resetAdapter();
                stopScanning();
            } else {
                checkBluetoothIsOn();
                while (isTurningOnBluetooth) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (mBluetoothAdapter != null) {
                    emptyTvScan.setVisibility(View.VISIBLE);
                    isScanning = true;
                    progressBar.setVisibility(View.VISIBLE);
                    scanButton.setText(getResources().getString(R.string.stop_scan));
                    deviceList.clear();
                    deviceAddressList.clear();
                    scanAdapter.notifyDataSetChanged();
                    startScanning(isScanning);
                } else {
                    Utils.showSnackbar(constraintLayout, getResources().getString(R.string.request_activate_bluetooth));
                }
            }
        });
    }

//    public void swapItem(View v){
//        TransitionManager.beginDelayedTransition(constraintLayout);
//        placeholder.setContentId(v.getId());
//    }

    @Override
    protected void onResume() {
        super.onResume();

        checkBluetoothIsOn();
    }

    private void checkBluetoothIsOn() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            isTurningOnBluetooth = true;
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_ENABLE_CODE);
        }
    }

    private void stopScanning() {
        stopLeScanner();
    }

    private void stopLeScanner() {
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.stopScan(scanCallback);
    }

    private void startScanning(final boolean enable) {
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        Handler mHandler = new Handler();
        if (enable) {
            List<ScanFilter> scanFilters = new ArrayList<>();
            final ScanSettings settings = new ScanSettings.Builder().build();
//            ScanFilter scanFilter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(SampleGattAttributes.UUID_BATTERY_SERVICE)).build();
//            scanFilters.add(scanFilter);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    progressBar.setVisibility(View.GONE);
                    scanButton.setText(getResources().getString(R.string.start_scan));
                    scanAdapter.resetAdapter();
                    bluetoothLeScanner.stopScan(scanCallback);
                }
            }, Constants.SCAN_PERIOD);
            isScanning = true;
            bluetoothLeScanner.startScan(scanFilters, settings, scanCallback);
        } else {
            isScanning = false;
            isScanning = false;
            progressBar.setVisibility(View.GONE);
            scanButton.setText(getResources().getString(R.string.start_scan));
            scanAdapter.resetAdapter();
            bluetoothLeScanner.stopScan(scanCallback);
        }
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothLowEnergyDevice bluetoothDevice = new BluetoothLowEnergyDevice(result.getDevice(), result.getRssi());
            if (bluetoothDevice.getName() != null && !bluetoothDevice.getName().equals("")
                    && !deviceAddressList.contains(bluetoothDevice.getAddress())) {
                emptyTvScan.setVisibility(View.GONE);
                deviceList.add(bluetoothDevice);
                deviceAddressList.add(bluetoothDevice.getAddress());
                scanAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("ERROR:", "Scanning Failed " + errorCode);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BLUETOOTH_ENABLE_CODE) {
            if (resultCode == RESULT_OK) {
                BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = bluetoothManager.getAdapter();
            } else if (resultCode == RESULT_CANCELED) {
                Utils.showSnackbar(constraintLayout, getResources().getString(R.string.request_activate_bluetooth));
            }
        }
        isTurningOnBluetooth = false;
    }

    @Override
    public void onClickScanItem() {
        stopLeScanner();
    }
}
