package at.tugraz.ist.swe.cheat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.tugraz.ist.swe.cheat.serviceimpl.RealBluetoothDeviceProvider;

public class MainActivity extends AppCompatActivity {

    //private BluetoothDeviceManager bluetoothDeviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bluetoothDeviceManager = new BluetoothDeviceManager(new RealBluetoothDeviceProvider());
    }

}
