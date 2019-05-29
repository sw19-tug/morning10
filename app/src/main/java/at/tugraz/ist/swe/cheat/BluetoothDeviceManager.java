package at.tugraz.ist.swe.cheat;

import at.tugraz.ist.swe.cheat.dto.Provider;
import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public class BluetoothDeviceManager {


    public static enum BtState {
        ON,
        OFF,
        SCAN
    }
    private Provider bluetoothDeviceProvider;

    private boolean active;

    private BtState btState;

    public BluetoothDeviceManager(Provider bluetoothDeviceProvider)
    {
        this.active = false;
        this.bluetoothDeviceProvider = bluetoothDeviceProvider;

        this.isOn();
    }
    public boolean isOn()
    {
        setActive(bluetoothDeviceProvider != null && bluetoothDeviceProvider.isEnabled());
        return this.active;
    }

    private synchronized void setActive(boolean state)
    {
        this.active = state;
        if(state)
        {
            btState = BtState.ON;
        }
        else
        {
            btState = BtState.OFF;
        }

    }


    public Provider getBluetoothDeviceProvider() {
        return bluetoothDeviceProvider;
    }

    public BtState getBtState() {
        return btState;
    }

    public void startScanning()
    {
        bluetoothDeviceProvider.startDiscovery();
        btState = BtState.SCAN;
    }

    public void stopScanning()
    {
        if (bluetoothDeviceProvider.isDiscovering()) {
            bluetoothDeviceProvider.cancelDiscovery();
        }
        btState = BtState.ON;
    }

    public void connectToDevice(String address) {
        bluetoothDeviceProvider.connectToDevice(address);
    }
}

