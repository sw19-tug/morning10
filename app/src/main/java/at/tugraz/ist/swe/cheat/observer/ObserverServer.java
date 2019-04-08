package at.tugraz.ist.swe.cheat.observer;

import android.os.Message;

import java.util.Observable;
import java.util.Observer;

public class ObserverServer implements Observer {

    private Object message;

    @Override
    public void update(Observable o, Object message) {
        this.setMessage(message);
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
