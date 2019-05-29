package at.tugraz.ist.swe.cheat.services;

import java.io.IOException;

import at.tugraz.ist.swe.BluetoothDeviceState;

public interface BluetoothDeviceProvider {


    void setCurrentState(int state);

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void startDiscovery();

    boolean isDiscovering();

    void cancelDiscovery();

    BluetoothDeviceState getState();

    // start service
    void start();

    void connectToDevice(String deviceAddress);

    // initiate connection to remote device
    void connect();

    // manage Bluetooth connection
    void connected();

    void connectionFailed();

    int getCurrentState();

    void disconnected() throws IOException;
}
