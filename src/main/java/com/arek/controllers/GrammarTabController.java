package com.arek.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


public class GrammarTabController implements Initializable {

    @FXML private WebView browser;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        File siteFile = new File(".\\sites\\index.html");

        try{
            browser.getEngine().load(siteFile.toURI().toURL().toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }


    }
}
