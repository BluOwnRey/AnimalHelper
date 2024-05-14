package com.example.animalhelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    @FXML
    public Button btnMenu;
    @FXML
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
    void initialize() throws SQLException {
        menu.setVisible(false);
        menu_bac.setVisible(false);

//        DB.clearTables();
        DB.createTables();

        // Список элементов сортировки
        spinnerSort.getItems().addAll("Сначала старые", "Сначала новые");
        // Предустановленный элемент
        spinnerSort.setValue("Сначала старые");


        String[][] matrix = DB.getAllRegistryData();


        for (String[] row : matrix) {
            try {
                // Загружаем макет FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("record-vbox-main-view.fxml"));
                Node customLayout = loader.load();

                // Получаем контроллер загруженного макета
                RecordVBoxMainController recordController = loader.getController();

                // Передаем данные в контроллер кастомного макета
                recordController.setPersonInfo(row[0], row[1], row[2], row[3], row[4], row[5]);

                // Добавляем загруженный макет в VBoxDataPerson
                VBoxDataPerson.getChildren().add(customLayout);
            } catch (IOException e) {
                System.out.println("ERROR: " + e);
            }
        }
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
    private void handleBtnEdit() throws SQLException, IOException {
        String name, phone, animalName, animalType, date;
        name = editName.getText();
        phone = editPhone.getText();
        animalName = editAnimalName.getText();
        animalType = editAnimaltype.getText();
        date = editDate.getValue().toString();
        if (!name.isEmpty() && !phone.isEmpty() && !animalName.isEmpty() && !animalType.isEmpty() && !date.isEmpty()){
            int personId = DB.addPerson(name, phone);
            if (personId == 0){
                System.out.println("ERROR: not add person");
            } else if (personId == -1){
                System.out.println("занято");
            } else {
                int animalId = DB.addAnimal(personId, animalName, animalType);
                int id_record = DB.addRegistry(animalId, date);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("card-view.fxml"));
                Parent root = loader.load();
                CardController controller = loader.getController();
                controller.setIdRecord(id_record);

                Stage stage = (Stage) editName.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
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
