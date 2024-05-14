package com.example.animalhelper;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    public Button btnMenu;
    public Button btnEdit;
    @FXML
    private VBox VBoxDataPerson;

    @FXML
    private TextField editAnimalName;

    @FXML
    private TextField editAnimaltype;

    @FXML
    private DatePicker editDate;

    @FXML
    private TextField editName;

    @FXML
    private TextField editPhone;

    @FXML
    private ComboBox<String> spinnerSort;

    @FXML
    private Label notification;

    @FXML
    private AnchorPane menu;

    @FXML
    private AnchorPane menu_bac;

    @FXML
    private Button menu_btn_pat;

    @FXML
    private Button menu_btn_reg;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void initialize() {
        menu.setVisible(false);
        menu_bac.setVisible(false);
//        DB.createTables();
        // Список элементов сортировки
        spinnerSort.getItems().addAll("Сначала старые", "Сначала новые");
        // Предустановленный элемент
        spinnerSort.setValue("Сначала старые");
    }

    @FXML
    private void handleBtnMenu() {
        menu.setVisible(true);
        menu_bac.setVisible(true);
    }

    @FXML
    private void handleBacMenu() {
        menu.setVisible(false);
        menu_bac.setVisible(false);
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
    private void handleBtnEdit() {
        if (!editName.getText().isEmpty() && !editPhone.getText().isEmpty() && !editAnimalName.getText().isEmpty()
                && !editAnimaltype.getText().isEmpty() && !editDate.getValue().toString().isEmpty()){
            try {
//                loadScene("card-view.fxml");
                // Добавляем нового пользователя в базу данных и получаем его id
//                int personId = DB.addPerson(editName.getText(), editPhone.getText());
//                int animalId = DB.addAnimal(personId, editAnimalName.getText(), editAnimaltype.getText());
//                DB.addRegistry(animalId, editDate.getValue().toString());


//                // Загружаем макет FXML
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-main-view.fxml   "));
//                Node customLayout = loader.load();
//
//                // Получаем контроллер загруженного макета
//                RecordVBoxMainController recordController = loader.getController();
//
//                // Передаем данные в контроллер кастомного макета
//                recordController.setPersonInfo(
//                        editName.getText(),
//                        editPhone.getText(),
//                        editAnimalName.getText(),
//                        editAnimaltype.getText(),
//                        editDate.getValue().toString()
//                );
//
//                // Добавляем загруженный макет в VBoxDataPerson
//                VBoxDataPerson.getChildren().add(customLayout);
            } catch (Exception e) {
                System.out.println("ERROR: "+e);
            }
        } else {
            notification.setText("Все поля должны быть заполнены!");
        }

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
