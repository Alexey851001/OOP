package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import javax.swing.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.lang.reflect.Field;


public class Controller {

    public static ObservableList<TableField> tableDataList = FXCollections.observableArrayList();
    public static TableField editTbFld;

    private String fxmlAddResourcePath = "AddWindow.fxml";
    private String fxmlSerResourcePath = "SerializeForm.fxml";
    private String sceneAddTitle = "Add new object";
    private String sceneEditTitle = "Edit object's fields";
    private String sceneSerializeTitle = "Serialize/Deserialize";
    private String classPath = "sample.";

    @FXML
    private TableView<TableField> tableObject;

    @FXML
    private TableColumn<TableField, Ship> objColumn;

    @FXML
    private TableColumn<TableField, Integer> hashCodeColumn;

    @FXML
    private void initialize() {
        objColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getShip()));
        hashCodeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHshCd()));
        tableObject.setItems(tableDataList);
    }

    public void addClick() throws Exception{
        editTbFld = null;
        Parent root = FXMLLoader.load(getClass().getResource(fxmlAddResourcePath));
        Stage primaryStage = new Stage();
        primaryStage.setTitle(sceneAddTitle);
        primaryStage.setScene(new Scene(root, 600, 700));
        primaryStage.show();
    }

    public void deleteClick() throws Exception{
        int selectIndex = tableObject.getSelectionModel().getSelectedIndex();
        for (int j = 0; j < tableDataList.size(); j++) {
            TableField tableField = tableDataList.get(j);
            Field[] fieldList = tableField.getShip().getClass().getFields();
            for (Field field : fieldList) {
                if (field.getGenericType().getTypeName().equals(classPath + "Boat")){
                    if(field.get(tableField.getShip()).equals(tableDataList.get(selectIndex).getShip())) {
                        field.set(tableField.getShip(), null);
                    }
                }
            }
        }
        tableObject.getItems().remove(selectIndex);
    }

    public void editClick() throws Exception{
        editTbFld = tableObject.getSelectionModel().getSelectedItem();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlAddResourcePath));
        Stage primaryStage = new Stage();
        primaryStage.setTitle(sceneEditTitle);
        primaryStage.setScene(new Scene(root, 600, 700));
        primaryStage.show();
    }

    public void SerializeForm() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlSerResourcePath));
        Stage primaryStage = new Stage();
        primaryStage.setTitle(sceneSerializeTitle);
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }

}
