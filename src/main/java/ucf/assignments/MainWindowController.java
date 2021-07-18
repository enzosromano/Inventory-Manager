package ucf.assignments;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class MainWindowController {

    public TableView<Item> itemTable;

    public TextField SerialNumberField;
    public TextField ItemNameField;
    public TextField ItemPriceField;
    public TextArea ErrorOutput;
    public TableColumn SerialNumberColumn;
    public TableColumn NameColumn;
    public TableColumn PriceColumn;


    ItemHolder holder = new ItemHolder();
    MainWindowMethods methods = new MainWindowMethods();

    public void initialize() {

        SerialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        itemTable.setItems(ItemHolder.itemList);
        itemTable.setEditable(true);
        SerialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        NameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        PriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter(){
            @Override
            public BigDecimal fromString(String value) {
                try {
                    return super.fromString(value);
                } catch(NumberFormatException e) {
                    ErrorOutput.setText("Price: Must be a decimal value");
                    return null;
                }
            }
        }));
    }

    public void changeSerialNumberCellEvent(TableColumn.CellEditEvent editedCell){

        Item itemSelected = itemTable.getSelectionModel().getSelectedItem();
        String newSerial = editedCell.getNewValue().toString();
        if(methods.verifySerialNumber(newSerial)) {
            itemSelected.setSerialNumber(newSerial);
        }
        else{
            ErrorOutput.setText("Serials: Combination of 10 letters/numbers (No Duplicates!)");
            System.out.println(itemSelected.getSerialNumber());
            initialize();
        }

    }

    public void changeNameCellEvent(TableColumn.CellEditEvent editedCell){

        Item itemSelected = itemTable.getSelectionModel().getSelectedItem();
        String newName = editedCell.getNewValue().toString();
        if(methods.verifyName(newName)) {
            itemSelected.setName(newName);
        }
        else{
            ErrorOutput.setText("Names: 2-256 Characters");
            initialize();
        }

    }

    public void changePriceCellEvent(TableColumn.CellEditEvent editedCell){

        Item itemSelected = itemTable.getSelectionModel().getSelectedItem();
        BigDecimal newPrice = (BigDecimal) editedCell.getNewValue();
        if (methods.verifyPrice(newPrice)) {
            itemSelected.setPrice(newPrice);
            System.out.println(itemSelected.getPrice());
        } else {
            ErrorOutput.setText("Price: Must be a decimal value");
            initialize();
        }


    }

    public void saveAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html"),
                new FileChooser.ExtensionFilter("TSV files (*.txt)", "*.txt"));

        File toSave = fileChooser.showSaveDialog(null);
        if(!methods.exportFile(toSave)){
            ErrorOutput.setText("Could not export");
        }
    }

    public void importFile(){
        FileChooser fileChooser = new FileChooser();
        File toImport = fileChooser.showOpenDialog(null);

        String fileName = toImport.getName();
        System.out.println(fileName);
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, toImport.getName().length());
        System.out.println(">> fileExtension" + fileExtension);


        if(fileExtension.equalsIgnoreCase("json")){
            if(!methods.importJson(toImport)){
                ErrorOutput.setText("Could not import JSON file!");
            }
        }
        else if(fileExtension.equalsIgnoreCase("html")){
            if(!methods.importHtml(toImport)){
                ErrorOutput.setText("Could not import HTML file!");
            }
        }
        else if(fileExtension.equalsIgnoreCase("txt")){
            if(!methods.importTsv(toImport)){
                ErrorOutput.setText("Could not import TSV file!");
            }
        }

    }

    public void makeNewTask() {

        String nameInput = ItemNameField.getText();
        String serialNumberInput = SerialNumberField.getText();
        for(String serialNum: ItemHolder.Serials){
            if(serialNum.equalsIgnoreCase(serialNumberInput)){
                SerialNumberField.clear();
                SerialNumberField.setPromptText("No duplicate serials.");
                return;
            }
        }
        BigDecimal priceValue = null;
        try {
            Double priceInput = Double.valueOf(ItemPriceField.getText());
            priceValue = BigDecimal.valueOf(priceInput);
        }
        catch(NumberFormatException e) {
            ItemPriceField.clear();
            ItemPriceField.setPromptText("Must be a decimal value!");
        }

        if(!methods.makeNewTaskMethod(priceValue, nameInput, serialNumberInput)){
            ItemNameField.clear();
            SerialNumberField.clear();
            ErrorOutput.setText("Constraints: Names: 2-256 Characters -- " +
                    "Serials: Combination of 10 letters/numbers (no duplicates)");
        }

    }

    public void deleteTask() {

        Item toDelete = itemTable.getSelectionModel().getSelectedItem();
        if(!methods.deleteItemMethod(toDelete)){
            ErrorOutput.setText("Issue Deleting");
        }

    }

}
