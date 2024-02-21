package com.example.apssmo;

public class Request {
    private double timeOfCreation;
    private String requestId;
    private int requestNum;
    private int sourceNum;
    private double startTimeInBuff;
    private double startTimeInSystem;
    private double endTimeInBuff;
    private double endTimeInSystem;

    public String getRequestId() {
        return requestId;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public int getSourceNum() {
        return sourceNum;
    }

    public double getTimeOfCreation() {
        return timeOfCreation;
    }

    public Request(double timeOfCreation, int requestNum, int sourceNum) {
        this.timeOfCreation = timeOfCreation;
        this.requestNum = requestNum;
        this.sourceNum = sourceNum;
        this.requestId = "(" + (sourceNum) + "," + (requestNum) + ")";
        this.startTimeInSystem = 0;
        this.startTimeInBuff = 0;
    }

    @Override
    public String toString() {
        return requestId;
    }

    public void setStartTimeInBuff(double startTimeInBuff) {
        this.startTimeInBuff = startTimeInBuff;
    }

    public void setStartTimeInSystem(double startTimeInSystem) {
        this.startTimeInSystem = startTimeInSystem;
    }

    public void setEndTimeInBuff(double endTimeInBuff) {
        this.endTimeInBuff = endTimeInBuff;
    }

    public void setEndTimeInSystem(double endTimeInSystem) {
        this.endTimeInSystem = endTimeInSystem;
    }

    public double getEndTimeInBuff() {
        return endTimeInBuff;
    }

    public double getStartTimeInBuff() {
        return startTimeInBuff;
    }

    public double getEndTimeInSystem() {
        return endTimeInSystem;
    }

    public double getStartTimeInSystem() {
        return startTimeInSystem;
    }
}
