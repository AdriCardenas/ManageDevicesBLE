package adriancardenas.com.ehealth.model;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Adrian on 21/04/2018.
 */

public class BluetoothLowEnergyDevice {
    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BluetoothLowEnergyDevice(BluetoothDevice bluetoothDevice, int rssi){
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
    }

    private void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getName(){
        return bluetoothDevice.getName();
    }

    public String getAddress(){
        return bluetoothDevice.getAddress();
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
}
