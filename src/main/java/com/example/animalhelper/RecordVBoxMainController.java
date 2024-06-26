package com.example.animalhelper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;

public class RecordVBoxMainController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label animalNameLabel;

    @FXML
    private Label animalTypeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label idLabel;


    public void setPersonInfo(String name, String phone, String animalName, String animalType, String id, String date) {
        try {
            nameLabel.setText(name);
            phoneLabel.setText(phone);
            animalNameLabel.setText(animalName);
            animalTypeLabel.setText(animalType);
            idLabel.setText(id);
            dateLabel.setText(date);
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
