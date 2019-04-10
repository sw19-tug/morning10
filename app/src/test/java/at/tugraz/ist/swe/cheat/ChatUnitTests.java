package at.tugraz.ist.swe.cheat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.os.Parcelable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatUnitTests {

    private static final String SENDER_ADDRESS = "00:d0a:95:9d:68:16";

    @Test
    public void messageConstructor() {
        final String MESSAGE = "Hello Receiver!";
        final int MESSAGE_ID = 0;
        final Date timeStamp = new Date();
        ChatMessage messageObj = new ChatMessage(MESSAGE_ID, SENDER_ADDRESS, MESSAGE, timeStamp);
        assertEquals(MESSAGE_ID, messageObj.getId());
        assertEquals(SENDER_ADDRESS, messageObj.getSenderAddress());
        assertEquals(MESSAGE, messageObj.getMessage());
        assertEquals(timeStamp, messageObj.getTimeStep());
    }
}