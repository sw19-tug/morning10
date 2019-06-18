package at.tugraz.ist.swe.cheat.btobserver;

import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.dto.CustomMessage;


public class BasicObserver implements Observer {

    private CustomMessage message;

    @Override
    public void update(Observable o, Object arg) {
        this.message = (CustomMessage) arg;
    }

    public CustomMessage getMessage() {
        return message;
    }

    public void setMessage(CustomMessage message) {
        this.message = message;
    }
}
