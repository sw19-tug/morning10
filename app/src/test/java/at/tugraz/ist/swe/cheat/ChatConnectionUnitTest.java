package at.tugraz.ist.swe.cheat;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatConnectionUnitTest {

    @Test
    public void chatListenTest()
    {
        ChatController chatController = new ChatController();
        assertEquals(chatController.getConnectionState(), ChatController.ConnectionState.LISTEN);
        assertTrue(chatController.getListenThread() != null);
        assertTrue(chatController.getConnectThread() == null);
        assertTrue(chatController.getConnectingThread() == null);
    }

}
