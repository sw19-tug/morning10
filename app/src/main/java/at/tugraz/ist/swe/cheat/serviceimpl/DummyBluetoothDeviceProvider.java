package at.tugraz.ist.swe.cheat.serviceimpl;

import at.tugraz.ist.swe.BluetoothDeviceState;
import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public class DummyBluetoothDeviceProvider implements BluetoothDeviceProvider {

    private boolean enabled;
    private BluetoothDeviceState state = BluetoothDeviceState.STOPSCAN;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void startDiscovery() {
        this.state = BluetoothDeviceState.SCAN;
    }

    @Override
    public boolean isDiscovering() {
        return this.state == BluetoothDeviceState.SCAN? true: false;
    }

    @Override
    public void cancelDiscovery() {
        this.state = BluetoothDeviceState.STOPSCAN;
    }

    @Override
    public BluetoothDeviceState getState() {
        return this.state;
    }
}
