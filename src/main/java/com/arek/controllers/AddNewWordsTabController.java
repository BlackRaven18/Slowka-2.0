package com.arek.controllers;

import com.arek.database_utils.DatabaseQueryManager;
import com.arek.database_utils.DatabaseResponse;
import com.arek.database_utils.WordDetails;
import com.arek.language_learning_app.AppOptions;
import com.arek.language_learning_app.TranslationOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddNewWordsTabController implements Initializable {

   private AppOptions options;

    @FXML private Label messageLabel;
    @FXML private MenuButton selectLanguageMenu, typeButton, categoryButton;
    @FXML private TextField wordField, translationField, searchField;

    @FXML private TableView<WordDetails> wordsAndTranslationsTable;
    @FXML private TableColumn<WordDetails, String> wordsColumn;
    @FXML private TableColumn<WordDetails, String> translationsColumn;
    @FXML private TableColumn<WordDetails, String> typeColumn;
    @FXML private TableColumn<WordDetails, String> categoryColumn;


    ObservableList<WordDetails> wordDetailsObservableList;
    FilteredList<WordDetails> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messageLabel.setText("");

        initiateWordsAndTranslationsTableView();
        initializeTypeButton();
        initializeCategoryButton();
    }

    public void resetTab(){
        messageLabel.setText("");
        selectSpanishLanguage();
    }

    private void initiateWordsAndTranslationsTableView(){
        options = AppOptions.getInstance();

        wordsAndTranslationsTable.getItems().clear();
        ArrayList<WordDetails> wordDetailsList = DatabaseQueryManager.getWordDetailsAsList();

        wordDetailsObservableList = FXCollections.observableArrayList(wordDetailsList);

        wordsColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        translationsColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));


        //wordsAndTranslationsTable.setItems(wordDetailsObservableList);

        filteredData = new FilteredList<>(wordDetailsObservableList, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(wordAndTranslation -> {

                //if no search value then display all records
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(wordAndTranslation.getWord().toLowerCase().contains(searchKeyword.trim())){
                    System.out.println(wordAndTranslation.getWord().toLowerCase());
                    return true;// means we found a match in word name
                } else if(wordAndTranslation.getTranslation().toLowerCase().contains(searchKeyword.trim())){
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<WordDetails> sortedData = new SortedList<>(filteredData);

        //Bind sorted result with Table View
        sortedData.comparatorProperty().bind(wordsAndTranslationsTable.comparatorProperty());

        //apply filtered and sorted data to the Table View
        ObservableList<WordDetails> tmpObservableList = FXCollections.observableArrayList(sortedData);
        wordsAndTranslationsTable.setItems(tmpObservableList);
        System.out.println(tmpObservableList.size());

        //wordsAndTranslationsTable.setItems(sortedData);

        wordsAndTranslationsTable.refresh();
    }

    private void initializeTypeButton(){
        typeButton.setText("Rzeczownik");

        typeButton.getItems().add(createMenuButtonItem("Rzeczownik", typeButton));
        typeButton.getItems().add(createMenuButtonItem("Czasownik", typeButton));
        typeButton.getItems().add(createMenuButtonItem("Przymiotnik", typeButton));
    }

    private void initializeCategoryButton(){
        categoryButton.setText("Natura");

        categoryButton.getItems().add(createMenuButtonItem("Natura", categoryButton));
        categoryButton.getItems().add(createMenuButtonItem("Zwierzęta", categoryButton));
        categoryButton.getItems().add(createMenuButtonItem("Jedzenie", categoryButton));
    }

    private MenuItem createMenuButtonItem(String text, MenuButton parent){
        MenuItem item = new MenuItem(text);
        item.setOnAction(event -> parent.setText(item.getText()));

        return item;
    }

    @FXML
    public void updateTableWithSearchResult(){
        wordsAndTranslationsTable.setItems(FXCollections.observableArrayList(filteredData));
    }

    @FXML
    public void selectSpanishLanguage(){
        selectLanguageMenu.setText("Hiszpański");
        DatabaseQueryManager.changeToSpanish();
        initiateWordsAndTranslationsTableView();
    }

    @FXML
    public void selectEnglishLanguage(){
        selectLanguageMenu.setText("Angielski");
        DatabaseQueryManager.changeToEnglish();
        initiateWordsAndTranslationsTableView();
    }
    @FXML
    private void selectWordTranslationTableRow(){
        WordDetails selectedWordWithTranslation = getSelectedWordWithTranslation();

        if(selectedWordWithTranslation != null) {
            wordField.setText(selectedWordWithTranslation.getWord());
            translationField.setText(selectedWordWithTranslation.getTranslation());
            typeButton.setText(selectedWordWithTranslation.getType());
            categoryButton.setText(selectedWordWithTranslation.getCategory());
        }
    }

    private WordDetails getSelectedWordWithTranslation(){
        int index = wordsAndTranslationsTable.getSelectionModel().getSelectedIndex();

        if(index < 0){
            return null;
        }

        return new WordDetails(wordsColumn.getCellData(index), translationsColumn.getCellData(index),
                    typeColumn.getCellData(index), categoryColumn.getCellData(index));
    }


    @FXML
    public void addNewWord(){
        trimTextFieldsContent();

        if(!wordField.getText().isEmpty() && !translationField.getText().isEmpty()) {
            if(DatabaseQueryManager.addWordDetails(
                    new WordDetails(wordField.getText(), translationField.getText(), typeButton.getText(), categoryButton.getText()))
                == DatabaseResponse.DB_ALREADY_IN){
                setMessageLabel(Color.BLACK, "Słówko z tym tłumaczeniem jest już w bazie");
                return;
            }
            initiateWordsAndTranslationsTableView();

            setMessageLabel(Color.GREEN, "Dodano nowe słówko");
            wordField.setText("");
            translationField.setText("");

            wordField.requestFocus();
            setWordFieldAsLastFocusedTextField();

        }else {
            setMessageLabel(Color.BLACK, "Pola nie mogą być puste!");
        }
    }

    @FXML
    public void deleteWord(){
        trimTextFieldsContent();

        if(!wordField.getText().isEmpty() && !translationField.getText().isEmpty()){
            if(DatabaseQueryManager.deleteWordDetails(
                    new WordDetails(wordField.getText(), translationField.getText(), typeButton.getText(), categoryButton.getText()))
                    == DatabaseResponse.DB_NOT_FOUND){
                setMessageLabel(Color.RED, "Nie znaleziono takiego słówka lub tłumaczenia!");
                return;
            }
            initiateWordsAndTranslationsTableView();

            setMessageLabel(Color.RED, "Usunięto słówko!");
            wordField.setText("");
            translationField.setText("");
        }else {
            setMessageLabel(Color.BLACK, "Pola nie mogą być puste!");
        }
    }

    @FXML
    public void changeWord(){
        trimTextFieldsContent();

        if(!wordField.getText().isEmpty() && !translationField.getText().isEmpty()){
            WordDetails oldWordAndTranslation = getSelectedWordWithTranslation();
            if(oldWordAndTranslation == null){
                setMessageLabel(Color.RED, "Najpierw wybierz słówko z listy!");
                return;
            }

            WordDetails newWordAndTranslation = new WordDetails(
                    wordField.getText(), translationField.getText()
                    , typeButton.getText(), categoryButton.getText());

            if(DatabaseQueryManager.changeWordDetails(oldWordAndTranslation, newWordAndTranslation)
                    == DatabaseResponse.DB_NOT_FOUND){
                setMessageLabel(Color.RED, "Nie znaleziono takiego słówka!");
                return;
            }

            initiateWordsAndTranslationsTableView();

            setMessageLabel(Color.DARKORANGE, "Zmieniono słówko!");
            wordField.setText("");
            translationField.setText("");
        }else {
            setMessageLabel(Color.BLACK, "Pola nie mogą być puste!");        }
    }

    private void trimTextFieldsContent(){
        wordField.setText(wordField.getText().trim());
        translationField.setText(translationField.getText().trim());
    }

    @FXML
    public void handleKeyEvents(KeyEvent event){
        //add new word when enter is pressed
        if(event.getCode().equals(KeyCode.ENTER)){
            wordField.requestFocus();
            addNewWord();
            return;
        }

        // select last focused text field
        if(event.getCode().equals(KeyCode.TAB)){
            if(wordField.isFocused()){
                setWordFieldAsLastFocusedTextField();
                return;
            }

            if(translationField.isFocused()){
                setTranslationFieldAsLastFocusedTextField();
                return;
            }
        }
    }

    private void setMessageLabel(Color color, String text){
        messageLabel.setTextFill(color);
        messageLabel.setText(text);
    }

    @FXML
    public void setWordFieldAsLastFocusedTextField(){
        //set as last focused text field
        options.setLastFocusedTextField(wordField);

        //listen last caret position before focus lost
        wordField.focusedProperty().addListener((observable, oldValue, newValue) ->
                options.setLastFocusedTextFieldCaretPosition(wordField.getCaretPosition()));
    }

    @FXML
    public void setTranslationFieldAsLastFocusedTextField(){
        //set as last focused text field
        options.setLastFocusedTextField(translationField);

        //listen last caret position before focus lost
        translationField.focusedProperty().addListener((observable, oldValue, newValue) ->
                options.setLastFocusedTextFieldCaretPosition(translationField.getCaretPosition()));
    }
}
