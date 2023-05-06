package com.arek.controllers;

import com.arek.clock_utils.ClockManager;
import com.arek.language_learning_app.AppOptions;
import com.arek.language_learning_app.Languages;
import com.arek.language_learning_app.TranslationOrder;
import com.arek.language_learning_app.WordAndTranslationsManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TranslationTabController implements Initializable {

    private final String CORRECT_ANSWER_MESSAGE = "Tłumaczenie poprawne!";
    private final String WRONG_ANSWER_MESSAGE = "Tłumaczenie niepoprawne!";

    private WordAndTranslationsManager wordAndTranslationsManager;
    private Languages language;
    private TranslationOrder translationOrder;
    private ClockManager clockManager;

    private AppOptions options;

    @FXML private SpanishAccentsBoxController spanishAccentsBoxController;

    @FXML private Label wordLabel, messageLabel, translationOrderLabel, clockLabel;
    @FXML private Tooltip wordLabelTooltip;
    @FXML private TextField translationField;
    @FXML private MenuButton selectLanguageMenu;
    @FXML private Button startClockButton, stopClockButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        options = AppOptions.getInstance();
        language = Languages.SPANISH;
        translationOrder = TranslationOrder.NORMAL;
        wordAndTranslationsManager = new WordAndTranslationsManager(language, translationOrder);

        loadWordLabel();
        wordLabelTooltip.setShowDelay(Duration.seconds(0.2));
        updateTranslationOrderLabel();

        messageLabel.setText("");

        clockManager = ClockManager.getInstance();
        clockManager.prepareClock(clockLabel, startClockButton, stopClockButton);

        //set focus on translationField when app is running
        translationField.requestFocus();
        setTranslationFieldAsLastFocusedTextField();
    }

    public void resetTab(){
        clockManager.stopClock();

        if(wordAndTranslationsManager != null){
            wordAndTranslationsManager.selectSpanishLanguage();
            wordAndTranslationsManager.changeTranslationOrder(TranslationOrder.NORMAL);

            language = Languages.SPANISH;
            updateSelectLanguageMenu();

            translationOrder = TranslationOrder.NORMAL;
            updateTranslationOrderLabel();

            loadWordLabel();
        }
    }

    private void loadWordLabel(){
        wordLabel.setText(wordAndTranslationsManager.getRandomWord());
        loadWordLabelTooltip();
    }

    private void loadWordLabelTooltip(){
        wordLabelTooltip.setText(wordAndTranslationsManager.getWordTranslations(wordLabel.getText()));
    }

    private void updateSelectLanguageMenu(){
        switch (wordAndTranslationsManager.getSelectedLanguage()){
            case SPANISH:
                selectLanguageMenu.setText("Hiszpański");
                updateTranslationOrderLabel();
                break;
            case ENGLISH:
                selectLanguageMenu.setText("Angielski");
                updateTranslationOrderLabel();
                break;
        }
    }

    public void updateTranslationOrderLabel(){
        switch(language){
            case SPANISH:
                if(translationOrder == TranslationOrder.NORMAL) translationOrderLabel.setText("Hiszpańsko - Polski");
                else translationOrderLabel.setText("Polsko - Hiszpański");
                break;
            case ENGLISH:
                if(translationOrder == TranslationOrder.NORMAL) translationOrderLabel.setText("Angielsko - Polski");
                else translationOrderLabel.setText("Polsko - Angielski");
                break;
        }
    }

    @FXML
    public void checkIfCorrect(){

        if(translationField.getText().isEmpty()){
            setMessageLabel(Color.BLACK, "");
            return;
        }

        if(wordAndTranslationsManager.checkTranslation(wordLabel.getText(), translationField.getText())){
            setMessageLabel(Color.GREEN, CORRECT_ANSWER_MESSAGE);
            translationField.setText("");
            loadWordLabel();

            if(clockManager.isClockRunning()){
                startClock();
            }

        } else{
            setMessageLabel(Color.RED, WRONG_ANSWER_MESSAGE);
        }
    }

    @FXML
    public void changeTranslationOrder(){
        messageLabel.setText("");

        if(translationOrder == TranslationOrder.NORMAL){
            translationOrder = TranslationOrder.REVERSE;
        } else{
            translationOrder = TranslationOrder.NORMAL;
        }
        wordAndTranslationsManager.changeTranslationOrder(translationOrder);
        updateTranslationOrderLabel();
        loadWordLabel();
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
    public void selectSpanishLanguage(){
        selectLanguageMenu.setText("Hiszpański");
        language = Languages.SPANISH;
        wordAndTranslationsManager.selectSpanishLanguage();
        updateSelectLanguageMenu();
        loadWordLabel();
        spanishAccentsBoxController.setVisible(true);
    }

    @FXML
    public void selectEnglishLanguage(){
        selectLanguageMenu.setText("Angielski");
        language = Languages.ENGLISH;
        wordAndTranslationsManager.selectEnglishLanguage();
        updateSelectLanguageMenu();
        loadWordLabel();
        spanishAccentsBoxController.setVisible(false);
    }

    private void setMessageLabel(Color color, String text){
        messageLabel.setTextFill(color);
        messageLabel.setText(text);
    }

    @FXML
    public void setTranslationFieldAsLastFocusedTextField(){
        //set as last focused text field
        options.setLastFocusedTextField(translationField);

        //listen last caret position before focus lost
        translationField.focusedProperty().addListener((observable, oldValue, newValue) ->
                options.setLastFocusedTextFieldCaretPosition(translationField.getCaretPosition()));
    }

    public WordAndTranslationsManager getWordAndTranslationsManager() {
        return wordAndTranslationsManager;
    }
}
