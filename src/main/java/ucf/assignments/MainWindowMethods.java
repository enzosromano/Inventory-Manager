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
        /*for(String serialNum: ItemHolder.Serials){
            if(serialNum.equalsIgnoreCase(itemSerialNumber)){
                return false;
            }
        }*/
        for(int i = 0; i < itemSerialNumber.length(); i++){
            if(!Character.isLetter(itemSerialNumber.charAt(i)) && !Character.isDigit(itemSerialNumber.charAt(i))){
                return false;
            }
        }
        return true;
    }


    public boolean makeNewTaskMethod(BigDecimal itemPrice, String itemName, String itemSerialNumber) {

        if(!verifyName(itemName) || !verifySerialNumber(itemSerialNumber)){
            return false;
        }

        ItemHolder.Serials.add(itemSerialNumber);
        ItemHolder.itemList.add(new Item(itemPrice, itemSerialNumber, itemName));
        return true;

    }

    public boolean deleteItemMethod(Item toDelete){
        ItemHolder.itemList.remove(toDelete);
        return true;
    }

}
