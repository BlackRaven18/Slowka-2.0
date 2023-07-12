package com.arek.database_utils;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;



public class WordDetails {

    CheckBox isUsedCheckbox;
    String word;
    String translation;
    String type;
    String category;

    public WordDetails(boolean isUsed, String word, String translation, String type, String category) {
        this.isUsedCheckbox = new CheckBox();
        this.isUsedCheckbox.setSelected(isUsed);
        this.word = word;
        this.translation = translation;
        this.type = type;
        this.category = category;

        this.isUsedCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("bebe");
        });
    }


    public CheckBox getIsUsedCheckbox() {
        return isUsedCheckbox;
    }

    public void setIsUsedCheckbox(CheckBox isUsedCheckbox) {
        this.isUsedCheckbox = isUsedCheckbox;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }
}
