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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

private
@RunWith(MockitoJUnitRunner.class)
public class HandlerUnitTest {

    private static final String DEVICE_ADDRESS = "Deviceaddress";
    @Mock BluetoothDevice bluetoothDevice;
    @Mock BluetoothGatt bluetoothGatt;
    @Mock Context context;
    @Mock Handler handler;

    @Before
    public void setUp() {
        initMocks(this);
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Runnable r = (Runnable) args[0];
            r.run();
        }).when(handler).post(any());
        when(bluetoothDevice.getAddress()).thenReturn(DEVICE_ADDRESS);
        when(bluetoothDevice.getName()).thenReturn("RICK");
        when(bluetoothDevice.connectGatt(eq(context), anyBoolean(), any(BluetoothGattCallback.class))).thenReturn(bluetoothGatt);
    }


    @Test
    public void TestConnectedMsg()
    {
       Message msg = handler.obtainMessage(MainActivity.MESSAGE_DEVICE_INFO);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.DEVICE_INFO, bluetoothDevice);
        msg.setData(bundle);
        handler.sendMessage(msg);

    }

}
