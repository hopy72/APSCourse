package com.example.apssmo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


import static java.lang.Thread.sleep;


public class Controller {
    private static double alpha;
    private static double beta;
    private static double lambda;
    private static double workingTime = 0;
    private static double startOfWorkingTime = 0;
    private static int countOfRequests = 0;
    private static int countOfSources = 0;
    private static int countOfDevices = 0;
    private static int currRequests;
    private Buffer buffer;
    private DeviceRepository deviceRepository;

    private Generator generator;
    private RegDispatcher regDispatcher;
    private SelectDispatcher selectDispatcher;

    public Controller(double alpha, double beta, double lambda, int countOfRequests, int countOfSource, int countOfDevices, int buffSize) {
        Controller.alpha = alpha;
        Controller.beta = beta;
        Controller.lambda = lambda;
        Controller.countOfSources = countOfSource;
        this.buffer = new Buffer(buffSize);
        this.deviceRepository = new DeviceRepository(countOfDevices);
        this.regDispatcher = new RegDispatcher(buffer);
        this.generator = new Generator(countOfSource, regDispatcher);
        this.selectDispatcher = new SelectDispatcher(buffer, deviceRepository);
        Controller.countOfRequests = countOfRequests;
    }

    public void stepByStepSimulation(int countOfRequests) {
        HashMap<Integer, Request> sourceNumAndRequest = new HashMap<>();
        currRequests = 0;
        double generationTime = 0.1;
        startOfWorkingTime = generationTime;
        Scanner scanner = new Scanner(System.in);
        ArrayList<Request> tempRequests= new ArrayList<>();
        while (currRequests < countOfRequests) {
            if (scanner.nextInt() == 1) {

                System.out.println("---------------CLOCK " + currRequests + "------------------\n");
                setWorkingTime(generationTime);

                for (Request request : buffer.getRequests()) {
                    if (!tempRequests.contains(request)) {
                        tempRequests.add(request);
                    }
                }

                sourceNumAndRequest = selectDispatcher.freeDevice(generationTime);

                Set<Integer> setKeys = sourceNumAndRequest.keySet();
                for (Integer k : setKeys) {
                    generator.getSources().get(k - 1).setTimeInBuff(sourceNumAndRequest.get(k).getEndTimeInBuff() - sourceNumAndRequest.get(k).getStartTimeInBuff());
                    generator.getSources().get(k - 1).setTimeInSystem(sourceNumAndRequest.get(k).getEndTimeInSystem() - sourceNumAndRequest.get(k).getStartTimeInSystem());
                }
                selectDispatcher.processingToDevice(generationTime);

                generator.generateRequest(generationTime);
                currRequests++;
                generationTime = getGenerationTime();

                System.out.printf("\n%-10s %-10s %-10s %-15s %-15s %-15s %-15s %-15s%n", "Sources", "Accepted", "Denied", "efficiency", "Avg time in buffer", "Avg time in system", "Disperion buffer", "Dispersion system");
                for (Source source : generator.getSources()) {
                    double AverageTimeInBuff = source.getTimeInBuff() / ((double)source.getAcceptedRequests() + (double)source.getDeniedRequests());
                    double AverageTimeInSystem = source.getTimeInSystem() / ((double)source.getAcceptedRequests() + (double)source.getDeniedRequests());
                    double TimeIBuff = 0;
                    double TimeISystem = 0;
                    for (Request request : buffer.getRequests()) {
                        if (request.getSourceNum() == source.getSourceNum()) {
                            TimeIBuff += Math.pow(((request.getEndTimeInBuff() - request.getStartTimeInBuff()) - AverageTimeInBuff) , 2);
                            TimeISystem += Math.pow(((request.getEndTimeInSystem() - request.getStartTimeInSystem()) - AverageTimeInSystem) , 2);
                        }
                    }
                    System.out.printf("%-10s %-10s %-10s %-15s %-15s %-15s %-15s %-15s%n", source.getSourceNum(), source.getAcceptedRequests(),
                            source.getDeniedRequests(), (double)source.getAcceptedRequests() / ((double)source.getAcceptedRequests() + (double)source.getDeniedRequests()),
                            AverageTimeInBuff, AverageTimeInSystem, TimeIBuff / (source.getNumOfRequests() - 1), TimeISystem / (source.getNumOfRequests() - 1)
                    );
                }

                System.out.printf("\nBuffer state: ");
                System.out.printf("\n%-10s %-10s%n", "N", "(sourceNum, requestNum)");
                int i = 0;
                for (Request request : buffer.getRequests()) {
                    System.out.printf("%-10s %-10s%n", i, request.getRequestId());
                    i++;
                }

                System.out.printf("\n%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Devices", "CurrentRequest", "proccesedRequests", "workingTime", "fullTimeOfWorking", "inactivityTime", "efficiency");
                for (Device device : deviceRepository.getDevices()) {
                    Request request = device.getCurrentRequest();
                    double inactivityTime = generationTime - startOfWorkingTime - device.getWorkingTime();
                    if (request == null) {
                        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", device.getDeviceNum(), "empty", device.getAcceptedRequests(),
                                device.getWorkingTime(), generationTime - startOfWorkingTime, inactivityTime, device.getWorkingTime() / generationTime - startOfWorkingTime);
                    }
                    else {
                        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", device.getDeviceNum(), device.getCurrentRequest().getRequestId(), device.getAcceptedRequests(),
                                device.getWorkingTime(), generationTime - startOfWorkingTime, inactivityTime, device.getWorkingTime() / generationTime - startOfWorkingTime);
                    }
                }
                System.out.println("\n");
                try {
                    sleep(100);
                } catch (Exception e) {

                }
            }
        }
        finishWork(generationTime, tempRequests);
    }

    public void autoModeSimulation(int countOfRequests) {
        currRequests = 0;
        double generationTime = 0.1;
        ArrayList<Request> tempRequests= new ArrayList<>();
        HashMap<Integer, Request> sourceNumAndRequest = new HashMap<>();
        while (currRequests < countOfRequests) {
            System.out.println("---------------CLOCK " + currRequests + "------------------\n");
            System.out.println(regDispatcher.getBuffer());
            setWorkingTime(generationTime);

            for (Request request : buffer.getRequests()) {
                if (!tempRequests.contains(request)) {
                    tempRequests.add(request);
                }
            }

            sourceNumAndRequest = selectDispatcher.freeDevice(generationTime);

            Set<Integer> setKeys = sourceNumAndRequest.keySet();
            for (Integer k : setKeys) {
                generator.getSources().get(k - 1).setTimeInBuff(sourceNumAndRequest.get(k).getEndTimeInBuff() - sourceNumAndRequest.get(k).getStartTimeInBuff());
                generator.getSources().get(k - 1).setTimeInSystem(sourceNumAndRequest.get(k).getEndTimeInSystem() - sourceNumAndRequest.get(k).getStartTimeInSystem());
            }

            selectDispatcher.processingToDevice(generationTime);

            generator.generateRequest(generationTime);
            currRequests++;
            generationTime = getGenerationTime();
            System.out.println("\n");
            try {
                sleep(1);
            } catch (Exception e) {

            }
        }
        finishWork(generationTime, tempRequests);
    }

    public void finishWork(double currentTime, ArrayList<Request> tempRequests) {
        System.out.println("Finishing touches...");
        HashMap<Integer, Request> sourceNumAndRequest = new HashMap<>();
        while (true) {
            System.out.println("---------------CLOCK " + currRequests + "------------------");
            sourceNumAndRequest = selectDispatcher.freeDevice(currentTime);

            Set<Integer> setKeys = sourceNumAndRequest.keySet();
            for (Integer k : setKeys) {
                generator.getSources().get(k - 1).setTimeInBuff(sourceNumAndRequest.get(k).getEndTimeInBuff() - sourceNumAndRequest.get(k).getStartTimeInBuff());
                generator.getSources().get(k - 1).setTimeInSystem(sourceNumAndRequest.get(k).getEndTimeInSystem() - sourceNumAndRequest.get(k).getStartTimeInSystem());
            }

            selectDispatcher.processingToDevice(currentTime);
            currRequests++;
            try {
                sleep(1);
            }
            catch(Exception e) {

            }
            if (selectDispatcher.isDevicesFinished()) {
                break;
            }
            setWorkingTime(currentTime);
            currentTime = getGenerationTime();
        }
        System.out.printf("\n%-10s %-10s %-10s %-15s %-15s %-15s %-15s %-15s%n", "Sources", "Accepted", "Denied", "efficiency", "Avg time in buffer", "Avg time in system", "Disperion buffer", "Dispersion system");
        for (Source source : generator.getSources()) {
            double AverageTimeInBuff = source.getTimeInBuff() / ((double)source.getAcceptedRequests() + (double)source.getDeniedRequests());
            double AverageTimeInSystem = source.getTimeInSystem() / ((double)source.getAcceptedRequests() + (double)source.getDeniedRequests());
            double TimeIBuff = 0;
            double TimeISystem = 0;
            for (Request request : tempRequests) {
                if (request.getSourceNum() == source.getSourceNum()) {
                    TimeIBuff += Math.pow(((request.getEndTimeInBuff() - request.getStartTimeInBuff()) - AverageTimeInBuff) , 2);
                    TimeISystem += Math.pow(((request.getEndTimeInSystem() - request.getStartTimeInSystem()) - AverageTimeInSystem) , 2);
                }
            }
            System.out.printf("%-10s %-10s %-10s %-15s %-15s %-15s %-15s %-15s%n", source.getSourceNum(), source.getAcceptedRequests(),
                    source.getDeniedRequests(), (double)source.getAcceptedRequests() / ((double)source.getAcceptedRequests() + (double)source.getDeniedRequests()),
                    AverageTimeInBuff, AverageTimeInSystem, TimeIBuff / (source.getNumOfRequests() - 1), TimeISystem / (source.getNumOfRequests() - 1)
                    );
        }

        System.out.printf("\nBuffer state: ");
        System.out.printf("\n%-10s %-10s%n", "N", "(sourceNum, requestNum)");
        int i = 0;
        for (Request request : buffer.getRequests()) {
            System.out.printf("%-10s %-10s%n", i, request.getRequestId());
            i++;
        }

        System.out.printf("\n%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Devices", "CurrentRequest", "proccesedRequests", "workingTime", "fullTimeOfWorking", "inactivityTime", "efficiency");
        for (Device device : deviceRepository.getDevices()) {
            Request request = device.getCurrentRequest();
            double inactivityTime = currentTime - startOfWorkingTime - device.getWorkingTime();
            if (request == null) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", device.getDeviceNum(), "empty", device.getAcceptedRequests(),
                        device.getWorkingTime(), currentTime - startOfWorkingTime, inactivityTime, device.getWorkingTime() / (currentTime - startOfWorkingTime));
            }
            else {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", device.getDeviceNum(), device.getCurrentRequest().getRequestId(), device.getAcceptedRequests(),
                        device.getWorkingTime(), currentTime - startOfWorkingTime, inactivityTime, device.getWorkingTime() / (currentTime - startOfWorkingTime));
            }
        }
    }

    public static double getAlpha() {
        return alpha;
    }

    public static double getBeta() {
        return beta;
    }

    public static double getLambda() {
        return lambda;
    }

    public static double getWorkingTime() {
        return workingTime;
    }

    public static void setWorkingTime(double workingTime) {
        Controller.workingTime = workingTime;
    }

    public static double getUniFormDistrib(double a, double b) {
        return Math.random() * (b-a) + a;
    }

    public static double getExponentialDistrib(double lambda) {
        return -(1 / lambda) * Math.log(1 - Math.random());
    }
    public static double getGenerationTime() {
        return getWorkingTime() + getUniFormDistrib(getAlpha(), getBeta());
    }

    public static double getReleaseTime() {
        return getWorkingTime() + getExponentialDistrib(getLambda());
    }

}
