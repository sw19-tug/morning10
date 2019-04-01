package at.tugraz.ist.swe.cheat;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatConnectionUnitTest {

    @Test
    public void chatListenTest()
    {
        ChatController chatController = new ChatController();
        chatController.start();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
    }

    @Test
    public void chatConnectTest()
    {
        ChatController chatController = new ChatController();
        chatController.connect();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.CONNECTING);
    }

    @Test
    public void chatConnectedTest()
    {
        ChatController chatController = new ChatController();
        chatController.connected();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.CONNECTED);
    }

    @Test
    public void chatStopTest()
    {
        ChatController chatController = new ChatController();
        chatController.stop();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.NONE);
    }

    @Test
    public void chatConnectionLostTest()
    {
        ChatController chatController = new ChatController();
        chatController.connectionLost();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
    }

    @Test
    public void chatConnectionFaildTest()
    {
        ChatController chatController = new ChatController();
        chatController.connectionFailed();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
    }


}
