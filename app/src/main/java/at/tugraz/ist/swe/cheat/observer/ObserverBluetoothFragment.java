package at.tugraz.ist.swe.cheat.observer;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.DTO.Device;
import at.tugraz.ist.swe.cheat.MainActivity;

public class ObserverBluetoothFragment extends Fragment implements Observer {

    MainActivity mainActivity;
    private ArrayAdapter<String> devices;

    @Override
    public void update(Observable o, Object arg) {
        Device device = (Device) arg;
        devices.add(device.getDevice_name() + "\n" + device.getDevice_address());
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public ArrayAdapter<String> getDevices() {
        return devices;
    }

    public void setDevices(ArrayAdapter<String> devices) {
        this.devices = devices;
    }
}
