package at.tugraz.ist.swe.cheat;

import android.widget.ArrayAdapter;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.btobservable.DeviceObservable;
import at.tugraz.ist.swe.cheat.btobserver.BasicObserver;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;
import at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider;
import at.tugraz.ist.swe.cheat.viewfragments.DeviceListFragment;

import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static org.junit.Assert.assertEquals;

public class DeviceObservableTest {

    private DummyBluetoothDeviceProvider provider;
    private DeviceObservable deviceObservable;
    private Observer test;
    private CustomMessage testmessage;


    @Before
    public void setUp() {
        this.provider = new DummyBluetoothDeviceProvider();
        this.deviceObservable = new DeviceObservable();
         test = new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                 testmessage = (CustomMessage) arg;
            }
         };

         this.deviceObservable.addObserver(test);
    }


    @Test
    public void addDeviceTest()
    {

        CustomMessage customMessage = new CustomMessage(STATE_CONNECTED,
                new Device("Dummy Device","00:11:22:AA:BB:CC"));
        deviceObservable.addDevice(customMessage);

        assertEquals(testmessage.getDevice().getDevice_address(), "00:11:22:AA:BB:CC");

    }


}
