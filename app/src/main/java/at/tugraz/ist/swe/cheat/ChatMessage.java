package at.tugraz.ist.swe.cheat;

import android.graphics.Bitmap;
import java.util.Date;

public class ChatMessage {

    int message_id;
    String address;
    String message;
    Date timeStamp;
    Bitmap image;
    // modified

    public ChatMessage(int message_id, String address, String message, Date timeStamp) {
        this.message  = message;
        this.address =  address;
        this.message_id =  message_id;
        this.timeStamp = timeStamp;
    }

    public ChatMessage(int message_id, String address, Bitmap image, Date timeStamp) {
        this.message  = "";
        this.address =  address;
        this.message_id =  message_id;
        this.timeStamp = timeStamp;
        this.image = image;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSenderAddress() {
        return this.address;
    }

    public int getId() {
        return this.message_id;
    }

    public Date getTimeStamp() { return this.timeStamp; }

    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) { this.image = image; }

}
