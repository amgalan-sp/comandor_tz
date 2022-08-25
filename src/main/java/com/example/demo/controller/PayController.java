package com.example.demo.controller;


import com.example.demo.DialogManager;
import com.example.demo.model.Checks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

@Component
public class PayController implements Initializable {

    @FXML
    private TextField searchBarToPay;

    private double totalSum;

    @FXML
    private Button buttonToCompare;
    double customerSum;
    private Checks checkToPay;

    public Checks getCheck(){
        return checkToPay;
    }

    @FXML
    void compare(KeyEvent event) {
        customerSum  = Double.parseDouble(searchBarToPay.getText());
    }
    void setValueToPay(Checks checkToPay) {
        this.checkToPay = checkToPay;
        this.totalSum = checkToPay.getSum();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public void actionPay(ActionEvent actionEvent) {
        if(customerSum==totalSum) {
            Checks checkToSave = new Checks(LocalDate.now(), LocalTime.now(), checkToPay.getSum());
            DialogManager.showInfoDialog("Успех", "Оплата проведена");
        } else {
            DialogManager.showErrorDialog("Ошибка", "Оплата не проведена");

        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }
}
