package at.tugraz.ist.swe.cheat.dto;

public class CustomMessage {
    private int state;
    private Device device;

    public CustomMessage(int state, Device device) {
        this.state = state;
        this.device = device;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
