package at.tugraz.ist.swe.cheat.viewfragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.MainActivity;
import at.tugraz.ist.swe.cheat.R;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;

import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTING;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_LISTEN;


public class ToastFragment extends Fragment implements Observer {

    private MainActivity mainActivity;
    private CustomMessage message;
    private String toastString = "";

    @Override
    public void update(Observable o, final Object message) {
        this.message = (CustomMessage) message;
        if(((CustomMessage) message).getDevice() != null)
        {
            mainActivity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toolbar myToolbar = (Toolbar)mainActivity.findViewById(R.id.menu);

                    MenuItem btConnect = (MenuItem)myToolbar.getMenu().findItem(R.id.bt_connect);
                    switch (((CustomMessage) message).getState())
                    {
                        case STATE_CONNECTED:
                            toastString = "is connected to ";
                            myToolbar.setBackgroundColor(0xff66bb6a);
                            btConnect.setIcon(R.drawable.ic_wifi_tethering_black_24dp);
                            break;
                        case STATE_CONNECTING:
                            toastString = "connecting to ";
                            break;
                        case STATE_LISTEN:
                            toastString = "is disconnected from ";
                            myToolbar.setBackgroundColor(0xffffffff);
                            btConnect.setIcon(R.drawable.ic_portable_wifi_off_black_24dp);
                            break;
                        default:
                            return;
                    }
                    toastString += (String) ((CustomMessage) message).getDevice().getDevice_name();
                    Toast.makeText(mainActivity.getApplicationContext(),
                            toastString, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    public CustomMessage getMessage() {
        return message;
    }

    public void setMessage(CustomMessage message) {
        this.message = message;
    }

    public String getToastString() {
        return toastString;
    }

}