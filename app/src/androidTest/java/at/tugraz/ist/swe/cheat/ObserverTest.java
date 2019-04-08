package at.tugraz.ist.swe.cheat;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class ObserverTest {

    private static final String DEVICE_ADDRESS = "Deviceaddress";
    @Mock BluetoothDevice bluetoothDevice;
    @Mock BluetoothGatt bluetoothGatt;
    @Mock Context context;
    @Mock Handler handler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Runnable r = (Runnable) args[0];
            r.run();
        }).when(handler).post(Matchers.any());
        Mockito.when(bluetoothDevice.getAddress()).thenReturn(DEVICE_ADDRESS);
        Mockito.when(bluetoothDevice.getName()).thenReturn("RICK");
        Mockito.when(bluetoothDevice.connectGatt(Matchers.eq(context), Matchers.anyBoolean(), Matchers.any(BluetoothGattCallback.class))).thenReturn(bluetoothGatt);
    }

    @Test
    public void TestConnecting()
    {
       ObservedClient obserable = new ObservedClient();
       ObserverServer observer = new ObserverServer();

       obserable.addObserver(observer);
       obserable.setMessage("Connecting ...");
       assertEquals(observer.getMessage(), "Connecting ...");
    }

    @Test
    public void TestHandshakeMessage()
    {
       ObservedClient obserable = new ObservedClient();
       ObserverServer observer = new ObserverServer();

       obserable.addObserver(observer);
       obserable.setMessage("Connecting to " + bluetoothDevice.getName());
       assertEquals(observer.getMessage(), "Connecting to " + "RICK");
    }
    @Test
    public void TestDisconnect()
    {
       ObservedClient obserable = new ObservedClient();
       ObserverServer observer = new ObserverServer();

       obserable.addObserver(observer);
       obserable.setMessage("Disconnected");
       assertEquals(observer.getMessage(), "Disconnected");
    }

    @Test
    public void TestChat()
    {
       ObservedClient obserableA = new ObservedClient();
       ObservedClient obserableB = new ObservedClient();
       ObserverServer observer = new ObserverServer();

       obserableA.addObserver(observer);
       obserableB.addObserver(observer);
       obserableA.setMessage("Hallo B");
       assertEquals(observer.getMessage(), "Hallo B");
       obserableB.setMessage("Hallo A");
       assertEquals(observer.getMessage(), "Hallo A");

    }



}
