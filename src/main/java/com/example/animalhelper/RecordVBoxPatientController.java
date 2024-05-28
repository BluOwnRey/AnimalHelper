package com.example.animalhelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RecordVBoxPatientController {

    @FXML
    private Label animalNameLabel;

    @FXML
    private Label animalTypeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label diagnosis;

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private AnchorPane panel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label status;


    public void setPersonInfo(String name, String phone, String animalName, String animalType, String id, String date, String diagnosiss, String statuss) {
        try {
            nameLabel.setText(name);
            phoneLabel.setText(phone);
            animalNameLabel.setText(animalName);
            animalTypeLabel.setText(animalType);
            idLabel.setText(id);
            dateLabel.setText(date);
            diagnosis.setText(diagnosiss);
            status.setText(statuss);
        } catch (Exception e) {
            System.out.println("ERROR: "+e);
        }
    }

    @FXML
    public void panelRecordClick() throws IOException {
        // Переход на новое окно card-view.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("card-view.fxml"));
        Parent root = loader.load();
        CardController controller = loader.getController();
        controller.setIdRecord(Integer.parseInt(idLabel.getText()));

        Stage stage = (Stage) animalNameLabel.getScene().getWindow(); // Получаем текущий Stage
        stage.setScene(new Scene(root)); // Устанавливаем новую сцену
        stage.show(); // Показываем новую сцену

    }

}
