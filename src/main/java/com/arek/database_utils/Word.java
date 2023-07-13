package com.arek.database_utils;

public class Word {
    private int wordID;
    private String word;
    private int typeID;
    private int categoryID;
    private String isUsed;

    public Word(int wordID, String word, int typeID, int categoryID, String isUsed) {
        this.wordID = wordID;
        this.word = word;
        this.typeID = typeID;
        this.categoryID = categoryID;
        this.isUsed = isUsed;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }
}
