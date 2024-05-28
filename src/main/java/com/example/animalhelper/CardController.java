package com.example.animalhelper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

public class CardController {

    @FXML
    private TextField age;

    @FXML
    private TextField breed;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_save_treatment;

    @FXML
    private ComboBox<String> castration;

    @FXML
    private TextArea chronic_diseases;

    @FXML
    private DatePicker date_birth;

    @FXML
    private TextField diagnosis;

    @FXML
    private ComboBox<String> doctor;

    @FXML
    private ComboBox<String> gender;

    @FXML
    private TextArea method_treatment;

    @FXML
    private TextField name;

    @FXML
    private Label name2;

    @FXML
    private TextField person_name;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> status;

    @FXML
    private DatePicker treatment_start_date;

    @FXML
    private TextField type;

    @FXML
    private RadioButton vaccine_false;

    @FXML
    private TextField vaccine_name;

    @FXML
    private RadioButton vaccine_true;

    @FXML
    private TextField weight;

    private int id_record;
    private int id_animal;
    private ToggleGroup toggleGroup;


    public void setIdRecord(int id){
        this.id_record = id;
    }

    @FXML
    void initialize(){

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String[] matrix = DB.getRegistryDetails(id_record);
            assert matrix != null;
            id_animal = Integer.parseInt(matrix[0]);
            name.setText(matrix[1]);
            name2.setText(matrix[1]);
            type.setText(matrix[2]);
            person_name.setText(matrix[3]);
            phone.setText(matrix[4]);

            // Список полов
            gender.getItems().addAll("Мальчик", "Девочка");
            gender.setValue("Мальчик");

            // Варианты выбора кастрации
            castration.getItems().addAll("Да", "Нет");
            castration.setValue("Да");

            // Список докторов
            doctor.getItems().addAll("Лобанов М.А.", "Кисегач Н.В.", "Быков Р.В.");
            doctor.setValue("Быков Р.В.");

            // Список статусов лечения
            status.getItems().addAll("Болен, начало лечения", "Под наблюдением", "Здоров");
            status.setValue("Болен, начало лечения");

            toggleGroup = new ToggleGroup();
            vaccine_true.setToggleGroup(toggleGroup);
            vaccine_false.setToggleGroup(toggleGroup);
            vaccine_false.setSelected(true);

            try {
                matrix = DB.getAnimalInfo(id_animal);
                if (matrix != null){
                    breed.setText(matrix[0]);
                    gender.setValue(matrix[1]);
                    weight.setText(matrix[2]);
                    age.setText(matrix[3]);
                    castration.setValue(matrix[4]);
                    LocalDate dateBirth = LocalDate.parse(matrix[5]);
                    date_birth.setValue(dateBirth);
                    boolean vaccine = Boolean.parseBoolean(matrix[6]);
                    if (vaccine) {
                        vaccine_true.setSelected(true);
                    } else {
                        vaccine_false.setSelected(true);
                    }
                    vaccine_name.setText(matrix[7]);
                    chronic_diseases.setText(matrix[8]);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                matrix = DB.getTreatmentDetails(id_record);
                if (matrix != null){
                    diagnosis.setText(matrix[0]);
                    method_treatment.setText(matrix[1]);
                    doctor.setValue(matrix[2]);
                    LocalDate dateBirth = LocalDate.parse(matrix[3]);
                    treatment_start_date.setValue(dateBirth);
                    status.setValue(matrix[4]);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void btn_save_click() throws SQLException {
        String breeds, genders, weights, ages, castrations, date_births, vaccine_names, chronic_diseasess;
        boolean vaccines = false;
        breeds = breed.getText();
        genders = gender.getValue();
        weights = weight.getText();
        ages = age.getText();
        castrations = castration.getValue();
        try{
            date_births = date_birth.getValue().toString();
        } catch (Exception e){
            date_births = "";
        }
        vaccine_names = vaccine_name.getText();
        chronic_diseasess = chronic_diseases.getText();

        if (vaccine_true.isSelected()) {
            vaccines = true;
        } else if (vaccine_false.isSelected()) {
            vaccines = false;
        }

        DB.addOrUpdateAnimalInfo(id_animal, breeds, genders, weights, ages, castrations, date_births, vaccines, vaccine_names, chronic_diseasess);
    }

    public void btn_save_treatment_click() {
        String diagnosiss, method_treatments, doctors, treatment_start_dates, statuss;

        diagnosiss = diagnosis.getText();
        method_treatments = method_treatment.getText();
        doctors = doctor.getValue();
        try{
            treatment_start_dates = treatment_start_date.getValue().toString();
        } catch (Exception e){
            treatment_start_dates = "";
        }
        statuss = status.getValue();
        try {
            DB.addTreatment(id_record, diagnosiss, method_treatments, doctors, treatment_start_dates, statuss);

            // Переход на новое окно card-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btn_save.getScene().getWindow(); // Получаем текущий Stage
            stage.setScene(new Scene(root)); // Устанавливаем новую сцену
            stage.show(); // Показываем новую сцену
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
