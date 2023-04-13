package com.arek.database_utils;

public class WordDetails {

    String word;
    String translation;
    String type;
    String category;

    public WordDetails(String word, String translation, String type, String category) {
        this.word = word;
        this.translation = translation;
        this.type = type;
        this.category = category;
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
