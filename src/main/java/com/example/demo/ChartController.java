package com.example.demo;

import com.example.demo.model.Checks;
import com.example.demo.model.Good;
import com.example.demo.repository.ChecksRepository;
import com.example.demo.repository.GoodRepository;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Component
public class ChartController implements Initializable {
    private final GoodRepository goodRepository;
    private final ChecksRepository checksRepository;

//    ArrayList<Good> productList = new ArrayList<>(
//            Arrays.asList(
//                    new Good(1,"Яблоко", 12.5),
//                    new Good(2, "Груша", 13.3),
//                    new Good(3,"Банан", 10.5),
//                    new Good(4,"Виноград", 10.0),
//                    new Good(5,"Персики", 11.5)
//            ));
//    ArrayList<Checklines> checkLinesList = new ArrayList<>(
//            Arrays.asList(
//                    new Checklines()
//
//            ));

    ObservableList<Good> shoppingList = FXCollections.observableArrayList();


    @FXML
    private TextField searchBar;

    @FXML
    private Good currentFood;

    @FXML
    private ListView<Good> products;
    @FXML
    private TableView<Good> shoppingCartTable;
    @FXML
    private TableColumn<Good, String> name;
    @FXML
    private TableColumn<Good, Integer> count;
    @FXML
    private TableColumn<Good, Double> price;

    public ChartController(GoodRepository goodRepository, ChecksRepository checksRepository) {
        this.goodRepository = goodRepository;
        this.checksRepository = checksRepository;
    }

    @FXML
    void search(KeyEvent event) {
        products.getItems().clear();
        products.getItems().addAll(searchList(searchBar.getText(),findAll()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        productList = findAll().;
        products.getItems().addAll(findAll());

        products.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Good>() {

            @Override
            public void changed(ObservableValue<? extends Good> observableValue, Good s, Good t1) {

                currentFood = products.getSelectionModel().getSelectedItem();
                if (currentFood!=null){
                    if(shoppingList.contains(currentFood)) {
                        System.out.println("!!!");
                    }
                    shoppingList.add(currentFood);
                    name.setCellValueFactory(new PropertyValueFactory<Good, String>("name"));
                    count.setCellValueFactory(new PropertyValueFactory<Good, Integer>("id"));
                    price.setCellValueFactory(new PropertyValueFactory<Good, Double>("price"));
                    shoppingCartTable.setItems(shoppingList);

                }
            }
        } ) ;
    }
    private List<Good> searchList(String searchWords, List<Good> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.getName().toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }
    private List<Good> findAll(){
        return goodRepository.findAll();
    }
    private Checks createCheck(){
        return checksRepository.saveAndFlush(new Checks());
    }
}