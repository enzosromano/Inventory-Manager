package ucf.assignments;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        for(String serialNum: ItemHolder.Serials){
            if(serialNum.equalsIgnoreCase(itemSerialNumber)){
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

    public boolean exportFile(File toSave){
        if (toSave != null) {
            String fileName = toSave.getName();
            System.out.println(fileName);
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, toSave.getName().length());
            System.out.println(">> fileExtension" + fileExtension);

            try {
                if (toSave.createNewFile()) {
                    System.out.println("File created: " + toSave.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            if(fileExtension.equalsIgnoreCase("json")){
                return exportToJson(toSave);
            }
            else if(fileExtension.equalsIgnoreCase("html")){
                return exportToHtml(toSave);
            }

        }

        return true;

    }

    public boolean importJson(File toImport){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(toImport.getPath()));

            JSONObject jsonObject = (JSONObject) obj;
            //Tasks must be stored within a JSON object of the value "tasks"
            JSONArray itemList = (JSONArray) jsonObject.get("items");

            for (JSONObject object : (Iterable<JSONObject>) itemList) {
                BigDecimal price = new BigDecimal(object.get("price").toString());
                ItemHolder.itemList.add(new Item(price, object.get("serialNumber").toString(),
                        object.get("name").toString()));
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Uh Oh");
        return false;
    }

    public boolean exportToJson(File toSave){

        if(ItemHolder.itemList.isEmpty()){
            return false;
        }

        //Create an array to store our tasks in and then an object to format it for our JSON output
        JSONArray allItems = new JSONArray();
        JSONObject toJson = new JSONObject();
        for (int i = 0; i < ItemHolder.itemList.size(); i++) {
            //Store each task in an abject so we can add it to our array of objects
            JSONObject currentItem = new JSONObject();
            currentItem.put("price", ItemHolder.itemList.get(i).getPrice());
            currentItem.put("serialNumber", ItemHolder.itemList.get(i).getSerialNumber());
            currentItem.put("name", ItemHolder.itemList.get(i).getName());
            allItems.add(currentItem);
        }
        toJson.put("items", allItems);

        //Put our output in string form for writing to the file
        String jsonString = toJson.toString();
        try {
            FileWriter myWriter = new FileWriter(toSave);
            myWriter.write(jsonString);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean exportToHtml(File toSave) {

        if(ItemHolder.itemList.isEmpty()){
            return false;
        }

        try {
            PrintWriter writer = new PrintWriter(toSave);
            StringBuilder outputString = new StringBuilder();
            for (int i = 0; i < ItemHolder.itemList.size(); i++) {
                Item currentItem = ItemHolder.itemList.get(i);

                outputString.append(currentItem.getPrice() + " ");
                outputString.append(currentItem.getSerialNumber() + " ");
                outputString.append(currentItem.getName());
                outputString.append("<br>");
            }
            writer.write(String.valueOf(outputString));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
