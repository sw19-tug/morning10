package at.tugraz.ist.swe.cheat;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.ist.swe.BluetoothDeviceState;
import at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider;
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
    public void bluetoothOnTest() {
        BluetoothDeviceProvider bluetoothDeviceProvider = bluetoothDeviceManager.getBluetoothDeviceProvider();
        bluetoothDeviceProvider.setEnabled(true);
        assertTrue(bluetoothDeviceManager.isOn());
        assertEquals(bluetoothDeviceManager.getBtState(), BluetoothDeviceManager.BtState.ON);
    }

    @Test
    public void bluetoothOffTest() {
        BluetoothDeviceProvider bluetoothDeviceProvider = bluetoothDeviceManager.getBluetoothDeviceProvider();
        bluetoothDeviceProvider.setEnabled(false);
        assertFalse(bluetoothDeviceManager.isOn());
        assertEquals(bluetoothDeviceManager.getBtState(), BluetoothDeviceManager.BtState.OFF);
    }

    @Test
    public void scanTest(){
        bluetoothDeviceManager.startScanning();
        assertEquals(bluetoothDeviceManager.getBtState(), BluetoothDeviceManager.BtState.SCAN);
        assertEquals(bluetoothDeviceManager.getBluetoothDeviceProvider().getState(), BluetoothDeviceState.SCAN);
    }

    @Test
    public void stopScanTest()
    {
        bluetoothDeviceManager.startScanning();
        assertEquals(bluetoothDeviceManager.getBtState(), BluetoothDeviceManager.BtState.SCAN);
        assertEquals(bluetoothDeviceManager.getBluetoothDeviceProvider().getState(), BluetoothDeviceState.SCAN);

        bluetoothDeviceManager.stopScanning();
        assertEquals(bluetoothDeviceManager.getBtState(), BluetoothDeviceManager.BtState.ON);
        assertEquals(bluetoothDeviceManager.getBluetoothDeviceProvider().getState(), BluetoothDeviceState.STOPSCAN);
    }


}