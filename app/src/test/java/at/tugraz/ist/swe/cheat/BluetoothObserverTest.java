package at.tugraz.ist.swe.cheat;


import org.junit.Before;
import org.junit.Test;

import at.tugraz.ist.swe.cheat.btobserver.BasicObserver;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider;

import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTING;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_LISTEN;
import static junit.framework.TestCase.assertEquals;


public class BluetoothObserverTest {

    private DummyBluetoothDeviceProvider provider;
    private BasicObserver basicObserver;


    @Before
    public void setUp() {
        this.provider = new DummyBluetoothDeviceProvider();
        this.basicObserver = new BasicObserver();

        provider.addObserver(basicObserver);
    }


    @Test
    public void connectDevice()
    {
        this.provider.connectToDevice("00:11:22:AA:BB:CC");
        CustomMessage message  = this.basicObserver.getMessage();

        assertEquals(message.getState(),STATE_CONNECTING);
        assertEquals(message.getDevice().getDevice_name(),"TEST DUMMY");
    }

    @Test
    public void listenDevice()
    {
        this.provider.connectToDevice("00:11:22:AA:BB:CC");
        this.provider.connected();
        CustomMessage message  = this.basicObserver.getMessage();

        assertEquals(message.getState(),STATE_CONNECTED);
        assertEquals(message.getDevice().getDevice_name(),"TEST DUMMY");
    }

    @Test
    public void connectFailed()
    {
        this.provider.connectionFailed();
        CustomMessage message  = this.basicObserver.getMessage();

        assertEquals(message.getState(),STATE_LISTEN);
    }


}
