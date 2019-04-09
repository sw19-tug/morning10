package at.tugraz.ist.swe.cheat;

public class ChatMessage {

    int message_id;
    String address;
    String message;

    public ChatMessage(int message_id, String address, String message) {
        this.message  = message;
        this.address =  address;
        this.message_id =  message_id;
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
}
