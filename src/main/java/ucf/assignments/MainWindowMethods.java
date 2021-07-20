/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Enzo Romano
 */

package ucf.assignments;

import java.math.BigDecimal;

public class MainWindowMethods {

    public boolean verifyName(String itemName){
        if(itemName.length() < 2 || itemName.length() > 256){
            return false;
        }
        return true;
    }
    public boolean verifySerialNumber(String itemSerialNumber){
        if(itemSerialNumber.length() != 10){
            return false;
        }
        for(int i = 0; i < ItemHolder.itemList.size(); i++){
            if(ItemHolder.itemList.get(i).getSerialNumber().equals(itemSerialNumber)){
                System.out.println(itemSerialNumber + " does match");
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

    public boolean verifyPrice(BigDecimal itemPrice){
        if(itemPrice != null){
            return true;
        }
        return false;
    }


    public boolean makeNewTaskMethod(BigDecimal itemPrice, String itemName, String itemSerialNumber) {

        if(!verifyName(itemName) || !verifySerialNumber(itemSerialNumber) || !verifyPrice(itemPrice)){
            return false;
        }

        ItemHolder.Serials.add(itemSerialNumber);
        ItemHolder.itemList.add(new Item(itemPrice, itemSerialNumber, itemName));
        return true;

    }

    public boolean deleteItemMethod(Item toDelete){

        if(ItemHolder.itemList.contains(toDelete)){
            ItemHolder.itemList.remove(toDelete);
            return true;
        }

        return false;
    }


}
