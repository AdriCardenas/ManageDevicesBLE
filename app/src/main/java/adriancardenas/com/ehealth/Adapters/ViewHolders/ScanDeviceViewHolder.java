package adriancardenas.com.ehealth.Adapters.ViewHolders;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import adriancardenas.com.ehealth.MainActivity;
import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.ScanActivity;
import adriancardenas.com.ehealth.model.BluetoothLowEnergyDevice;
import adriancardenas.com.ehealth.model.ScanItemListener;

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

    public void bind(BluetoothLowEnergyDevice bluetoothDevice, ScanItemListener scanItemListener) {
        device = bluetoothDevice;
        deviceName.setText(device.getName());
        addressDevice.setText(device.getAddress());
        signalBar.setVisibility(View.VISIBLE);
        signalBar.setProgress(getIntensity(device.getRssi()));
        rootCell.setOnClickListener((View) -> {
            Log.v("test", "Connecting to " + device.getAddress());
            Log.v("test", "Device name " + bluetoothDevice.getName());

            scanItemListener.onClickScanItem();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
    }

    private int getIntensity(int rssi) {
        return 5000 / (rssi * -1);
    }
}