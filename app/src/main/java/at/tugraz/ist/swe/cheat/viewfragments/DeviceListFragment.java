package at.tugraz.ist.swe.cheat.viewfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.MainActivity;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;

public class DeviceListFragment  implements Observer {

    ArrayAdapter<String> deviceListAdapter;


    public DeviceListFragment(ArrayAdapter<String> deviceListAdapter)
    {
        this.deviceListAdapter = deviceListAdapter;
    }


    @Override
    public void update(Observable o, Object message) {
        CustomMessage customMessage = (CustomMessage) message;
        deviceListAdapter.add(customMessage.getDevice().getDevice_name()+"\n"
                +customMessage.getDevice().getDevice_address());

    }
}
