package com.example.apssmo;

public class Device {
    private int deviceNum;
    private Request currentRequest = null;
    private double startTime;
    private double endTime;
    private double workingTime;
    private int acceptedRequests;

    public Device(int deviceNum) {
        this.deviceNum = deviceNum;
    }

    public int getDeviceNum() {
        return deviceNum;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(Request currentRequest) {
        this.currentRequest = currentRequest;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) { this.endTime = endTime; }

    public double getEndTime() {
        return endTime;
    }

    public void incrementAcceptedRequests() {
        this.acceptedRequests++;
    }

    public int getAcceptedRequests() {
        return acceptedRequests;
    }

    public void setWorkingTime(double someTime) { workingTime += someTime; }

    public double getWorkingTime() { return workingTime; }
}
