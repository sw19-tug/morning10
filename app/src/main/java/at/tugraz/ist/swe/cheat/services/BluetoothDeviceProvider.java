package at.tugraz.ist.swe.cheat.services;

import at.tugraz.ist.swe.BluetoothDeviceState;

public interface BluetoothDeviceProvider {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void startDiscovery();

    boolean isDiscovering();

    void cancelDiscovery();

    BluetoothDeviceState getState();
}
