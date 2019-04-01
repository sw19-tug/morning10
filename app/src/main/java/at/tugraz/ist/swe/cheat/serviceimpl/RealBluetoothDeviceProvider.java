package at.tugraz.ist.swe.cheat.serviceimpl;

import android.bluetooth.BluetoothAdapter;

import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public class RealBluetoothDeviceProvider implements BluetoothDeviceProvider {


    @Override
    public boolean isEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
    }
}
