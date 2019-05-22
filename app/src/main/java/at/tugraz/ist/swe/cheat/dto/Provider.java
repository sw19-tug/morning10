package at.tugraz.ist.swe.cheat.dto;

import java.util.Observable;

import at.tugraz.ist.swe.BluetoothDeviceState;
import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public abstract class Provider extends Observable implements BluetoothDeviceProvider {

    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTIONLOST = 4;


    public Provider() {
    }

    @Override
    public void setCurrentState(int state) {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void startDiscovery() {

    }

    @Override
    public boolean isDiscovering() {
        return false;
    }

    @Override
    public void cancelDiscovery() {

    }

    @Override
    public BluetoothDeviceState getState() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void connectToDevice(String deviceAddress) {

    }

    @Override
    public void connect() {

    }

    @Override
    public void connected() {

    }

    @Override
    public void connectionFailed() {

    }
}