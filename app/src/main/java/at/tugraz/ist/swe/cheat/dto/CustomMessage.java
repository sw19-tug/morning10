package at.tugraz.ist.swe.cheat.dto;

import java.io.Serializable;

import at.tugraz.ist.swe.cheat.ChatMessage;

public class CustomMessage implements Serializable {
    private int state;
    private Device device;

    private ChatMessage message;

    public CustomMessage(int state, Device device) {
        this.state = state;
        this.device = device;
    }


    public CustomMessage(int state, Device device, ChatMessage message) {
        this.state = state;
        this.device = device;
        this.message = message;
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

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }
}
