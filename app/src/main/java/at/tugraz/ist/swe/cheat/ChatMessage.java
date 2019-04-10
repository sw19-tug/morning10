package at.tugraz.ist.swe.cheat;

import java.util.Date;

public class ChatMessage {

    int message_id;
    String address;
    String message;
    Date timeStamp;
    // modified

    public ChatMessage(int message_id, String address, String message, Date timeStamp) {
        this.message  = message;
        this.address =  address;
        this.message_id =  message_id;
        this.timeStamp = timeStamp;
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
}
