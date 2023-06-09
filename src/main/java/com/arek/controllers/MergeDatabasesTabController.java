package com.arek.controllers;

import com.arek.database_utils.DatabaseBackupManager;
import com.arek.database_utils.DatabaseQueryManager;
import com.arek.database_utils.DatabaseMergeManager;
import com.arek.language_learning_app.Languages;
import com.arek.language_learning_app.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MergeDatabasesTabController implements Initializable {

    private FileChooser fileChooser;
    private File databaseFile;
    private Languages language;

    @FXML private MenuButton selectLanguageMenu;
    @FXML private Label fileLabel, messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileLabel.setText("");
        messageLabel.setText("");

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database Files", "*.db"));
        language = Languages.SPANISH;
    }

    public void resetTab(){
        selectSpanishLanguage();
    }

    @FXML
    public void addDatabaseFile(){
        databaseFile = fileChooser.showOpenDialog(Main.getMainStage());
        if(databaseFile != null){
            setMessageLabel(Color.BLACK, "");
            fileLabel.setText(databaseFile.getName());
        }
    }

    public void resetDatabaseFile(){
        databaseFile = null;
        fileLabel.setText("");
    }

    @FXML
    public void mergeDatabases(){

        if(databaseFile != null){
            DatabaseBackupManager.makeBackup(null, true);
            DatabaseMergeManager databaseMergeManager = new DatabaseMergeManager(language, databaseFile);

            databaseMergeManager.mergeDatabases();
            setMessageLabel(Color.GREEN, "Połączono bazy słówek!");
        } else{
            setMessageLabel(Color.RED, "Musisz wybrać bazę słowek, którą chcesz połączyć!");
        }
    }

    @FXML
    public void selectSpanishLanguage(){
        selectLanguageMenu.setText("Hiszpański");
        language = Languages.SPANISH;
        DatabaseQueryManager.changeToSpanish();
        resetDatabaseFile();
    }

    @FXML
    public void selectEnglishLanguage(){
        selectLanguageMenu.setText("Angielski");
        language = Languages.ENGLISH;
        DatabaseQueryManager.changeToEnglish();
        resetDatabaseFile();
    }

    private void setMessageLabel(Color color, String text){
        messageLabel.setTextFill(color);
        messageLabel.setText(text);
    }
}
