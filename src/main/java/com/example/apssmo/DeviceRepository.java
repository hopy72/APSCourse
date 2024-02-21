package com.example.apssmo;

import java.util.ArrayList;

public class DeviceRepository {
    private ArrayList<Device> devices = new ArrayList<>();
    private int numOfDevices;

    public DeviceRepository(int numOfDevices) {
        this.numOfDevices = numOfDevices;
        for (int i = 0; i < this.numOfDevices; i++) {
            devices.add(new Device(i));
        }
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public int getNumOfDevices() {
        return numOfDevices;
    }

    public boolean repositoryIsFull() {
        boolean flag = true;
        for (Device device : devices) {
            if (device.getCurrentRequest() == null) {
                flag = false;
            }
        }
        return flag;
    }
}
