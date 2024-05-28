package com.example.animalhelper;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
//        DB.dropDB();

        String[][] matrix = DB.getAllRegistryData();
        Arrays.sort(matrix, Comparator.comparing(row -> LocalDate.parse(row[5])));
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
            } catch (Exception e) {
                System.out.println("ERROR: " + e);
            }
        }

        // Список элементов сортировки
        spinnerSort.getItems().addAll("Сначала старые", "Сначала новые");
        spinnerSort.setValue("Сначала старые");

        spinnerSort.setOnAction(event -> {
            String selectedItem = spinnerSort.getSelectionModel().getSelectedItem();
            try {
                ObservableList<Node> children = VBoxDataPerson.getChildren();
                children.remove(1, children.size());

                if (selectedItem.equals("Сначала старые")) {
                    Arrays.sort(matrix, Comparator.comparing(row -> LocalDate.parse(row[5])));

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
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                        }
                    }
                } else if (selectedItem.equals("Сначала новые")) {
                    Arrays.sort(matrix, Comparator.comparing(row -> LocalDate.parse(row[5]), Comparator.reverseOrder()));

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
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                        }
                    }
                }
            } catch (Exception e){
                System.out.println(e);
            }
        });
    }

    @FXML
    public void clicked_edit_record() {
        String name, phone, animalName, animalType;
        LocalDate date = editDate.getValue();
        name = editName.getText();
        phone = editPhone.getText();
        animalName = editAnimalName.getText();
        animalType = editAnimaltype.getText();

        if (!name.isEmpty() && !phone.isEmpty() && !animalName.isEmpty() && !animalType.isEmpty() && date != null){
            try {
                int personId = DB.addPerson(name, phone);
                if (personId == 0){
                    notification.setText("Ошибка сервера, попробуйте ещё раз");
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> notification.setText("")));
                    timeline.setCycleCount(1);
                    timeline.play();
                } else
                    {
                    int animalId = DB.addAnimal(personId, animalName, animalType);
                    int id_record = DB.addRegistry(1, animalId, date);

                        // Переход на новое окно card-view.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("card-view.fxml"));
                        Parent root = loader.load();
                        CardController controller = loader.getController();
                        controller.setIdRecord(id_record);

                        Stage stage = (Stage) btnEdit.getScene().getWindow(); // Получаем текущий Stage
                        stage.setScene(new Scene(root)); // Устанавливаем новую сцену
                        stage.show(); // Показываем новую сцену

                }
            } catch (Exception e){
                System.out.println(e);
            }
        } else {
            notification.setText("Все поля должны быть заполнены!");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> notification.setText("")));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }


    public void handleBacRegMenuClick() {
        menu.setVisible(false);
        menu_bac.setVisible(false);
    }

    public void handleBacPatMenuClick() throws IOException {
        // Переход на новое окно card-view.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnEdit.getScene().getWindow(); // Получаем текущий Stage
        stage.setScene(new Scene(root)); // Устанавливаем новую сцену
        stage.show(); // Показываем новую сцену
    }



    // Открытие и закрытие меню и анимация кнопок
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
