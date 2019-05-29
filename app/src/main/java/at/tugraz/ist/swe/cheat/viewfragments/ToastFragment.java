package at.tugraz.ist.swe.cheat.viewfragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.MainActivity;
import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;

import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTING;


public class ToastFragment extends Fragment implements Observer {

    MainActivity mainActivity;
    CustomMessage message;

    @Override
    public void update(Observable o, final Object message) {
        this.message = (CustomMessage) message;
        if(((CustomMessage) message).getDevice() != null)
        {
            mainActivity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    String toastString = "";
                    switch (((CustomMessage) message).getState())
                    {
                        case STATE_CONNECTED:
                            toastString = "is connected to ";
                            break;
                        case STATE_CONNECTING:
                            toastString = "connecting to ";
                            break;
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
}