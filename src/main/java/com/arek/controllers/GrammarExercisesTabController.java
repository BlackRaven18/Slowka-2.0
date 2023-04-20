package com.arek.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


class GrammarTask{
    private TextField answerField;
    private Label correctAnswer;

    public GrammarTask(){
        answerField = new TextField();
        correctAnswer = new Label();
        correctAnswer.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
    }

    public GrammarTask(String correctAnswer){
        this.answerField = new TextField();
        this.correctAnswer = new Label(
                new StringBuilder().append("(").append(correctAnswer).append(")").toString());
        this.correctAnswer.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
    }

    public boolean isCorrect(){
        return answerField.getText().toLowerCase().trim().equals(
                correctAnswer.getText().toLowerCase().trim());
    }

    public TextField getAnswerField() {
        return answerField;
    }

    public void setAnswerField(TextField answerField) {
        this.answerField = answerField;
    }

    public Label getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Label correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}

public class GrammarExercisesTabController implements Initializable {

    @FXML GridPane examplesPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        examplesPane.getChildren().clear();

        GrammarTask firstTask = new GrammarTask("close");

        HBox exampleContainer = new HBox();
        exampleContainer.setSpacing(10);
        exampleContainer.setAlignment(Pos.CENTER_LEFT);

        Label counter = new Label("1.");
        Label firstPart = new Label("The shop");
        Label secondPart = new Label("often");

        exampleContainer.getChildren().add(counter);
        exampleContainer.getChildren().add(firstPart);
        exampleContainer.getChildren().add(firstTask.getAnswerField());
        exampleContainer.getChildren().add(secondPart);
        exampleContainer.getChildren().add(firstTask.getCorrectAnswer());

        examplesPane.add(exampleContainer, 0, 0);

    }
}
