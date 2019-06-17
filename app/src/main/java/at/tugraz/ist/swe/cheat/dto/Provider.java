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
    public static final int STATE_CHAT = 5;


    public Provider() {
    }


}