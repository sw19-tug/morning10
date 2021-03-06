package at.tugraz.ist.swe.cheat.serviceimpl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import at.tugraz.ist.swe.BluetoothDeviceState;
import at.tugraz.ist.swe.cheat.ChatMessage;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;
import at.tugraz.ist.swe.cheat.dto.Provider;
import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;
import at.tugraz.ist.swe.cheat.util.ConverterClassByte;

public class RealBluetoothDeviceProvider extends Provider implements BluetoothDeviceProvider{


    private static final String APP_NAME = "Cheating";
    private static final UUID APP_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");


    BluetoothDeviceState state = BluetoothDeviceState.STOPSCAN;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ReadWriteThread connectedThread;
    private int currentState;
    private static String device_address;

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
      // Cancel running thread
       if (connectedThread != null) {
           connectedThread.cancel();
           connectedThread = null;
       }


        setCurrentState(STATE_LISTEN);
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }


        CustomMessage message = new CustomMessage(STATE_LISTEN, null);

        setChanged();
        notifyObservers(message);
        setCurrentState(STATE_LISTEN);
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
      // Cancel running thread
       if (connectedThread != null) {	
           connectedThread.cancel();
           connectedThread = null;
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

      // Cancel any running thresd
       if (connectedThread != null) {
           connectedThread.cancel();
           connectedThread = null;
       }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

      // Start the thread to manage the connection and perform transmissions
       setCurrentState(STATE_CONNECTED);
       connectedThread = new ReadWriteThread();
       connectedThread.start();


        CustomMessage message = new CustomMessage(STATE_CONNECTED, new Device(device.getName(),device.getAddress()));

        setChanged();
        notifyObservers(message);

        setCurrentState(STATE_CONNECTED);
    }

    @Override
    public void connectionFailed() {

        CustomMessage message;
        // Start the service over to restart listening mode
        if(device != null && !device.getAddress().equals(device_address))
        {
            device_address = device.getAddress();
            message = new CustomMessage(STATE_CONNECTING, new Device(device.getName(), device.getAddress()));
            setChanged();
            notifyObservers(message);
            this.connect();
        }
        else
        {
            message = new CustomMessage(STATE_CONNECTIONLOST, null);
            setChanged();
            notifyObservers(message);
            this.start();
        }
    }

    @Override
    public int getCurrentState() {
        return currentState;
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
            while (currentState != STATE_CONNECTED) {
                try {
                    socket = serverSocket.accept();


                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (this) {
                        switch (currentState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // start the connected thread.
                                System.out.println("Accepted Thread to device " + socket.getRemoteDevice());
                                device = socket.getRemoteDevice();
                                connected();
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate
                                // new socket.
                                try {
                                    socket.close();
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

    @Override
    public void disconnected() throws IOException {
        CustomMessage message = new CustomMessage(STATE_LISTEN, new Device(device.getName(), device.getAddress()));

        socket.close();

        setChanged();
        notifyObservers(message);

        setCurrentState(STATE_LISTEN);

    }

    @Override
    public void write(byte[] out) {
        ReadWriteThread r;
        synchronized (this) {
            if (currentState != STATE_CONNECTED)
                return;
            r = connectedThread;
        }
        r.write(out);
    }


    @Override
    public void sendMessage(ChatMessage message) throws IOException {

        CustomMessage fullmessage = new CustomMessage(STATE_CHAT,
                new Device(BluetoothAdapter.getDefaultAdapter().getName(),
                        BluetoothAdapter.getDefaultAdapter().getAddress()),message);


        if(message != null)
        {
            byte [] data = ConverterClassByte.toByteArray(fullmessage);


            int subArraySize = 400;

            System.out.println("What is my size?" + data.length );
            write(String.valueOf(data.length).getBytes());


            for(int i=0; i < data.length; i+= subArraySize)
            {
                byte[] tempArray;
                tempArray = Arrays.copyOfRange(data,i,Math.min(data.length,i+subArraySize));
                write(tempArray);
            }

        }

    }



    /**ReadWrite Thread**/
    // runs during a connection with a remote device
    private class ReadWriteThread extends Thread {
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ReadWriteThread() {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            System.out.println("ReadWrite Thread to device ");
            byte[] buffer = null;
            int bytes = 0;
            int index = 0;
            boolean flag = true;


            // Keep listening to the InputStream
            while (true) {
                try {
                    // Read from the InputStream

                    if(flag)
                    {
                        try {
                            byte[] temp = new byte[inputStream.available()];
                            if(inputStream.read(temp) > 0)
                            {
                                bytes = Integer.parseInt(new String(temp,"UTF-8"));
                                System.out.println("GOT A SIZE " + bytes);
                                buffer = new byte[bytes];

                                flag = false;
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                    else {
                        try {
                            byte[] data = new byte[inputStream.available()];
                            int numbers = inputStream.read(data);
                            System.out.println("GOT B SIZE " + numbers);

                            System.arraycopy(data,0,buffer,index,numbers);
                            index = index+numbers;

                            if(index == bytes)
                            {

                                flag = true;
                                CustomMessage customMessage = (CustomMessage) ConverterClassByte.toObject(buffer);
                                System.out.println("This was the message" + customMessage.getMessage().getTimeStamp());
                                received(customMessage);
                                index = 0;

                                //MAYBE
                                //inputStream.close();
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    //bytes = inputStream.read(buffer);

                    /*try {
                        CustomMessage customMessage = (CustomMessage) ConverterClassByte.toObject(buffer);
                        received(customMessage);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/


                } catch (Exception e) {
                    e.printStackTrace();
                    connectionFailed();
                    break;
                }
            }
        }



        // write to OutputStream
        public void write(byte[] buffer) {
            try {
                outputStream.write(buffer);
                outputStream.flush();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public synchronized void received(CustomMessage customMessage) {

        System.out.println("Got the message!");
        System.out.println("Get message with date " + customMessage.getMessage().getTimeStamp());
        setChanged();

        notifyObservers(customMessage);
    }

}


