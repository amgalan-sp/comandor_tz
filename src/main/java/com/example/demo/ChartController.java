package com.example.demo;

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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class ChartController implements Initializable {
    private final GoodRepository goodRepository;
    private final ChecksRepository checksRepository;

    ObservableList<Checklines> shoppingListCopy = FXCollections.observableArrayList();
    CopyOnWriteArrayList<Checklines> shoppingList = new CopyOnWriteArrayList<>();


    @FXML
    private TextField searchBar;

    @FXML
    private Good currentFood;

    @FXML
    private ListView<Good> products;
    @FXML
    private TableView<Checklines> shoppingCartTable;
    @FXML
    private TableColumn<Checklines, String> name;
    @FXML
    private TableColumn<Checklines, Integer> countOfCheck;
    @FXML
    private TableColumn<Checklines, Double> sum;

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
        products.getItems().addAll(findAll());
        products.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Good>() {

            @Override
            public void changed(ObservableValue<? extends Good> observableValue, Good s, Good t1) {
                Checks newCheck = createCheck();
                int lineNumber =1;
                int count =1;

                currentFood = products.getSelectionModel().getSelectedItem();
                if (currentFood!=null){
//                    try {
                    if(shoppingList.size()!=0) {
                        shoppingList.forEach(checklines -> {
                            if (checklines.getGood().equals(currentFood)) {
                                checklines.setCount(checklines.getCount() + 1);
                            } else {
                                shoppingList.add(new Checklines(newCheck, currentFood, lineNumber + 1, count, (count * currentFood.getPrice())));
                            }
                        });
                    } else {
                        shoppingList.add(new Checklines(newCheck, currentFood, lineNumber + 1, count, (count * currentFood.getPrice())));
                    }
                    name.setCellValueFactory(new PropertyValueFactory<Checklines, String>("good"));
                    countOfCheck.setCellValueFactory(new PropertyValueFactory<Checklines, Integer>("count"));
                    sum.setCellValueFactory(new PropertyValueFactory<Checklines, Double>("sum"));
                    System.out.println(shoppingList);
                    shoppingListCopy.addAll(shoppingList);
                    shoppingCartTable.setItems(shoppingListCopy);
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