/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Enzo Romano
 */

package ucf.assignments;

import java.math.BigDecimal;

public class MainWindowMethods {

    //Verify name input matches constraints
    public boolean verifyName(String itemName){
        if(itemName.length() < 2 || itemName.length() > 256){
            return false;
        }
        return true;
    }

    //Verify serial number input matches constraints
    public boolean verifySerialNumber(String itemSerialNumber){
        if(itemSerialNumber.length() != 10){
            return false;
        }
        for(int i = 0; i < ItemHolder.itemList.size(); i++){
            if(ItemHolder.itemList.get(i).getSerialNumber().equals(itemSerialNumber)){
                return false;
            }
        }
        for(int i = 0; i < itemSerialNumber.length(); i++){
            if(!Character.isLetter(itemSerialNumber.charAt(i)) && !Character.isDigit(itemSerialNumber.charAt(i))){
                return false;
            }
        }
        return true;
    }

    //Verify price input matches constraints
    public boolean verifyPrice(BigDecimal itemPrice){
        if(itemPrice != null){
            return true;
        }
        return false;
    }

    //Method to add a task to our ItemHolder; will not add a task if it does not meet constraints
    public boolean makeNewTaskMethod(BigDecimal itemPrice, String itemName, String itemSerialNumber) {

        if(!verifyName(itemName) || !verifySerialNumber(itemSerialNumber) || !verifyPrice(itemPrice)){
            return false;
        }

        ItemHolder.Serials.add(itemSerialNumber);
        ItemHolder.itemList.add(new Item(itemPrice, itemSerialNumber, itemName));
        return true;

    }

    //Method to delete an item from our ItemHolder
    public boolean deleteItemMethod(Item toDelete){

        if(ItemHolder.itemList.contains(toDelete)){
            ItemHolder.itemList.remove(toDelete);
            return true;
        }

        return false;
    }


}
