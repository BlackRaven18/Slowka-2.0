package com.arek.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpTabController implements Initializable {

    @FXML private WebView browser;

    private WebEngine engine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.engine = browser.getEngine();

        File siteFile = new File("sites/help.html");
        engine.load(siteFile.toURI().toString());





    }
}
