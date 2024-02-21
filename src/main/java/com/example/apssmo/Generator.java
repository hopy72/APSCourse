package com.example.apssmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Generator {
    private final int numOfSources;
    private RegDispatcher regDispatcher;
    private ArrayList<Source> sources = new ArrayList<>();

    public Generator(int numOfSources, RegDispatcher regDispatcher) {
        this.numOfSources = numOfSources;
        this.regDispatcher = regDispatcher;
        for (int i = 1; i < numOfSources + 1; i++) {
            sources.add(new Source(i));
        }
    }

    public void generateRequest(double generationTime) {
        HashMap<Integer, Double> sourceNumAndTime = new HashMap<>();
        int sourceNumToGenerate = findSourceToGenerate();
        sourceNumAndTime.put(-15, 0.01);
        if (sourceNumToGenerate == 0)
            sourceNumToGenerate = 1;
        Source currentSource = sources.get(sourceNumToGenerate - 1);
        currentSource.incrementNumOfRequests();
        Request request = new Request(generationTime, currentSource.getNumOfRequests(), currentSource.getSourceNum());
        request.setStartTimeInBuff(generationTime);
        request.setStartTimeInSystem(generationTime);
        sourceNumAndTime = regDispatcher.addToBuff(request, generationTime);
        currentSource.incrementAcceptedRequests();
        if (sourceNumAndTime.get(-15) == null) {
            Set<Integer> setKeys = sourceNumAndTime.keySet();
            for (Integer k : setKeys) {
                sources.get(k - 1).incrementDeniedRequests();
                sources.get(k - 1).decrementAcceptedRequests();
                sources.get(k - 1).setTimeInBuff(sourceNumAndTime.get(k));
                sources.get(k - 1).setTimeInSystem(sourceNumAndTime.get(k));
            }
        }
    }

    public int findSourceToGenerate() {
       int maxNumRequests = findMaxCountRequests();
       Random random = new Random();
       ArrayList<Integer> arraySources = new ArrayList<>();
       int countTemp = 0;
       int countSources = 0;
       for (Source source : sources) {
           if (source.getNumOfRequests() < maxNumRequests) {
               arraySources.add(source.getSourceNum());
               countSources++;
           }
           countTemp++;
       }
       if (countSources > 0) {
           return arraySources.get(random.nextInt(0, countSources));
       }
       else {
           return random.nextInt(1, countTemp);
       }
    }

    public int findMaxCountRequests() {
        int tempNumOfRequests = 0;
        for (Source source : sources) {
            if (source.getNumOfRequests() > tempNumOfRequests) {
                tempNumOfRequests = source.getNumOfRequests();
            }
        }
        return tempNumOfRequests;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }
}
