package at.tugraz.ist.swe.cheat;

import android.bluetooth.BluetoothDevice;


public class ChatMessage {

    BluetoothDevice sender;
    String message;

    public ChatMessage(BluetoothDevice sender, String message) {
        this.message  = message;
        this.sender =  sender;
    }

    public String getMessage() {
        return this.message;
    }

    public BluetoothDevice getSender()  {
        return this.sender;
    }
}
