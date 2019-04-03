package at.tugraz.ist.swe.cheat;

public class ChatController {


    public enum ConnectionState {
        LISTEN,
        CONNECTING,
        CONNECTED,
        NONE
    }

    private ConnectionState connectionState;


    public ChatController() {
        setConnectionState(ConnectionState.NONE);
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

    public void connect() {
        setConnectionState(ConnectionState.CONNECTING);
    }

    public void connected() {
        setConnectionState(ConnectionState.CONNECTED);
    }

    public void stop() {
        setConnectionState(ConnectionState.NONE);
    }

    public void connectionLost() {
        setConnectionState(ConnectionState.LISTEN);
    }

    public void connectionFailed() {
        setConnectionState(ConnectionState.LISTEN);
    }



}
