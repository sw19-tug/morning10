package at.tugraz.ist.swe.cheat.observable;

import java.util.Observable;

public class ObservedClient extends Observable {

    private String message;

    public void setMessage(String message) {
        this.message = message;
        setChanged();
        notifyObservers(message);
    }

    public String getMessage() {
        return message;
    }

}
