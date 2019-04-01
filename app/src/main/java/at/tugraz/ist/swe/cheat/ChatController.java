package at.tugraz.ist.swe.cheat;

public class ChatController {


    public enum ConnectionState {
        LISTEN,
    }

    private ConnectionState connectionState;


    public ChatController() {
    }

    public synchronized void start() {
        setConnectionState(ConnectionState.LISTEN);
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }
}
