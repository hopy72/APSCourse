package com.example.apssmo;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectDispatcher {
    private Buffer buffer;
    private static int cursor = 0;
    private DeviceRepository devices;
    private static int sourceNumPriority = 120;

    public SelectDispatcher(Buffer buffer, DeviceRepository devices) {
        this.buffer = buffer;
        this.devices = devices;
    }

    public int chooseDevice() {
        ArrayList<Device> listDevices = devices.getDevices();
        int idDevice = 0;
        int oldCursor = cursor;
        for (; cursor < devices.getNumOfDevices(); cursor++ ) {
            if (listDevices.get(cursor).getCurrentRequest() == null) {
                idDevice = cursor;
                cursor++;
                break;
            }
        }
        if (idDevice == 0) {
            cursor = 0;
            for (int i = 0; i < oldCursor; i++) {
                if (listDevices.get(i).getCurrentRequest() == null) {
                    idDevice = i;
                    cursor++;
                    break;
                }
            }
        }
        return idDevice;
    }

    public int chooseRequest() {
        int requestSourceId = 120;
        int flagId = 0;
        if (sourceNumPriority == 120 && !buffer.isEmpty()) {
            Request request = findMaxPriority();
            int sourceNum = request.getSourceNum();
            sourceNumPriority = sourceNum;
        }
        for (Request requestIter : buffer.getRequests()) {
            if (requestIter.getSourceNum() == sourceNumPriority) {
                requestSourceId = requestIter.getSourceNum();
            }
        }
        for (Request requestIter : buffer.getRequests()) {
            if (requestIter.getSourceNum() == sourceNumPriority) {
                flagId++;
            }
        }
        if (flagId <= 1) {
            sourceNumPriority = 120;
        }
        return requestSourceId;
    }

    public void processingToDevice(double startingTime) {
        if (devices.repositoryIsFull()) {
            return;
        }
        int flag = 0;
        double time = 0;
        while (buffer.hasElement() && !devices.repositoryIsFull()) {
            if (flag == 0) {
                time = Controller.getReleaseTime();
                flag = 1;
            }
        int deviceToChoose = chooseDevice();
        int requestToChoose = chooseRequest();
        for (Device device : devices.getDevices()) {
            if (device.getDeviceNum() == deviceToChoose) {
                for (Request request : buffer.getRequests()) {
                    if (request.getSourceNum() == requestToChoose) {
                        device.setCurrentRequest(request);
                        device.setStartTime(startingTime);
                        device.incrementAcceptedRequests();
                        request.getSourceNum();
                        request.setEndTimeInBuff(startingTime);
                        device.setEndTime(time);
                        System.out.println("Заявка под номером " + request.getRequestNum() +
                                " перешла в обработку к девайсу под номером " + deviceToChoose + " от источника " + request.getSourceNum());
                        buffer.deleteRequest(request);
                        break;
                    }
                }
            }
            }
        }
    }

    public HashMap<Integer, Request> freeDevice(double currTime) {
        HashMap<Integer, Request> sourceNumAndRequest = new HashMap<>();
        for (Device device : devices.getDevices()) {
            if (currTime - device.getEndTime() > 0 && device.getCurrentRequest() != null) {
                device.setWorkingTime(device.getEndTime() - device.getStartTime());
                Request request = device.getCurrentRequest();
                request.setEndTimeInSystem(currTime);
                sourceNumAndRequest.put(request.getSourceNum(), request);
                device.setCurrentRequest(null);
                System.out.println("Заявка под номером " + request.getRequestNum() + " от источника под номером " + request.getSourceNum() + " вышла из устройства " + device.getDeviceNum());
            }
        }
        return sourceNumAndRequest;
    }

    public Request findMaxPriority() {
        if (!buffer.isEmpty()) {
            int sourceNumTemp = buffer.getRequests().get(0).getSourceNum();
            int numInArray = 0;
            int someIndex = 0;
            Request tempRequest = buffer.getRequests().get(0);
            for (Request request : buffer.getRequests()) {
                if (request.getSourceNum() > sourceNumTemp) {
                    tempRequest = request;
                    sourceNumTemp = request.getSourceNum();
                    numInArray = someIndex;
                } else if (request.getSourceNum() == sourceNumTemp) {
                    if (request.getTimeOfCreation() < buffer.getRequests().get(numInArray).getTimeOfCreation()) {
                        tempRequest = request;
                        sourceNumTemp = request.getSourceNum();
                        numInArray = someIndex;
                    }
                }
                someIndex++;
            }
            return tempRequest;
        }
        return null;
    }

    public boolean isDevicesFinished() {
        boolean flag = true;
        for (Device device : devices.getDevices()) {
            if (device.getCurrentRequest() != null) {
                flag = false;
            }
        }
        return flag;
    }

    public DeviceRepository getDevices() {
        return devices;
    }
}
