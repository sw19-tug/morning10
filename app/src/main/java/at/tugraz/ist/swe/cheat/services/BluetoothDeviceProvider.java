package at.tugraz.ist.swe.cheat.services;

public interface BluetoothDeviceProvider {

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
