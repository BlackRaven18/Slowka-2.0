package com.arek.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class GrammarTabController implements Initializable {

    @FXML private WebView browser;

    private WebEngine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File siteFile = new File("sites/index.html");
        engine = browser.getEngine();

        engine.load(siteFile.toURI().toString());
    }
}
