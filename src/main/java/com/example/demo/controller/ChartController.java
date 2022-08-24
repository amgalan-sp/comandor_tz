package com.example.demo.controller;

import com.example.demo.ActionButtonTableCell;
import com.example.demo.model.Checklines;
import com.example.demo.model.Checks;
import com.example.demo.model.Good;
import com.example.demo.repository.ChecksRepository;
import com.example.demo.repository.GoodRepository;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


@Component
public class ChartController implements Initializable  {
    private final GoodRepository goodRepository;
    private final ChecksRepository checksRepository;

    ObservableList<Checklines> shoppingList = FXCollections.observableArrayList();
    ObservableList<Checks> checkList = FXCollections.observableArrayList();
    @FXML
    private TextField searchBar;
    @FXML
    private Good currentFood;
    @FXML
    private ListView<Good> products;
    @FXML
    private TableView<Checklines> shoppingCartTable;
    @FXML
    private TableView<Checks> totalCartTable;
    @FXML
    private TableColumn<Checklines, String> name;
    @FXML
    private TableColumn<Checklines, Integer> countOfCheckLine;
    @FXML
    private TableColumn<Checklines, Double> sum;
    @FXML
    private TableColumn<Checklines,Button> delete;
    @FXML
    private TableColumn<Checks, Double> totalSum;

    @FXML
    private Button button;
    @Value("classpath:/bankSecurePay.fxml")
    private Resource chartResource;

    public ChartController(GoodRepository goodRepository, ChecksRepository checksRepository) {
        this.goodRepository = goodRepository;
        this.checksRepository = checksRepository;
    }

    @FXML
    void search(KeyEvent event) {
        products.getItems().clear();
        products.getItems().addAll(searchList(searchBar.getText(),findAll()));
    }
    Checks newCheck = new Checks(LocalDate.now(), LocalTime.now(),  0);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products.getItems().addAll(findAll());
        checkList.add(newCheck);
        totalCartTable.setItems(checkList);
        products.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Good>() {

            @Override
            public void changed(ObservableValue<? extends Good> observableValue, Good s, Good t1) {
                int lineNumber =1;
                int count =1;

                currentFood = products.getSelectionModel().getSelectedItem();
                if (currentFood!=null) {
                    if (shoppingList.size() == 0) {
                        shoppingList.add(new Checklines(newCheck, currentFood, lineNumber + 1, count, (count * currentFood.getPrice())));
                    } else {
                        shoppingList.stream().filter(checklines ->
                                checklines.getGood().equals(currentFood)).forEach(
                                e -> {
                                    e.setCount(e.getCount() + 1);
                                    e.setSum(e.getCount() * e.getGood().getPrice());
                                });
                        if (shoppingList.stream().noneMatch(checklines ->
                                checklines.getGood().equals(currentFood))) {
                            shoppingList.add(new Checklines(newCheck, currentFood, lineNumber + 1, count, (count * currentFood.getPrice())));
                        }
                    }
                }
                name.setCellValueFactory(new PropertyValueFactory<Checklines, String>("good"));
                countOfCheckLine.setCellValueFactory(new PropertyValueFactory<Checklines, Integer>("count"));
                sum.setCellValueFactory(new PropertyValueFactory<Checklines, Double>("sum"));
                delete.setCellFactory(ActionButtonTableCell.<Checklines>forTableColumn("Удалить", (Checklines p) -> {
                    shoppingCartTable.getItems().remove(p);
                    newCheck.setSum(shoppingCartTable.getItems().stream().map(Checklines::getSum).reduce(Double::sum).orElse(0.0));
                    totalCartTable.refresh();
                    shoppingCartTable.refresh();
                    return p;
                }));

                shoppingCartTable.setItems(shoppingList);
                newCheck.setSum(shoppingCartTable.getItems().stream().map(Checklines::getSum).reduce(Double::sum).orElse(0.0));
                totalSum.setCellValueFactory(new PropertyValueFactory<Checks,Double>("sum"));
                totalCartTable.refresh();
                shoppingCartTable.refresh();
                button.setOnAction(event -> {
                    button.getScene().getWindow().hide();

                    try {
                        FXMLLoader loader = new FXMLLoader(chartResource.getURL());
                        Parent parent =loader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.setTitle("Окно оплаты");
                        stage.showAndWait();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
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
    private Checks createCheck(Checks check){
        return checksRepository.saveAndFlush(check);
    }

}