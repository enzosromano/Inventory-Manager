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


    ItemHolder holder = new ItemHolder();
    MainWindowMethods methods = new MainWindowMethods();
    FileManagement fileManager = new FileManagement();

    public void initialize() {

        SerialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

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

        FilteredList<Item> filteredData = new FilteredList<>(ItemHolder.itemList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (item.getSerialNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Item> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(itemTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        itemTable.setItems(sortedData);
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
        if(!fileManager.exportFile(toSave)){
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
