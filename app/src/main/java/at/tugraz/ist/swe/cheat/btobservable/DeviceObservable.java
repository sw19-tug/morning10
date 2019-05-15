package at.tugraz.ist.swe.cheat.btobservable;

import java.util.Observable;
import java.util.Observer;

public class DeviceObservable extends Observable{

    private String device;

    public void setDevice(String message) {
        this.device = message;
        setChanged();
        notifyObservers(message);
    }

    public String getDevice() {
        return device;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        System.out.print("Registered TO Observer");
        //this.setDevice("TEST Device");
    }
}