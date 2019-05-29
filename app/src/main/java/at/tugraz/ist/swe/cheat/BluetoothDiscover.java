package at.tugraz.ist.swe.cheat;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import at.tugraz.ist.swe.cheat.btobservable.DeviceObservable;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;

public class BluetoothDiscover extends BroadcastReceiver {

    DeviceObservable deviceObservable;

    public BluetoothDiscover(DeviceObservable deviceObservable)
    {
        this.deviceObservable = deviceObservable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                CustomMessage customMessage = new CustomMessage(5,new Device(device.getName(),device.getAddress()));
                deviceObservable.setDevice(customMessage);
            }
        }
    }
}

