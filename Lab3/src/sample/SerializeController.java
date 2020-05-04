package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.lang.reflect.Field;

public class SerializeController {

    private String binaryFilePath = "D:\\4 sem\\OOTП\\Lab3\\binary.txt";
    private String xmlFilePath = "D:\\4 sem\\OOTП\\Lab3\\Date.xml";
    private String textFilePath = "D:\\4 sem\\OOTП\\Lab3\\text.txt";
    private String classPath = "sample.";

    public void binarySerialize() throws Exception{
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(binaryFilePath));
        for (int i = 0; i < Controller.tableDataList.size(); i++) {
            TableField tableField = Controller.tableDataList.get(i);
            out.writeObject(tableField.getShip());
        }
        out.close();
    }

    public void binaryDeserialize() throws Exception{
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(binaryFilePath));
        Controller.tableDataList.clear();
        while (true) {
            try {
                Ship tmp = (Ship) in.readObject();
                Controller.tableDataList.add(new TableField(tmp,tmp.hashCode()));
            }catch (Exception e) {
                in.close();
                break;
            }
        }
    }

    public void XMLSerialize() throws Exception {
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(xmlFilePath)));
        for (TableField field : Controller.tableDataList) {
            encoder.writeObject(field.getShip());
        }
        encoder.close();
    }

    public void XMLDeserialize() throws Exception {
        FileInputStream in = new FileInputStream(xmlFilePath);
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
        Controller.tableDataList.clear();
        while (true) {
            try {
                Ship tmp = (Ship) decoder.readObject();
                Controller.tableDataList.add(new TableField(tmp, tmp.hashCode()));
            } catch (ArrayIndexOutOfBoundsException e) {
                decoder.close();
                break;
            }
        }
        in.close();
    }

    private String serializeString(Object obj) throws IllegalAccessException {
        String buf = "";
        buf += obj.getClass().getName() + "_" + obj.hashCode() + "(";
        Field[] fieldList = obj.getClass().getFields();
        for (Field field : fieldList) {
            buf += field.getName() + ":";
            if (field.getGenericType().getTypeName().equals(classPath + "Boat")) {
                if (field.get(obj) == null){
                    buf += "null;";
                } else {
                    buf += field.get(obj).getClass().getName() + "_" + field.get(obj).hashCode() + ";";
                }
            } else {
                buf += field.get(obj).toString() + ";";
            }
        }
        buf += ")";
        return buf;
    }

    public void textSerialize() throws Exception {
        FileWriter out = new FileWriter(textFilePath, false);
        for (int i = 0; i < Controller.tableDataList.size(); i++) {
            String buf = "";
            TableField tableField = Controller.tableDataList.get(i);
            String s = tableField.getShip().getClass().getName();
            buf = serializeString(tableField.getShip());
            buf += "\n";
            out.write(buf);
        }
        out.close();
    }

    public void textDeserialize() throws Exception {
        FileReader in = new FileReader(textFilePath);
        int c = 0;
        String lastSumb = "";
        ObservableList<Integer> idefList = FXCollections.observableArrayList();
        ObservableList<TableField> bufferAgregationList = FXCollections.observableArrayList();
        Controller.tableDataList.clear();
        while (true) {
            String buf = "";
            buf += lastSumb;
            while ((char) (c = in.read()) != '_') {
                buf += (char) c;
            }
            Class tempClass = Class.forName(buf);
            Field[] fieldList = tempClass.getFields();

            buf = "";
            while ((char) (c = in.read()) != '(') {
                buf += (char) c;
            }
            int idef = Integer.parseInt(buf);
            idefList.add(idef);

            Ship obj = null;
            if (tempClass.getName().equals(classPath + "Boat")) {
                boolean isFind = false;
                for (int i = 0; i < bufferAgregationList.size(); i++) {
                    if (idef == bufferAgregationList.get(i).getHshCd()) {
                        obj = bufferAgregationList.get(i).getShip();
                        isFind = true;
                    }
                }
                if (!isFind) {
                    obj = (Ship) tempClass.getConstructor().newInstance();
                }
            } else {
                obj = (Ship) tempClass.getConstructor().newInstance();
            }

            for (Field field : fieldList) {
                while ((char) c != ':') {
                    c = in.read();
                }
                buf = "";
                while ((char) (c = in.read()) != ';')
                    buf += (char) c;

                String s = field.getGenericType().getTypeName();
                if (s.equals("int"))
                    field.setInt(obj, Integer.parseInt(buf));
                else if (s.equals("byte"))
                    field.setByte(obj, Byte.parseByte(buf));
                else if (s.equals("boolean"))
                    field.setBoolean(obj, Boolean.parseBoolean(buf));
                else if (s.equals("java.lang.String"))
                    field.set(obj, buf);
                else if (s.equals(classPath + "Boat")) {
                    if (buf.equals("null"))
                        field.set(obj, null);
                    else {
                        buf = buf.replace(classPath + "Boat_", "");
                        int idefBuf = Integer.parseInt(buf);
                        if (idefList.contains(idefBuf)) {
                            for (int j = 0; j < idefList.size(); j++) {
                                if (idefBuf == idefList.get(j)) {
                                    field.set(obj, Controller.tableDataList.get(j).getShip());
                                }
                            }
                        } else {
                            Boat boat = null;
                            boolean isFind = false;
                            for (int j = 0; j < bufferAgregationList.size(); j++) {
                                if (idefBuf == bufferAgregationList.get(j).getHshCd()){
                                    boat = (Boat) bufferAgregationList.get(j).getShip();
                                    isFind = true;
                                }
                            }
                            if (!isFind){
                                boat = new Boat();
                            }
                            bufferAgregationList.add(new TableField(boat, idefBuf));
                            field.set(obj, boat);
                        }
                    }
                }
            }
            Controller.tableDataList.add(new TableField(obj, obj.hashCode()));
            c = in.read();
            c = in.read();
            if ((c = in.read()) == -1) {
                break;
            } else {
                lastSumb = String.valueOf((char) c);
            }
        }
    }
}
