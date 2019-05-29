package at.tugraz.ist.swe.cheat;

import org.junit.Before;
import org.junit.Test;

import at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatConnectionUnitTest {

    private ChatController chatController;
    private DummyBluetoothDeviceProvider dummyBluetoothDeviceProvider;

    @Before
    public void setUp() {

        dummyBluetoothDeviceProvider = new DummyBluetoothDeviceProvider();
        chatController = new ChatController(dummyBluetoothDeviceProvider);
    }

    @Test
    public void chatListenTest()
    {

        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
        chatController.start();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
    }

    @Test
    public void chatConnectingTest()
    {
        chatController.connect();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.CONNECTING);
    }

    @Test
    public void chatConnectedTest()
    {
        chatController.connected();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.CONNECTED);

    }

    @Test
    public void chatStopTest()
    {
        chatController.stop();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.NONE);
    }

    @Test
    public void chatConnectionLostTest()
    {
        chatController.connectionLost();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
    }

    @Test
    public void chatConnectionFaildTest()
    {
        chatController.connectionFailed();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
    }




}
