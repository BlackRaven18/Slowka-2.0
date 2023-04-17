
module com.arek{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.bootstrapfx.core;
    requires FXTrayIcon;
    requires java.sql;
    requires java.desktop;


    exports com.arek.language_learning_app;
    opens com.arek.language_learning_app to javafx.fxml;
    exports com.arek.controllers;
    opens com.arek.controllers to javafx.fxml;
    exports com.arek.database_utils;
    opens com.arek.database_utils to javafx.fxml;
    exports com.arek.clock_utils;
    opens com.arek.clock_utils to javafx.fxml;

}