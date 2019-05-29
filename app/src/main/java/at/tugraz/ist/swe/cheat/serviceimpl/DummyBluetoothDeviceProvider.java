package at.tugraz.ist.swe.cheat.serviceimpl;

import at.tugraz.ist.swe.BluetoothDeviceState;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;
import at.tugraz.ist.swe.cheat.dto.Provider;
import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public class DummyBluetoothDeviceProvider extends Provider implements BluetoothDeviceProvider {

    class DeviceDummy {
        private String name = "Dummy Device";
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    private boolean enabled;
    private BluetoothDeviceState state = BluetoothDeviceState.STOPSCAN;
    private int currentState;


    private DeviceDummy device;

    public DummyBluetoothDeviceProvider() {
        super();
    }

    @Override
    public synchronized void setCurrentState(int state) {
        this.currentState = state;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void startDiscovery() {
        this.state = BluetoothDeviceState.SCAN;
    }

    @Override
    public boolean isDiscovering() {
        return this.state == BluetoothDeviceState.SCAN ? true : false;
    }

    @Override
    public void cancelDiscovery() {
        this.state = BluetoothDeviceState.STOPSCAN;
    }

    @Override
    public BluetoothDeviceState getState() {
        return this.state;
    }

    @Override
    public void start() {

        CustomMessage message = new CustomMessage(STATE_LISTEN, null);

        setChanged();
        notifyObservers(message);
        setCurrentState(STATE_LISTEN);
    }

    @Override
    public void connectToDevice(String deviceAddress) {
        device = new DeviceDummy();
        device.setAddress(deviceAddress);
        connect();
    }

    @Override
    public void connect() {

        CustomMessage message = new CustomMessage(STATE_CONNECTING, new Device(device.getName(), device.getAddress()));

        setChanged();
        notifyObservers(message);

        setCurrentState(STATE_CONNECTING);

    }

    @Override
    public void connected() {

        CustomMessage message = new CustomMessage(STATE_CONNECTED, new Device(device.getName(), device.getAddress()));

        setChanged();
        notifyObservers(message);

        setCurrentState(STATE_CONNECTED);

    }

    @Override
    public void connectionFailed() {

        CustomMessage message = new CustomMessage(STATE_CONNECTIONLOST, null);

        setChanged();
        notifyObservers(message);

        // Start the service over to restart listening mode
        this.start();
    }

    @Override
    public int getCurrentState() {
        return currentState;
    }

    @Override
    public void disconnected() {
        CustomMessage message = new CustomMessage(STATE_LISTEN, new Device(device.getName(), device.getAddress()));

        setChanged();
        notifyObservers(message);

        setCurrentState(STATE_LISTEN);

    }
}