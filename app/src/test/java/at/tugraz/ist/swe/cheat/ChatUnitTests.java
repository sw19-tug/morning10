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

    private static final String DEVICE_ADDRESS = "00:d0a:95:9d:68:16";
    private static final String DEVICE_NAME = "Falco's Blackberry";

    @Mock
    BluetoothDevice sender;

    @Before
    public void setUp() {
        initMocks(this);
        when(sender.getAddress()).thenReturn(DEVICE_ADDRESS);
        when(sender.getName()).thenReturn(DEVICE_NAME);
    }

    @Test
    public void messageConstructor() {
        final String MESSAGE = "Hello Receiver!";
        ChatMessage messageObj = new ChatMessage(sender, MESSAGE);
        assertEquals(sender, messageObj.getSender());
        assertEquals(MESSAGE, messageObj.getMessage());
    }
}