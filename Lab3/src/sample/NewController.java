package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Field;

public class NewController {

    private Field[] fieldList;
    private TextField[] textFieldsArr;
    private ComboBox tempCB;
    private String classPath = "sample.";

    @FXML
    private ComboBox<String> classNameCB;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button applyBtn;

    @FXML
    private void initialize() {
        ObservableList<String> classList = FXCollections.observableArrayList("Ship","Submarine","AtomicSubmarine", "MilitarySubmarine","SurfaceShip","Flattop","Sailboat","Boat");
        if (Controller.editTbFld == null){
            classNameCB.setValue("Ship");
            changeCB();
        }
        else {
            String str = Controller.editTbFld.getShip().getClass().toString();
            str = str.replace("class " + classPath, "");
            classNameCB.setValue(str);
            try {
                fillFields();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        classNameCB.setItems(classList);
    }

    public void changeCB(){
        if (Controller.editTbFld == null) {
            gridPane.getChildren().clear();
            fieldList = getFields(classNameCB.getValue());
            if (fieldList != null) {
                int i = 0;
                textFieldsArr = new TextField[fieldList.length];
                for (Field field : fieldList) {
                    Text tempText = new Text();
                    tempText.setText(field.getName());
                    gridPane.add(tempText, 0, i + 1);
                    if (!field.getGenericType().getTypeName().equals(classPath + "Boat")) {
                        TextField tempTextField = new TextField();
                        textFieldsArr[i] = tempTextField;
                        gridPane.add(tempTextField, 1, i + 1);
                        tempTextField.setText(field.getType().toString());
                    } else {
                        tempCB = new ComboBox();
                        tempCB.setValue("");
                        ObservableList<Ship> agregationList = FXCollections.observableArrayList();
                        agregationList.add(null);
                        for (int j = 0; j < Controller.tableDataList.size(); j++) {
                            TableField tableField = Controller.tableDataList.get(j);
                            if (tableField.getShip().getClass().toString().equals("class " + classPath + "Boat")){
                                agregationList.add(tableField.getShip());
                            }
                        }
                        tempCB.setItems(agregationList);
                        gridPane.add(tempCB,1, i + 1);
                    }
                    i++;
                }
            }
        }
    }

    private void fillFields() throws IllegalAccessException {
        gridPane.getChildren().clear();
        fieldList = Controller.editTbFld.getShip().getClass().getFields();
        if (fieldList != null) {
            int i = 0;
            textFieldsArr = new TextField[fieldList.length];
            for (Field field : fieldList) {
                Text tempText = new Text();
                tempText.setText(field.getName());
                gridPane.add(tempText, 0, i + 1);
                if (!field.getGenericType().getTypeName().equals(classPath + "Boat")) {
                    TextField tempTextField = new TextField();
                    textFieldsArr[i] = tempTextField;
                    gridPane.add(tempTextField, 1, i + 1);
                    tempTextField.setText(field.get(Controller.editTbFld.getShip()).toString());
                } else {
                    tempCB = new ComboBox();
                    tempCB.setValue(field.get(Controller.editTbFld.getShip()));
                    ObservableList<Ship> agregationList = FXCollections.observableArrayList();
                    agregationList.add(null);
                    for (int j = 0; j < Controller.tableDataList.size(); j++) {
                        TableField tableField = Controller.tableDataList.get(j);
                        if (tableField.getShip().getClass().toString().equals("class " + classPath + "Boat")){
                            agregationList.add(tableField.getShip());
                        }
                    }
                    tempCB.setItems(agregationList);
                    gridPane.add(tempCB,1, i + 1);
                }
                    i++;
            }
        }
    }

    private Field[] getFields(String className){
        try{
            Class tempClass = Class.forName(classPath + className);
            Field[] fieldList = tempClass.getFields();
            return fieldList;
        }catch (Exception e){
            return null;
        }
    }

    private Ship getobj(String className, Field[] fieldList, TextField[] textFieldsArr){
        try {
            Class tempClass = Class.forName(classPath + className);
            Ship obj;
            if (Controller.editTbFld == null)
                obj = (Ship) tempClass.getConstructor().newInstance();
            else
                obj = Controller.editTbFld.getShip();
            int i = 0;
            for(Field field: fieldList){
                String s = field.getGenericType().getTypeName();
                if (s.equals("int"))
                    field.setInt(obj, Integer.parseInt(textFieldsArr[i].getText()));
                else if (s.equals("byte"))
                    field.setByte(obj,Byte.parseByte(textFieldsArr[i].getText()));
                else if (s.equals("boolean"))
                    field.setBoolean(obj, Boolean.parseBoolean(textFieldsArr[i].getText()));
                else if (s.equals("java.lang.String"))
                    field.set(obj, textFieldsArr[i].getText());
                else if (s.equals(classPath + "Boat"))
                    if (tempCB.getValue().equals(""))
                        field.set(obj, null);
                    else
                        field.set(obj,tempCB.getValue());
                i++;
            }
            return obj;
        }catch (Exception e){
            return null;
        }
    }

    public void addClickBtn(){
        Ship obj;
        obj = getobj(classNameCB.getValue(), fieldList, textFieldsArr);
        if (obj != null && Controller.editTbFld == null) {
            Controller.tableDataList.add(new TableField(obj, obj.hashCode()));
        }
        Stage stage = (Stage) applyBtn.getScene().getWindow();
        stage.close();
    }
}
