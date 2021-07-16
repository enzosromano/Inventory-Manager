package ucf.assignments;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.math.BigDecimal;

public class MainWindowController {

    public TableView<Item> itemTable;

    public TextField SerialNumberField;
    public TextField ItemNameField;
    public TextField ItemPriceField;
    public TextArea ErrorOutput;

    ItemHolder holder = new ItemHolder();
    MainWindowMethods methods = new MainWindowMethods();

    public void initialize() {
        itemTable.setEditable(true);

        TableColumn<Item, String> SerialNumberColumn = new TableColumn<>("Serial Number");
        SerialNumberColumn.setMinWidth(150);
        SerialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));

        TableColumn<Item, String> NameColumn = new TableColumn<>("Name");
        NameColumn.setMinWidth(150);
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, BigDecimal> PriceColumn = new TableColumn<>("Price");
        PriceColumn.setMinWidth(150);
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        itemTable.setItems(ItemHolder.itemList);
        itemTable.getColumns().addAll(SerialNumberColumn, NameColumn, PriceColumn);
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
