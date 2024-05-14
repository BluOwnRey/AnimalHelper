package com.example.animalhelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class PatientController {

    @FXML
    private VBox VBoxDataPerson;

    @FXML
    private Button btnMenu;

    @FXML
    private AnchorPane menu;

    @FXML
    private AnchorPane menu_bac;

    @FXML
    private Button menu_btn_pat;

    @FXML
    private Button menu_btn_reg;

    @FXML
    private Label notification;

    @FXML
    private ComboBox<?> spinnerSort;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void initialize() throws SQLException {
        menu.setVisible(false);
        menu_bac.setVisible(false);

    }
    @FXML
    private void handleBtnMenu() {
        menu.setVisible(true);
        menu_bac.setVisible(true);
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBacMenu() {
        menu.setVisible(false);
        menu_bac.setVisible(false);
    }

    public void handleBacRegMenuClick() {
        loadScene("main-view.fxml");
    }

    public void handleBacPatMenuClick() {
        loadScene("patient-view.fxml");
    }
    @FXML
    private void handleBacPatMenuEntered() {
        menu_btn_pat.setStyle("-fx-background-color: #70baac;");
    }
    @FXML
    private void handleBacPatMenuExited() {
        menu_btn_pat.setStyle("-fx-background-color:  #FFFFFF00;");
    }
    @FXML
    private void handleBacRegMenuEntered() {
        menu_btn_reg.setStyle("-fx-background-color: #70baac;");
    }
    @FXML
    private void handleBacRegMenuExited() {
        menu_btn_reg.setStyle("-fx-background-color:  #FFFFFF00;");
    }
}
