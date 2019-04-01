package at.tugraz.ist.swe.cheat;

import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

class DummyBluetoothDeviceProvider implements BluetoothDeviceProvider {

    private boolean enabled;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
