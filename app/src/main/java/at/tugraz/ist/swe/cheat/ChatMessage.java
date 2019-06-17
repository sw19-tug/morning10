package at.tugraz.ist.swe.cheat;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

import at.tugraz.ist.swe.cheat.dto.ProxyBitmap;

public class ChatMessage implements Serializable {

    public enum MessageType { DEFAULT, EDIT, DELETE };
    private static int id_counter = 0;
    private int message_id;
    private String address;
    private String message;
    private Date timeStamp;
    private ProxyBitmap image;
    private MessageType type = MessageType.DEFAULT;

    public ChatMessage(String address, String message, Date timeStamp) {
        this.message  = message;
        this.address =  address;
        this.message_id = id_counter++;
        this.timeStamp = timeStamp;
    }

    public ChatMessage(int id, String address, String message, Date timeStamp, MessageType type) {
        this.message  = message;
        this.address =  address;
        this.message_id = id;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public ChatMessage(String address, Bitmap image, Date timeStamp) {
        this.message  = "";
        this.address =  address;
        this.message_id = id_counter++;
        this.timeStamp = timeStamp;
        this.image = new ProxyBitmap(image);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getSenderAddress() {
        return this.address;
    }

    public int getId() {
        return this.message_id;
    }

    public Date getTimeStamp() { return this.timeStamp; }

    public void setTimeStamp(Date date) {
        this.timeStamp = date;
    }

    public Bitmap getImage() { return image.getBitmap(); }

    public void setImage(Bitmap image) { this.image = new ProxyBitmap(image); }

    public void setAddress(String address) {
        this.address = address;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public static void setIdCounter(int counter) {
        id_counter = counter;
    }
}
