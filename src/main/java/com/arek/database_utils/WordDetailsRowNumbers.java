package com.arek.database_utils;

public class WordDetailsRowNumbers {
    int wordRowNumber;
    int translationRowNumber;

    public WordDetailsRowNumbers(){}

    public WordDetailsRowNumbers(int wordRowNumber, int translationRowNumber){
        this.wordRowNumber = wordRowNumber;
        this.translationRowNumber = translationRowNumber;
    }

    public int getWordRowNumber() {
        return wordRowNumber;
    }

    public void setWordRowNumber(int wordRowNumber) {
        this.wordRowNumber = wordRowNumber;
    }

    public int gettranslationRowNumber() {
        return translationRowNumber;
    }

    public void settranslationRowNumber(int translationRowNumber) {
        this.translationRowNumber = translationRowNumber;
    }
}
