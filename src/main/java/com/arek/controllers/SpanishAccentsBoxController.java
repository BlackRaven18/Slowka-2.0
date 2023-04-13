package com.arek.controllers;

import com.arek.language_learning_app.AppOptions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SpanishAccentsBoxController implements Initializable {

    private TextField focusedTextField;
    private AppOptions options;

    @FXML private Button firstAccentButton, secondAccentButton, thirdAccentButton, fourthAccentButton,
            fifthAccentButton, sixthAccentButton, seventhAccentButton, eighthAccentButton, ninthAccentButton;
    @FXML private CheckBox upperCaseAccentsCheckBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        options = AppOptions.getInstance();
        focusedTextField = options.getLastFocusedTextField();

    }


    private void insertAccent(String accent){
        //get last focused text field
        focusedTextField = options.getLastFocusedTextField();
        if(focusedTextField == null){
            return;
        }

        int caretPosition = options.getLastFocusedTextFieldCaretPosition();
        StringBuilder builder = new StringBuilder(focusedTextField.getText());
        builder.insert(caretPosition, accent);
        focusedTextField.setText(builder.toString());

        //request focus on TextField and set caret position to next position after new sign added
        focusedTextField.requestFocus();
        focusedTextField.positionCaret(caretPosition + 1);
    }

    @FXML
    public void setUpperOrLowerAccents(){
        if(upperCaseAccentsCheckBox.isSelected()){
            firstAccentButton.setText("Á");
            secondAccentButton.setText("É");
            thirdAccentButton.setText("Í");
            fourthAccentButton.setText("Ó");
            fifthAccentButton.setText("Ú");
            sixthAccentButton.setText("Ü");
            seventhAccentButton.setText("Ñ");
        } else{
            firstAccentButton.setText("á");
            secondAccentButton.setText("é");
            thirdAccentButton.setText("í");
            fourthAccentButton.setText("ó");
            fifthAccentButton.setText("ú");
            sixthAccentButton.setText("ü");
            seventhAccentButton.setText("ñ");
        }
    }

    @FXML public void insertFirstAccent(){ insertAccent(firstAccentButton.getText());}
    @FXML public void insertSecondAccent(){ insertAccent(secondAccentButton.getText());}
    @FXML public void insertThirdAccent(){ insertAccent(thirdAccentButton.getText());}
    @FXML public void insertFourthAccent(){ insertAccent(fourthAccentButton.getText());}
    @FXML public void insertFifthAccent(){ insertAccent(fifthAccentButton.getText());}
    @FXML public void insertSixthAccent(){ insertAccent(sixthAccentButton.getText());}
    @FXML public void insertSeventhAccent(){ insertAccent(seventhAccentButton.getText());}
    @FXML public void insertEighthAccent(){ insertAccent(eighthAccentButton.getText());}
    @FXML public void insertNinthAccent(){ insertAccent(ninthAccentButton.getText());}

    public TextField getFocusedTextField() {
        return focusedTextField;
    }

    public void setFocusedTextField(TextField focusedTextField) {
        this.focusedTextField = focusedTextField;
    }

    public void setVisible(boolean visible){
            firstAccentButton.setVisible(visible);
            secondAccentButton.setVisible(visible);
            thirdAccentButton.setVisible(visible);
            fourthAccentButton.setVisible(visible);
            fifthAccentButton.setVisible(visible);
            sixthAccentButton.setVisible(visible);
            seventhAccentButton.setVisible(visible);
            eighthAccentButton.setVisible(visible);
            ninthAccentButton.setVisible(visible);
            upperCaseAccentsCheckBox.setVisible(visible);

    }
}
