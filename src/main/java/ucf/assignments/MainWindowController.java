/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Enzo Romano
 */

package ucf.assignments;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.converter.BigDecimalStringConverter;
import java.io.File;
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
    public TextField filterField;


    MainWindowMethods methods = new MainWindowMethods();
    FileManagement fileManager = new FileManagement();

    //Called everytime the main view is shown
    public void initialize() {

        SerialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        itemTable.setEditable(true);
        SerialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        NameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        /*User input will be stored as a string when editing in the tableview, use a converter
        and a try catch statement to manipulate user input to follow our format*/
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

        //The following code in this method is used to control our search bar functionality
        FilteredList<Item> filteredData = new FilteredList<>(ItemHolder.itemList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                //Compare names and serial numbers to find matches
                if (item.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                } else if (item.getSerialNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else
                    return false; // Does not match.
            });
        });

        //Put our items into a sorted list and display that particular list when using our search bar
        SortedList<Item> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemTable.comparatorProperty());
        itemTable.setItems(sortedData);
    }

    //Method to control our editing serial number functionality
    public void changeSerialNumberCellEvent(TableColumn.CellEditEvent editedCell){

        Item itemSelected = itemTable.getSelectionModel().getSelectedItem();
        String newSerial = editedCell.getNewValue().toString();
        if(methods.verifySerialNumber(newSerial)) {
            itemSelected.setSerialNumber(newSerial);
        }
        else{
            ErrorOutput.setText("Serials: Combination of 10 letters/numbers (No Duplicates!)");
            initialize();
        }

    }

    //Method to control our editing name functionality
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

    //Method to control our editing name functionality
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

    //Method to control our export functionality; prompts a file chooser for the user to interact with
    public void saveAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html"),
                new FileChooser.ExtensionFilter("TSV files (*.txt)", "*.txt"));

        File toSave = fileChooser.showSaveDialog(null);
        if(!fileManager.exportFile(toSave)){
            ErrorOutput.setText("Could not export");
        }
    }

    //Method to control our import functionality; prompts a file chooser for the user to interact with
    public void importFile(){
        FileChooser fileChooser = new FileChooser();
        File toImport = fileChooser.showOpenDialog(null);

        String fileName = toImport.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, toImport.getName().length());

        if(fileExtension.equalsIgnoreCase("json")){
            if(!fileManager.importJson(toImport)){
                ErrorOutput.setText("Could not import JSON file!");
            }
        }
        else if(fileExtension.equalsIgnoreCase("html")){
            if(!fileManager.importHtml(toImport)){
                ErrorOutput.setText("Could not import HTML file!");
            }
        }
        else if(fileExtension.equalsIgnoreCase("txt")){
            if(!fileManager.importTsv(toImport)){
                ErrorOutput.setText("Could not import TSV file!");
            }
        }

    }

    //Method to create a new task on the user interface, all fields must be full and inputs must be verified
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

    //Method that gets our currently selected task and removes it from our ItemHolder
    public void deleteTask() {

        Item toDelete = itemTable.getSelectionModel().getSelectedItem();
        if(!methods.deleteItemMethod(toDelete)){
            ErrorOutput.setText("Issue Deleting");
        }

    }

    //Deletes all tasks currently in our inventory
    public void deleteAll(){
        ItemHolder.itemList.clear();
    }

    //Exits the program
    public void exitProgram(){
        System.exit(0);
    }

}
