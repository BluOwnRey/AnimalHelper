package com.example.animalhelper;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CardController {

    @FXML
    private AnchorPane menu;

    @FXML
    private AnchorPane menu_bac;

    @FXML
    private Button menu_btn_pat;

    @FXML
    private Button menu_btn_reg;

    @FXML
    public Button btnMenu;

    private int id;


    public void setIdRecord(int id){
        this.id = id;
    }
    @FXML
    void initialize() throws SQLException {
        menu.setVisible(false);
        menu_bac.setVisible(false);

        System.out.println(id);
    }
}
