package adriancardenas.com.ehealth.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.Utils.Utils;
import adriancardenas.com.ehealth.model.BluetoothLowEnergyDevice;
import butterknife.OnClick;

/**
 * Created by Adrian on 21/04/2018.
 */

public class ScanDeviceViewHolder extends RecyclerView.ViewHolder{
    ProgressBar signalBar;
    TextView deviceName;
    TextView addressDevice;
    View rootCell;

    public ScanDeviceViewHolder(View itemView) {
        super(itemView);
        deviceName = itemView.findViewById(R.id.name_device);
        addressDevice = itemView.findViewById(R.id.address_device);
        signalBar = itemView.findViewById(R.id.signal_progress_bar);
        rootCell = itemView.findViewById(R.id.root_cell);
    }

    public void bind(BluetoothLowEnergyDevice bluetoothDevice){
        deviceName.setText(bluetoothDevice.getName());
        addressDevice.setText(bluetoothDevice.getAddress());
        signalBar.setVisibility(View.VISIBLE);
        signalBar.setProgress(getIntensity(bluetoothDevice.getRssi()));
        rootCell.setOnClickListener((View)->{
            Utils.showSnackbar(itemView, "Has pulsado en un item");
        });
    }

    private int getIntensity(int rssi){
        return 5000/(rssi*-1);
    }
}