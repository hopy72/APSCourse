package com.example.apssmo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Controller simulation = new Controller(15, 50, 0.002, 100, 12, 7, 5);
        //simulation.stepByStepSimulation(20);
        simulation.autoModeSimulation(100);

    }

    public static void main(String[] args) {
        launch();
    }
}