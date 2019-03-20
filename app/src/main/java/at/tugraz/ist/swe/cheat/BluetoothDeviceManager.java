package at.tugraz.ist.swe.cheat;

import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public class BluetoothDeviceManager {
    private BluetoothDeviceProvider bluetoothDeviceProvider;
    private boolean active;

    public BluetoothDeviceManager(BluetoothDeviceProvider bluetoothDeviceProvider)
    {
        this.active = false;
        this.bluetoothDeviceProvider = bluetoothDeviceProvider;
    }
    public boolean isOn()
    {
        this.active = bluetoothDeviceProvider.isEnabled();
        return this.active;
    }

}
