package ucf.assignments;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.math.BigDecimal;
import java.util.StringTokenizer;

public class FileManagement {

    MainWindowMethods methods = new MainWindowMethods();

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
            else if(fileExtension.equalsIgnoreCase("txt")){
                return exportToTsv(toSave);
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
                String serial = object.get("serialNumber").toString();
                String name = object.get("name").toString();

                if(methods.verifySerialNumber(serial)) {
                    ItemHolder.itemList.add(new Item(price, serial, name));
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Uh Oh");
        return false;
    }

    public boolean importHtml(File toImport){

        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(toImport));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
            String[] lines = contentBuilder.toString().split("<br>");
            for(String s: lines){
                if(s.length() != 0){

                    StringTokenizer tokenizer = new StringTokenizer(s, " ");

                    BigDecimal price = new BigDecimal(tokenizer.nextToken());
                    String serial = tokenizer.nextToken();
                    String name = tokenizer.nextToken();

                    if(methods.verifySerialNumber(serial)){
                        ItemHolder.itemList.add(new Item(price, serial, name));
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean importTsv(File toImport){

        try {
            BufferedReader in = new BufferedReader(new FileReader(toImport));
            String str;
            while ((str = in.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(str, "\t");

                BigDecimal price = new BigDecimal(tokenizer.nextToken());
                String serial = tokenizer.nextToken();
                String name = tokenizer.nextToken();

                if(methods.verifySerialNumber(serial)) {
                    ItemHolder.itemList.add(new Item(price, serial, name));
                }
            }
            in.close();

        } catch (IOException e) {
            return false;
        }

        return true;
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

    public boolean exportToTsv(File toSave){

        if(ItemHolder.itemList.isEmpty()){
            return false;
        }

        try {
            PrintWriter writer = new PrintWriter(toSave);
            StringBuilder outputString = new StringBuilder();
            for (int i = 0; i < ItemHolder.itemList.size(); i++) {
                Item currentItem = ItemHolder.itemList.get(i);

                outputString.append(currentItem.getPrice() + "\t");
                outputString.append(currentItem.getSerialNumber() + "\t");
                outputString.append(currentItem.getName());
                outputString.append("\n");
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
