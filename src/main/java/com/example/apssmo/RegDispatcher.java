package com.example.apssmo;

import java.util.HashMap;

public class RegDispatcher {
    private Buffer buffer;

    public RegDispatcher(Buffer buffer) {
        this.buffer = buffer;
    }

    public HashMap<Integer, Double> addToBuff(Request request, double generationTime) {
        HashMap<Integer, Double> sourceNumAndTime = new HashMap<>();
        if (!buffer.isFull()) {
            buffer.addRequest(request);
            System.out.println("Заявка под номером " + request.getRequestId() + " встала в конец буфера");
            sourceNumAndTime.put(-15, 0.1);
            return sourceNumAndTime;
        }
        else {
            Request requestToDelete = buffer.findMinPriority();
            buffer.deleteRequest(requestToDelete);
            requestToDelete.setEndTimeInBuff(generationTime);
            requestToDelete.setEndTimeInSystem(generationTime);
            buffer.addRequest(request);
            sourceNumAndTime.put(requestToDelete.getSourceNum(), requestToDelete.getEndTimeInBuff() - requestToDelete.getStartTimeInBuff());
            System.out.println("Заявка под номером " + request.getRequestId() + " встала в буфер на место " + requestToDelete.getRequestId());
            return sourceNumAndTime;
        }
    }

    public Buffer getBuffer() {
        return buffer;
    }
}
