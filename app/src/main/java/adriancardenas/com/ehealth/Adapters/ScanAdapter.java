package adriancardenas.com.ehealth.Adapters;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import adriancardenas.com.ehealth.Adapters.ViewHolders.ScanDeviceViewHolder;
import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.model.BluetoothLowEnergyDevice;
import adriancardenas.com.ehealth.model.ScanItemListener;

/**
 * Created by Adrian on 21/04/2018.
 */

public class ScanAdapter extends RecyclerView.Adapter<ScanDeviceViewHolder> {
    private List<BluetoothLowEnergyDevice> deviceList;
    private int lastPosition;
    private Context context;
    private ScanItemListener scanItemListener;

    public ScanAdapter(List<BluetoothLowEnergyDevice> list, Context context, ScanItemListener scanItemListener) {
        deviceList = list;
        lastPosition = -1;
        this.context = context;
        this.scanItemListener = scanItemListener;
    }

    public void resetAdapter() {
        lastPosition = -1;
    }

    @NonNull
    @Override
    public ScanDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan, parent, false);
        ScanDeviceViewHolder vh = new ScanDeviceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ScanDeviceViewHolder holder, int position) {
        holder.bind(deviceList.get(position),scanItemListener);
        setAnimation(holder.itemView, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
