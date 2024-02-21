module com.example.apssmo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.accessibility;


    opens com.example.apssmo to javafx.fxml;
    exports com.example.apssmo;
}