package com.example.jafafx.controller;

import com.example.jafafx.ActionButtonTableCell;
import com.example.jafafx.init.Init;
import com.example.jafafx.model.Checklines;
import com.example.jafafx.model.Checks;
import com.example.jafafx.model.Good;
import com.example.jafafx.repository.ChecksRepository;
import com.example.jafafx.repository.GoodRepository;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
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
    private final Init init;

    ObservableList<Checklines> shoppingList = FXCollections.observableArrayList();
    ObservableList<Checks> checkList = FXCollections.observableArrayList();
    @FXML
    private TextField searchBar;

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
    private Button buttonToPay;
    @Value("classpath:/bankSecurePay.fxml")
    private Resource payResource;
    @FXML
    private Parent fxmlPay;
    private FXMLLoader fxmlLoader;
    private PayController payController;
    private Stage payStage;


    public ChartController(GoodRepository goodRepository, ChecksRepository checksRepository, Init init) {
        this.goodRepository = goodRepository;
        this.checksRepository = checksRepository;
        this.init = init;
    }
// Соответсвующая поисковой строке продуктов функция
    @FXML
    void search(KeyEvent event) {
        products.getItems().clear();
        products.getItems().addAll(searchList(searchBar.getText(),findAll()));
    }
// Создание нулевого чека
    public Checks newCheck = new Checks(BigDecimal.valueOf(0));

// добавление модального окна
    private void showDialog(Window parentWindow) {
        if(payStage==null) {
            payStage = new Stage();
            payStage.setTitle("Окно оплаты");
            payStage.setMinHeight(150);
            payStage.setMinWidth(300);
            payStage.setResizable(false);
            payStage.setScene(new Scene(fxmlPay));
            payStage.initModality(Modality.WINDOW_MODAL);
            payStage.initOwner(parentWindow);
        }
        payStage.showAndWait();
    }
    public void actionButtonPressed(ActionEvent actionEvent)   {
        Object source =  actionEvent.getSource();
        if(!(source instanceof Button))  {
            return;
        }
        Button clickedButton = (Button) source;
        Window parentWindow =  ((Node) actionEvent.getSource()).getScene().getWindow();
        payController.setValueToPay(newCheck);
        switch (clickedButton.getId()) {
            case "buttonToPay":
                showDialog(parentWindow);
                if(payController.returnFromPayController){
                    createCheck(payController.getCheck());
                    shoppingCartTable.getItems().clear();
                    totalCartTable.getItems().clear();
                }
                break;
            case "another":
                break;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//  Ввод списка товаров в из БД
        products.getItems().addAll(findAll());
        try{
            fxmlLoader = new FXMLLoader(payResource.getURL());
            fxmlPay = fxmlLoader.load();
            payController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//   Добавление нулевого чека для корректного добавления списка в корзине
        checkList.add(newCheck);
//   Единоразовое добавление кандидата - Нового чека на покупки
        totalCartTable.setItems(checkList);
        products.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                int lineNumber =1;
                int count =1;

                if (click.getClickCount()==2) {
                    currentFood = products.getSelectionModel().getSelectedItem();
// Поиск такой же позиции  в корзине и определение кол-ва и суммы
                    shoppingList.stream().filter(checklines ->
                            checklines.getGood().equals(currentFood)).forEach(
                            e -> {
                                e.setCount(e.getCount() + 1);
                                e.setSum(BigDecimal.valueOf(e.getCount()).multiply(e.getGood().getPrice()));
                            });
// При условии отсутствия данного товара в корзине добавляется в корзину
                    if (shoppingList.stream().noneMatch(checklines ->
                            checklines.getGood().equals(currentFood))) {
                        shoppingList.add(new Checklines(newCheck, currentFood, lineNumber + 1, count, (currentFood.getPrice().multiply(BigDecimal.valueOf(count)))));
                    }
                }
//  Определение ячеек
                name.setCellValueFactory(new PropertyValueFactory<Checklines, String>("good"));
                countOfCheckLine.setCellValueFactory(new PropertyValueFactory<Checklines, Integer>("count"));
                sum.setCellValueFactory(new PropertyValueFactory<Checklines, Double>("sum"));
// Удаление позиций
                delete.setCellFactory(ActionButtonTableCell.<Checklines>forTableColumn("Удалить", (Checklines p) -> {
                    shoppingCartTable.getItems().remove(p);
                    newCheck.setSum(shoppingCartTable.getItems().stream().map(Checklines::getSum).reduce(BigDecimal::add).orElse(BigDecimal.valueOf(0.0)));
                    totalCartTable.refresh();
                    shoppingCartTable.refresh();
                    return p;
                }));
// Добавление списка товаров в отображаемый список
                shoppingCartTable.setItems(shoppingList);
// Расчет итоговой суммы
                newCheck.setSum(shoppingCartTable.getItems().stream().map(Checklines::getSum).reduce(BigDecimal::add).orElse(BigDecimal.valueOf(0.0)));
//  Определение ячейки

                totalSum.setCellValueFactory(new PropertyValueFactory<Checks,Double>("sum"));
                totalCartTable.refresh();
                shoppingCartTable.refresh();
            }
        } ) ;
    }
// Поиск товаров по введенному в поисковой строке симоволов по совпадению начального ввода
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
//  функция записи чека в БД
    void createCheck(Checks check){
         checksRepository.saveAndFlush(check);
    }

}