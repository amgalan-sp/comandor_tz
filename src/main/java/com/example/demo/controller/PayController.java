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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class PayController implements Initializable {

    @FXML
    private TextField searchBarToPay;

    private BigDecimal totalSum;
    Checks checkToSave;
    boolean returnFromPayController;

    @FXML
    private Button buttonToCompare;
    BigDecimal customerSum;
    private Checks checkToPay;

    public Checks getCheck(){
        return checkToPay;
    }

    @FXML
    void compare(KeyEvent event) {
        customerSum  = new BigDecimal(searchBarToPay.getText()).setScale(2, RoundingMode.CEILING);
    }
    void setValueToPay(Checks checkToPay) {
        this.checkToPay = checkToPay;
        this.totalSum = checkToPay.getSum();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public void actionPay(ActionEvent actionEvent) {


        if(customerSum.compareTo(totalSum) ==0) {

            checkToSave = new Checks(checkToPay.getSum());
            DialogManager.showInfoDialog("Успех", "Оплата проведена");
            returnFromPayController=true;
        } else {
            DialogManager.showErrorDialog("Ошибка", "Оплата не проведена");
            returnFromPayController=false;
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }
}
