package com.arek.controllers;

import com.arek.database_utils.DatabaseManager;
import com.arek.database_utils.WordAndTranslation;
import com.arek.language_learning_app.Languages;
import com.arek.language_learning_app.TranslationOrder;
import com.arek.language_learning_app.WordAndTranslationsManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddNewWordsController implements Initializable {


    @FXML private Label messageLabel;
    @FXML private MenuButton selectLanguageMenu;
    @FXML private TextField wordField, translationField;

    @FXML private TableView<WordAndTranslation> wordsAndTranslationsTable;
    @FXML private TableColumn<WordAndTranslation, String> wordsColumn;
    @FXML private TableColumn<WordAndTranslation, String> translationsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messageLabel.setText("");

        initiateWordsAndTranslationsTableView();
    }

    public void restartTab(){
        messageLabel.setText("");
        selectSpanishLanguage();
    }

    private void initiateWordsAndTranslationsTableView(){
        wordsAndTranslationsTable.getItems().clear();
        ArrayList<WordAndTranslation> wordAndTranslationList = DatabaseManager.getWordsAndTranslationsAsList(TranslationOrder.NORMAL);

        wordsColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        translationsColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));

        for(WordAndTranslation element : wordAndTranslationList){
            wordsAndTranslationsTable.getItems().add(element);
        }
    }

    @FXML
    public void selectSpanishLanguage(){
        selectLanguageMenu.setText("Hiszpański");
        DatabaseManager.changeToSpanish();
        initiateWordsAndTranslationsTableView();
    }

    @FXML
    public void selectEnglishLanguage(){
        selectLanguageMenu.setText("Angielski");
        DatabaseManager.changeToEnglish();
        initiateWordsAndTranslationsTableView();
    }
    @FXML
    private void selectWordTranslationTableRow(){
        WordAndTranslation selectedWordWithTranslation = getSelectedWordWithTranslation();

        if(selectedWordWithTranslation != null) {
            wordField.setText(selectedWordWithTranslation.getWord());
            translationField.setText(selectedWordWithTranslation.getTranslation());
        }
    }

    private WordAndTranslation getSelectedWordWithTranslation(){
        int index = wordsAndTranslationsTable.getSelectionModel().getSelectedIndex();

        if(index < 0){
            return null;
        }

        return new WordAndTranslation(wordsColumn.getCellData(index), translationsColumn.getCellData(index));
    }


    @FXML
    public void addNewWord(){
        DatabaseManager.addWordWithTranslation(new WordAndTranslation(wordField.getText(), translationField.getText()));
        initiateWordsAndTranslationsTableView();
        //wordsAndTranslationsTable.refresh();

        messageLabel.setText("Dodano nowe słówko");
        wordField.setText("");
        translationField.setText("");
    }

    @FXML
    public void deleteWord(){
        if(!wordField.getText().isEmpty() && !translationField.getText().isEmpty()){
            DatabaseManager.deleteWordWithTranslation(new WordAndTranslation(wordField.getText(), translationField.getText()));
            initiateWordsAndTranslationsTableView();

            messageLabel.setText("Usunięto słówko");
            wordField.setText("");
            translationField.setText("");
        }
    }

    @FXML
    public void changeWord(){
        if(!wordField.getText().isEmpty() && !translationField.getText().isEmpty()){
            WordAndTranslation oldWordAndTranslation = getSelectedWordWithTranslation();
            WordAndTranslation newWordAndTranslation = new WordAndTranslation(wordField.getText(), translationField.getText());

            DatabaseManager.changeWordWithTranslation(oldWordAndTranslation, newWordAndTranslation);
            initiateWordsAndTranslationsTableView();

            messageLabel.setText("Zmieniono słówko");
            wordField.setText("");
            translationField.setText("");
        }
    }

}