package at.tugraz.ist.swe.cheat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.ist.swe.cheat.services.BluetoothDeviceProvider;

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
        bluetoothDeviceManager = new BluetoothDeviceManager(new DummyBluetoothDeviceProvider());
    }

    @Test
    public void bluetoothOn() {
        BluetoothDeviceProvider bluetoothDeviceProvider = bluetoothDeviceManager.getBluetoothDeviceProvider();
        bluetoothDeviceProvider.setEnabled(true);
        assertTrue(bluetoothDeviceManager.isOn());
        assertEquals(bluetoothDeviceManager.getState(),BTState.ON);
    }
    public void bluetoothOff() {
        BluetoothDeviceProvider bluetoothDeviceProvider = bluetoothDeviceManager.getBluetoothDeviceProvider();
        bluetoothDeviceProvider.setEnabled(false);
        assertFalse(bluetoothDeviceManager.isOn());
        assertEquals(bluetoothDeviceManager.getState(),BTState.OFF);
    }

    




}