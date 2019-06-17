package at.tugraz.ist.swe.cheat;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;


import static at.tugraz.ist.swe.cheat.util.ConverterClassByte.convertBitmapToByteArray;
import static at.tugraz.ist.swe.cheat.util.ConverterClassByte.convertCompressedByteArrayToBitmap;

public class ChatMessage implements Serializable {

    int message_id;
    String address;
    String message;
    Date timeStamp;
    byte[] image;
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
        this.image = convertBitmapToByteArray(image);
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

    public Bitmap getImage() { return convertCompressedByteArrayToBitmap(image); }

    public void setImage(Bitmap image) {convertBitmapToByteArray(image);}

    public void setAddress(String address) {
        this.address = address;
    }
}
