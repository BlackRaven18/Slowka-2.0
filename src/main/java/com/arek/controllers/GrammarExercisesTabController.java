package com.arek.controllers;

import com.arek.database_utils.DatabaseQueryManager;
import com.arek.database_utils.GrammarExercise;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


class TaskEntry{
    private HBox entryContainer;
    private TextField answerField;
    private Label correctAnswerLabel;

    private GrammarExercise exerciseData;


    public TaskEntry(String id, GrammarExercise exerciseData){
        this.exerciseData = exerciseData;
        this.answerField = new TextField();
        this.correctAnswerLabel = new Label(exerciseData.getHint());
                // StringBuilder().append("").append(correctAnswer).append("").toString());
        this.correctAnswerLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 14));

        entryContainer = createEntryContainer(id, exerciseData.getFirstSentence(),
                exerciseData.getSecondSentence());
    }

    private HBox createEntryContainer(String id, String firstSentence,
                                     String secondSentence){

        HBox exampleContainer = new HBox();
        exampleContainer.setSpacing(10);
        exampleContainer.setAlignment(Pos.CENTER_LEFT);

        Label exampleId = new Label(id);
        Label firstPart = new Label(firstSentence);
        Label secondPart = new Label(secondSentence);

        exampleContainer.getChildren().add(exampleId);
        exampleContainer.getChildren().add(firstPart);
        exampleContainer.getChildren().add(answerField);
        exampleContainer.getChildren().add(secondPart);
        exampleContainer.getChildren().add(correctAnswerLabel);

        return exampleContainer;
    }

    private boolean isAnswerCorrect(){
        return answerField.getText().toLowerCase().trim().equals(
                exerciseData.getCorrectAnswer().toLowerCase().trim());
    }

    public boolean checkAnswer(){
        if(isAnswerCorrect()){
            answerField.setStyle("-fx-background-color: rgba(0,255,0,0.5);");
            return true;
        } else{
            answerField.setStyle("-fx-background-color: rgba(255,0,0,0.5);");
            return false;
        }
    }


    public TextField getAnswerField() {
        return answerField;
    }

    public void setAnswerField(TextField answerField) {
        this.answerField = answerField;
    }

    public Label getCorrectAnswerLabel() {
        return correctAnswerLabel;
    }

    public void setCorrectAnswerLabel(Label correctAnswer) {
        this.correctAnswerLabel = correctAnswer;
    }

    public HBox getEntryContainer() {
        return entryContainer;
    }
}

public class GrammarExercisesTabController implements Initializable {

    @FXML GridPane examplesPane;
    @FXML Label messageLabel;

    private static final int NUMBER_OF_EXAMPLES = 5;

    private ArrayList<TaskEntry> taskEntryList;
    private ArrayList<GrammarExercise> exercisesList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        examplesPane.getChildren().clear();
        taskEntryList = new ArrayList<>();
        exercisesList = new ArrayList<>();
        messageLabel.setText("");

        fillExercisesList();
        fillExamplesPane();
    }

    private void fillExercisesList(){
        exercisesList = DatabaseQueryManager.getGrammarExercises();
    }


    private void fillExamplesPane(){

        int i = 0;
        Random randomGenerator = new Random();

        while(i < NUMBER_OF_EXAMPLES){
            if(!exercisesList.isEmpty()){
                int randomNumber = randomGenerator.nextInt(exercisesList.size() - 1);
                TaskEntry entry = new TaskEntry(String.valueOf(i + 1), exercisesList.get(randomNumber));
                taskEntryList.add(entry);
                examplesPane.add(entry.getEntryContainer(), 0, i);
                exercisesList.remove(randomNumber);
            }else{
                fillExercisesList();
                --i;
            }
            i++;
        }
        /*for(int i = 0; i < NUMBER_OF_EXAMPLES; i++){
            if(!exercisesList.isEmpty()){
                TaskEntry entry = new TaskEntry(String.valueOf(i + 1), exercisesList.get(i));
                taskEntryList.add(entry);
                examplesPane.add(entry.getEntryContainer(), 0, i);
            }else{
                fillExercisesList();
            }

        }*/
    }



    @FXML
    public void checkAnswers(){
        int wrongAnswersCounter = 0;

        for(TaskEntry task : taskEntryList){
            if(!task.checkAnswer()){
                wrongAnswersCounter++;
            }
        }

        if(wrongAnswersCounter > 0){
            setMessageLabel(Color.RED, "Znaleziono błędy");
        } else{
            setMessageLabel(Color.GREEN, "Wszystkie odpowiedzi są poprawne!");
        }
    }

    @FXML
    public void handleKeyPressedEvents(KeyEvent event){

        if(event.getCode().equals(KeyCode.ENTER)){
            checkAnswers();
        }

    }

    private void setMessageLabel(Color color, String text){
        messageLabel.setTextFill(color);
        messageLabel.setText(text);
    }
}
