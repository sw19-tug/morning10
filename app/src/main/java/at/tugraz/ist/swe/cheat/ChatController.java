package at.tugraz.ist.swe.cheat;

import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Provider;

import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTING;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTIONLOST;
import static at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider.STATE_LISTEN;

public class ChatController implements Observer {


    public enum ConnectionState {
        LISTEN,
        CONNECTING,
        CONNECTED,
        NONE
    }

    private ConnectionState connectionState;

    private Provider bluetoothDeviceProvider;


    @Override
    public void update(Observable o, Object arg) {
        CustomMessage customMessage = (CustomMessage) arg;
        switch (customMessage.getState())
        {
            case STATE_LISTEN:
            {
                System.out.println("STATE LISTEN");
                setConnectionState(ConnectionState.LISTEN);
                break;
            }
            case STATE_CONNECTING:
            {
                System.out.println("STATE CONNECTING");
                setConnectionState(ConnectionState.CONNECTING);
                break;
            }
            case STATE_CONNECTED:
            {
                System.out.println("STATE CONNECTED");
                System.out.println(customMessage.getDevice().getDevice_name() + " / " +
                        customMessage.getDevice().getDevice_address());
                setConnectionState(ConnectionState.CONNECTED);
                break;
            }
            case STATE_CONNECTIONLOST:
            {
                System.out.println("STATE CONNECTION LOST");
                setConnectionState(ConnectionState.LISTEN);
                break;
            }
            default:
            {
                System.out.println("NO State");
                setConnectionState(ConnectionState.NONE);
            }
        }
    }


    public ChatController(Provider bluetoothDeviceProvider) {
        setConnectionState(ConnectionState.NONE);
        this.bluetoothDeviceProvider = bluetoothDeviceProvider;
        bluetoothDeviceProvider.addObserver(this);
        bluetoothDeviceProvider.start();
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
