package com.example.apssmo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Simulation {
    private int countOfRequests;
    private Controller controller;
    private Generator generator;
    private RegDispatcher regDispatcher;
    private SelectDispatcher selectDispatcher;
    private Buffer buffer;
    private DeviceRepository deviceRepository;
    private int buffSize;
    private int countOfSource;


    public Simulation(double alpha, double beta, double lambda, int countOfRequests, int countOfSource, int countOfDevices, int buffSize) {
        this.countOfRequests = countOfRequests;
        this.countOfSource = countOfSource;
        this.buffSize = buffSize;
        buffer = new Buffer(buffSize);
        deviceRepository = new DeviceRepository(countOfDevices);
        regDispatcher = new RegDispatcher(buffer);
        generator = new Generator(countOfSource, regDispatcher);
        selectDispatcher = new SelectDispatcher(buffer, deviceRepository);
     //   controller = new Controller(alpha, beta, lambda, generator, regDispatcher, selectDispatcher, countOfRequests);
    }

    public void startSimulation(int countOfRequests) {
       // controller.generateRequests(countOfRequests);
    }

}
