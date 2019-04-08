package at.tugraz.ist.swe.cheat.DTO;

import java.util.Objects;

public class Device {
    private String device_name;
    private String device_address;

    public Device(String device_name, String device_address) {
        this.device_name = device_name;
        this.device_address = device_address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(device_name, device.device_name) &&
                Objects.equals(device_address, device.device_address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device_name, device_address);
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }
}
