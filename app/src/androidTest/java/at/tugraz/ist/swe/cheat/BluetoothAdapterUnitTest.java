package at.tugraz.ist.swe.cheat;

import android.bluetooth.BluetoothDevice;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class BluetoothAdapterUnitTest {

    BluetoothDeviceManager bluetoothDeviceManager;
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        BluetoothDeviceProvider bluetoothDeviceProvider = new DummyBluetoothDeviceProvider();
        bluetoothDeviceManager = new bluetoothDeviceManager(bluetoothDeviceProvider);
    }

    @Test
    public void bluetoothOn() {
        assertTrue(bluetoothDeviceManager.isOn());
    }
}