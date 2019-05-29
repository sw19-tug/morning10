package at.tugraz.ist.swe.cheat.serviceimpl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

import at.tugraz.ist.swe.BluetoothDeviceState;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;
import at.tugraz.ist.swe.cheat.dto.Provider;
import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

public class RealBluetoothDeviceProvider extends Provider implements BluetoothDeviceProvider{


    private static final String APP_NAME = "Cheating";
    private static final UUID APP_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");


    BluetoothDeviceState state = BluetoothDeviceState.STOPSCAN;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private int currentState;

    BluetoothAdapter bluetoothAdapter;

    BluetoothDevice device;
    BluetoothServerSocket serverSocket;
    BluetoothSocket socket;


    public RealBluetoothDeviceProvider() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public synchronized void setCurrentState(int state)
    {
        this.currentState = state;
    }


    @Override
    public boolean isEnabled() {
        return  BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public void startDiscovery() {
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
        this.state = BluetoothDeviceState.SCAN;
    }

    @Override
    public boolean isDiscovering() {
        return BluetoothAdapter.getDefaultAdapter().isDiscovering();
    }

    @Override
    public void cancelDiscovery() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        this.state = BluetoothDeviceState.STOPSCAN;
    }

    @Override
    public BluetoothDeviceState getState() {
        return this.state;
    }


    // start service
    @Override
    public synchronized void start() {

        // Cancel any thread
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }


        setCurrentState(STATE_LISTEN);
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }


        CustomMessage message = new CustomMessage(STATE_LISTEN, null);

        setChanged();
        notifyObservers(message);

    }


    @Override
    public void connectToDevice(String deviceAddress) {
        bluetoothAdapter.cancelDiscovery();
        device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        this.connect();
    }


    // initiate connection to remote device
    @Override
    public synchronized void connect() {
        System.out.println("Connect to device "+ device.getAddress());

        // Cancel any thread
        if (currentState == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }


        // Start the thread to connect with the given device
        connectThread = new ConnectThread();
        connectThread.start();

        CustomMessage message = new CustomMessage(STATE_CONNECTING, new Device(device.getName(),device.getAddress()));

        setChanged();
        notifyObservers(message);

        setCurrentState(STATE_CONNECTING);
    }


    // manage Bluetooth connection
    @Override
    public synchronized void connected() {
        System.out.println("Connected to device "+ device.getAddress());
        // Cancel the thread
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }


        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }


        CustomMessage message = new CustomMessage(STATE_CONNECTED, new Device(device.getName(),device.getAddress()));

        setChanged();
        notifyObservers(message);

        setCurrentState(STATE_CONNECTED);
    }

    @Override
    public void connectionFailed() {

        CustomMessage message = new CustomMessage(STATE_CONNECTIONLOST, null);

        setChanged();
        notifyObservers(message);

        // Start the service over to restart listening mode
        this.start();
    }

    @Override
    public int getCurrentState() {
        return 0;
    }


    /**Accept Thread**/
    // runs while listening for incoming connections
    private class AcceptThread extends Thread {

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, APP_UUID);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            serverSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");
            System.out.println("Accepted Thread to device ");
            BluetoothSocket accepted;
            while (currentState != STATE_CONNECTED) {
                try {
                    accepted = serverSocket.accept();


                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (accepted != null) {
                    synchronized (this) {
                        switch (currentState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // start the connected thread.
                                System.out.println("Accepted Thread to device " + accepted.getRemoteDevice());
                                device = accepted.getRemoteDevice();
                                connected();
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate
                                // new socket.
                                try {
                                    accepted.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }


    /**Connect Thread**/
    // runs while attempting to make an outgoing connection
    private class ConnectThread extends Thread {

        public ConnectThread() {

            BluetoothSocket tmp = null;
            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(APP_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = tmp;
        }

        public void run() {
            setName("ConnectThread");
            System.out.println("Connect Thread to device ");
            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                System.out.println("Connect starting .......");
                socket.connect();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e2) {
                }
                System.out.println("Connect failed .......");
                e.printStackTrace();
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (this) {
                connectThread = null;
            }

            // Start the connected thread
            connected();
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

}
