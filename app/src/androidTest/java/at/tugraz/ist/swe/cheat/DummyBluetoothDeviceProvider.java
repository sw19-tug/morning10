package at.tugraz.ist.swe.cheat;

import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

class DummyBluetoothDeviceProvider implements BluetoothDeviceProvider {
    @Override
    public boolean isEnabled() {
        return true;
    }
}
