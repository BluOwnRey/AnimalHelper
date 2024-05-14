package com.example.animalhelper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    public void setPersonInfo(String name, String phone, String animalName, String animalType, String date) {
        try {
            nameLabel.setText(name);
            phoneLabel.setText(phone);
            animalNameLabel.setText(animalName);
            animalTypeLabel.setText(animalType);
            dateLabel.setText(date);
        } catch (Exception e) {
            System.out.println("ERROR: "+e);
        }

    }

}
