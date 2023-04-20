package com.arek.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
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
                new StringBuilder().append("").append(correctAnswer).append("").toString());
        this.correctAnswer.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
    }

    private boolean isAnswerCorrect(){
        return answerField.getText().toLowerCase().trim().equals(
                correctAnswer.getText().toLowerCase().trim());
    }

    public void checkAnswer(){
        if(isAnswerCorrect()){
            answerField.setStyle("-fx-background-color: rgba(0,255,0,0.5);");
        } else{
            answerField.setStyle("-fx-background-color: rgba(255,0,0,0.5);");
        }
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
    @FXML Label messageLabel;

    private static final int NUMBER_OF_EXAMPLES = 5;

    private ArrayList<GrammarTask> taskList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        examplesPane.getChildren().clear();
        taskList = new ArrayList<>();
        messageLabel.setText("");

        fillTaskList();
        fillExamplesPane();
    }

    private void fillTaskList(){
        taskList.add(new GrammarTask("open"));
        taskList.add(new GrammarTask("close"));
    }

    private void fillExamplesPane(){

        /*for(int i = 0; i < NUMBER_OF_EXAMPLES; i++){

        }*/

        HBox firstExample = getExampleContainer("1", "The shops in poland",
                "very early", taskList.get(0));

        HBox secondExample = getExampleContainer("2", "Shops in poland",
                "very late", taskList.get(1));

        examplesPane.add(firstExample, 0, 0);
        examplesPane.add(secondExample, 0, 1);
    }

    private HBox getExampleContainer(String id, String firstSentence,
                                     String secondSentence, GrammarTask grammarTask){

        HBox exampleContainer = new HBox();
        exampleContainer.setSpacing(10);
        exampleContainer.setAlignment(Pos.CENTER_LEFT);

        Label exampleId = new Label(id);
        Label firstPart = new Label(firstSentence);
        Label secondPart = new Label(secondSentence);

        exampleContainer.getChildren().add(exampleId);
        exampleContainer.getChildren().add(firstPart);
        exampleContainer.getChildren().add(grammarTask.getAnswerField());
        exampleContainer.getChildren().add(secondPart);
        exampleContainer.getChildren().add(grammarTask.getCorrectAnswer());

        return exampleContainer;
    }

    @FXML
    public void checkAnswers(){
        for(GrammarTask task : taskList){
            task.checkAnswer();
        }
    }

    private void setMessageLabel(Color color, String text){
        messageLabel.setTextFill(color);
        messageLabel.setText(text);
    }
}
