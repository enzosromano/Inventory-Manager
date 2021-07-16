package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.Set;

public class ItemHolder {

    public static ObservableList<Item> itemList = FXCollections.observableArrayList();
    public static Set<String> Serials = new HashSet<String>();

}
