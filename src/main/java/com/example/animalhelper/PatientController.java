package com.example.animalhelper;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import java.time.LocalDate;
import java.util.*;

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
    private ComboBox<String> spinnerSortDate;

    @FXML
    private ComboBox<String> spinnerSortStatus;
    @FXML
    void initialize() throws SQLException {
        menu.setVisible(false);
        menu_bac.setVisible(false);


        // Список элементов сортировки
        spinnerSortDate.getItems().addAll("Сначала старые", "Сначала новые");
        spinnerSortDate.setValue("Сначала старые");

        // Список элементов сортировки
        spinnerSortStatus.getItems().addAll("Болен, начало лечения", "Под наблюдением", "Здоров");
        spinnerSortStatus.setValue("Болен, начало лечения");


        String[][] matrix = DB.getAllPatientData();
        Arrays.sort(matrix, Comparator.comparing(row -> LocalDate.parse(row[5])));

        for (String[] row : matrix) {
            try {

                // Загружаем макет FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-patient-view.fxml"));
                Node customLayout = loader.load();

                // Получаем контроллер загруженного макета
                RecordVBoxPatientController recordController = loader.getController();

                // Передаем данные в контроллер кастомного макета
                recordController.setPersonInfo(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);

                // Добавляем загруженный макет в VBoxDataPerson
                VBoxDataPerson.getChildren().add(customLayout);
            } catch (Exception e) {
                System.out.println("ERROR: " + e);
            }
        }

        spinnerSortDate.setOnAction(event -> {
            String selectedItem = spinnerSortDate.getSelectionModel().getSelectedItem();
            try {
                ObservableList<Node> children = VBoxDataPerson.getChildren();
                children.remove(1, children.size());

                if (selectedItem.equals("Сначала старые")) {
                    Arrays.sort(matrix, Comparator.comparing(row -> LocalDate.parse(row[5])));

                    for (String[] row : matrix) {
                        try {
                            // Загружаем макет FXML
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-patient-view.fxml"));
                            Node customLayout = loader.load();

                            // Получаем контроллер загруженного макета
                            RecordVBoxPatientController recordController = loader.getController();

                            // Передаем данные в контроллер кастомного макета
                            recordController.setPersonInfo(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);

                            // Добавляем загруженный макет в VBoxDataPerson
                            VBoxDataPerson.getChildren().add(customLayout);
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                        }
                    }
                } else if (selectedItem.equals("Сначала новые")) {
                    Arrays.sort(matrix, Comparator.comparing(row -> LocalDate.parse(row[5]), Comparator.reverseOrder()));

                    for (String[] row : matrix) {
                        try {

                            // Загружаем макет FXML
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-patient-view.fxml"));
                            Node customLayout = loader.load();

                            // Получаем контроллер загруженного макета
                            RecordVBoxPatientController recordController = loader.getController();

                            // Передаем данные в контроллер кастомного макета
                            recordController.setPersonInfo(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);

                            // Добавляем загруженный макет в VBoxDataPerson
                            VBoxDataPerson.getChildren().add(customLayout);
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                        }
                    }
                }
            } catch (Exception e){
                System.out.println(e);
            }
        });

        spinnerSortStatus.setOnAction(event -> {
            String selectedItem = spinnerSortStatus.getSelectionModel().getSelectedItem();
            switch (selectedItem) {
                case "Болен, начало лечения" -> {
                    ObservableList<Node> children = VBoxDataPerson.getChildren();
                    children.remove(1, children.size());
                    for (String[] row : matrix) {
                        try {
                            if (Objects.equals(row[7], "Болен, начало лечения")) {
                                // Загружаем макет FXML
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-patient-view.fxml"));
                                Node customLayout = loader.load();

                                // Получаем контроллер загруженного макета
                                RecordVBoxPatientController recordController = loader.getController();

                                // Передаем данные в контроллер кастомного макета
                                recordController.setPersonInfo(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);

                                // Добавляем загруженный макет в VBoxDataPerson
                                VBoxDataPerson.getChildren().add(customLayout);
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                        }
                    }
                }
                case "Под наблюдением" -> {
                    ObservableList<Node> children = VBoxDataPerson.getChildren();
                    children.remove(1, children.size());
                    for (String[] row : matrix) {
                        try {
                            if (Objects.equals(row[7], "Под наблюдением")) {
                                // Загружаем макет FXML
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-patient-view.fxml"));
                                Node customLayout = loader.load();

                                // Получаем контроллер загруженного макета
                                RecordVBoxPatientController recordController = loader.getController();

                                // Передаем данные в контроллер кастомного макета
                                recordController.setPersonInfo(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);

                                // Добавляем загруженный макет в VBoxDataPerson
                                VBoxDataPerson.getChildren().add(customLayout);
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                        }
                    }
                }
                case "Здоров" -> {
                    ObservableList<Node> children = VBoxDataPerson.getChildren();
                    children.remove(1, children.size());
                    for (String[] row : matrix) {
                        try {
                            if (Objects.equals(row[7], "Здоров")) {
                                // Загружаем макет FXML
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-patient-view.fxml"));
                                Node customLayout = loader.load();

                                // Получаем контроллер загруженного макета
                                RecordVBoxPatientController recordController = loader.getController();

                                // Передаем данные в контроллер кастомного макета
                                recordController.setPersonInfo(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);

                                // Добавляем загруженный макет в VBoxDataPerson
                                VBoxDataPerson.getChildren().add(customLayout);
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                        }
                    }
                }
            }
        });
    }





    public void btn_menu() {
        menu.setVisible(true);
        menu_bac.setVisible(true);
    }
    public void handleBacMenu() {
        menu.setVisible(false);
        menu_bac.setVisible(false);
    }
    public void handleBacPatMenuClick() {
        menu.setVisible(false);
        menu_bac.setVisible(false);
    }
    public void handleBacPatMenuEntered() {
        menu_btn_pat.setStyle("-fx-background-color: #70baac;");
    }
    public void handleBacPatMenuExited() {
        menu_btn_pat.setStyle("-fx-background-color:  #FFFFFF00;");
    }
    public void handleBacRegMenuClick() throws IOException {
        // Переход на новое окно main-view.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnMenu.getScene().getWindow(); // Получаем текущий Stage
        stage.setScene(new Scene(root)); // Устанавливаем новую сцену
        stage.show(); // Показываем новую сцену
    }
    public void handleBacRegMenuEntered() {
        menu_btn_reg.setStyle("-fx-background-color: #70baac;");
    }
    public void handleBacRegMenuExited() {
        menu_btn_reg.setStyle("-fx-background-color:  #FFFFFF00;");
    }
}
