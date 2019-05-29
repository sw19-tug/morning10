package at.tugraz.ist.swe.cheat.btobservable;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import at.tugraz.ist.swe.cheat.dto.CustomMessage;

public class DeviceObservable extends Observable{

    private Set<String> deviceLists;
    private String device;

    public DeviceObservable()
    {
        this.deviceLists = new HashSet<>();
    }

    public void setDevice(CustomMessage message) {

        if(!deviceLists.contains(message.getDevice().getDevice_address()))
        {
            this.deviceLists.add(message.getDevice().getDevice_address());
            setChanged();
            notifyObservers(message);
        }

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