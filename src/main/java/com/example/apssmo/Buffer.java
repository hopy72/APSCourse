package com.example.apssmo;

import java.util.ArrayList;

public class Buffer {
    private int bufferSize;
    private int usingSize;
    private ArrayList<Request> requests = new ArrayList<Request>();

    public Buffer(int size) {
        bufferSize = size;
        usingSize = 0;
    }

    public boolean isEmpty() {
        return usingSize == 0;
    }

    public boolean isFull() {
        return bufferSize == usingSize;
    }

    public boolean hasElement() { return usingSize > 0; }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void addRequest(Request request) {
        if (!isFull()) {
            requests.add(request);
            usingSize++;
        }
    }

    public void deleteRequest(Request request) {
        if (requests.contains(request)) {
            requests.remove(request);
            usingSize--;
        }
    }

    public Request findMinPriority() {
        int sourceNumTemp = requests.get(0).getSourceNum();
        int numInArray = 0;
        int someIndex = 0;
        Request tempRequest = requests.get(0);
        for (Request request: requests) {
            if (request.getSourceNum() < sourceNumTemp) {
                tempRequest = request;
                sourceNumTemp = request.getSourceNum();
                numInArray = someIndex;
            } else if (request.getSourceNum() == sourceNumTemp) {
                if (request.getTimeOfCreation() > requests.get(numInArray).getTimeOfCreation()) {
                    tempRequest = request;
                    sourceNumTemp = request.getSourceNum();
                    numInArray = someIndex;
                }
            }
            someIndex++;
        }
        return tempRequest;
    }

    @Override
    public String toString() {
        return "Сейчас в буфере: " + requests.toString();
    }
}
