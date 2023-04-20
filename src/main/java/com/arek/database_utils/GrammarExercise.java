package com.arek.database_utils;

public class GrammarExercise {

    private int exerciseId;
    private String firstSentence;
    private String secondSentence;
    private String hint;
    private String correctAnswer;

    public GrammarExercise() {}

    public GrammarExercise(int exerciseId, String firstSentence, String secondSentence, String hint, String correctAnswer) {
        this.exerciseId = exerciseId;
        this.firstSentence = firstSentence;
        this.secondSentence = secondSentence;
        this.hint = hint;
        this.correctAnswer = correctAnswer;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getFirstSentence() {
        return firstSentence;
    }

    public void setFirstSentence(String firstSentence) {
        this.firstSentence = firstSentence;
    }

    public String getSecondSentence() {
        return secondSentence;
    }

    public void setSecondSentence(String secondSentence) {
        this.secondSentence = secondSentence;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
