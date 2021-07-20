/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Enzo Romano
 */

package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.Set;

public class ItemHolder {

    public static ObservableList<Item> itemList = FXCollections.observableArrayList();
    public static Set<String> Serials = new HashSet<String>();

}
