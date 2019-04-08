package at.tugraz.ist.swe.cheat.observable;

import android.bluetooth.BluetoothClass;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import at.tugraz.ist.swe.cheat.DTO.Device;

public class OBluetoothClient extends Observable {

    private Set<Device> discoveredDevices;

    public OBluetoothClient() {
        this.discoveredDevices = new HashSet<>();
    }

    public void addDevice(Device device)
    {
        if(!discoveredDevices.contains(device))
        {
            discoveredDevices.add(device);
            setChanged();
            notifyObservers(device);
        }
    }
}
