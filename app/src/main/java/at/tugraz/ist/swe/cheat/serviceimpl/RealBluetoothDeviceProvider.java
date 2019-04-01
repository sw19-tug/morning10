package at.tugraz.ist.swe.cheat.serviceimpl;

import android.bluetooth.BluetoothAdapter;

import at.tugraz.ist.swe.BluetoothDeviceState;
import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public class RealBluetoothDeviceProvider implements BluetoothDeviceProvider {

    BluetoothDeviceState state = BluetoothDeviceState.STOPSCAN;

    @Override
    public boolean isEnabled() {
        return  BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public void startDiscovery() {
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
        this.state = BluetoothDeviceState.SCAN;
    }

    @Override
    public boolean isDiscovering() {
        return BluetoothAdapter.getDefaultAdapter().isDiscovering();
    }

    @Override
    public void cancelDiscovery() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        this.state = BluetoothDeviceState.STOPSCAN;
    }

    @Override
    public BluetoothDeviceState getState() {
        return this.state;
    }
}
