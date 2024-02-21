package com.example.apssmo;

public class Source {
    private int sourceNum;
    private int numOfRequests;
    private int acceptedRequests;
    private int deniedRequests;
    private double timeInBuff;
    private double timeInSystem;
    public Source(int sourceNum) {
        this.sourceNum = sourceNum;
        this.numOfRequests = 0;
        this.timeInBuff = 0;
        this.timeInSystem = 0;
    }

    public int getSourceNum() {
        return sourceNum;
    }

    public int getNumOfRequests() {
        return numOfRequests;
    }

    public void incrementNumOfRequests() {
        numOfRequests++;
    }

    public void incrementAcceptedRequests() {acceptedRequests++;}

    public void incrementDeniedRequests() {deniedRequests++;}

    public void decrementAcceptedRequests() {acceptedRequests--;}

    public int getAcceptedRequests() {
        return acceptedRequests;
    }

    public int getDeniedRequests() {
        return deniedRequests;
    }

    public void setTimeInBuff(double timeInBuff) {
        this.timeInBuff = this.timeInBuff + timeInBuff;
    }

    public void setTimeInSystem(double timeInSystem) {
        this.timeInSystem = this.timeInSystem + timeInSystem;
    }

    public double getTimeInBuff() {
        return timeInBuff;
    }

    public double getTimeInSystem() {
        return timeInSystem;
    }
}
