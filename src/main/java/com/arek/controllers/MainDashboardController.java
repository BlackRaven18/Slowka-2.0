package com.arek.controllers;

import com.arek.language_learning_app.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainDashboardController implements Initializable {


    // ****************************************************
    // Translation Tab variables
    // ****************************************************
    private WordAndTranslationsManager wordAndTranslationsManager;
    private ClockManager clockManager;

    @FXML
    private AddNewWordsController addNewWordsController;

    @FXML private Label wordLabel, messageLabel, translationOrderLabel, clockLabel;
    @FXML private TextField translationField;
    @FXML private MenuButton selectLanguageMenu;
    @FXML private Button startClockButton, stopClockButton;



    // ****************************************************
    // Add New Words Tab variables
    // ****************************************************



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        wordAndTranslationsManager = new WordAndTranslationsManager(Languages.SPANISH, TranslationOrder.NORMAL, translationOrderLabel);
        loadWordLabel();

        messageLabel.setText("");

        clockManager = ClockManager.getInstance();
        clockManager.prepareClock(clockLabel, startClockButton, stopClockButton);
    }



    // ****************************************************
    // Translation Tab methods
    // ****************************************************

    private void loadWordLabel(){
        wordLabel.setText(wordAndTranslationsManager.getRandomWord());
    }

    private void updateSelectLanguageMenu(){
        switch (wordAndTranslationsManager.getSelectedLanguage()){
            case SPANISH:
                selectLanguageMenu.setText("Hiszpański");
                break;
            case ENGLISH:
                selectLanguageMenu.setText("Angielski");
                break;
        }
    }

    @FXML
    public void checkIfCorrect(){

        if(wordAndTranslationsManager.checkTranslation(wordLabel.getText(), translationField.getText())){
            messageLabel.setText("OK!!!");
            translationField.setText("");
            loadWordLabel();

            if(clockManager.isClockRunning()){
                startClock();
            }

        } else{
            messageLabel.setText("NIE OK!!!");
        }

        translationField.setText("");
    }

    @FXML
    public void changeTranslationOrder(){

        TranslationOrder translationOrder = wordAndTranslationsManager.getTranslationOrder();

        messageLabel.setText("");

        if(translationOrder == TranslationOrder.NORMAL){
            wordAndTranslationsManager.changeTranslationOrder(TranslationOrder.REVERSE);
            loadWordLabel();
        } else{
            wordAndTranslationsManager.changeTranslationOrder(TranslationOrder.NORMAL);
            loadWordLabel();
        }
    }

    @FXML
    public void startClock(){
        clockManager.startClock();
    }

    @FXML
    public void stopClock(){
        clockManager.stopClock();
    }

    @FXML
    public void ifEnterPressedThenCheckIfCorrect(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            checkIfCorrect();
        }
    }

    @FXML
    public void onAddWordsTabChange(){
        clockManager.stopClock();
        addNewWordsController.restartTab();

    }

    @FXML
    public void onTranslationTabChange(){
        if(wordAndTranslationsManager != null) {
            wordAndTranslationsManager.selectSpanishLanguage();
            wordAndTranslationsManager.changeTranslationOrder(TranslationOrder.NORMAL);
            updateSelectLanguageMenu();
            loadWordLabel();
        }
    }

    @FXML
    public void onMergeDatabasesTabChange(){
        onAddWordsTabChange();
        onTranslationTabChange();
    }

    @FXML
    public void selectSpanishLanguage(){
        selectLanguageMenu.setText("Hiszpański");
        wordAndTranslationsManager.selectSpanishLanguage();
        loadWordLabel();
    }

    @FXML
    public void selectEnglishLanguage(){
        selectLanguageMenu.setText("Angielski");
        wordAndTranslationsManager.selectEnglishLanguage();
        loadWordLabel();
    }


}
