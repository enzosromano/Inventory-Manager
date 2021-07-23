/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Enzo Romano
 */

package ucf.assignments;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.math.BigDecimal;
import java.util.StringTokenizer;

public class FileManagement {

    MainWindowMethods methods = new MainWindowMethods();

    //Method to export all items currently within our ItemHolder
    //The file destination/name is passed in from our file chooser in our Controller
    public boolean exportFile(File toSave){

        if (toSave != null) {

            String fileName = toSave.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, toSave.getName().length());

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

            //Break apart our files extension and call a method based on the format selected
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

    //Method to import items from a .json file into our ItemHolder
    public boolean importJson(File toImport){

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(toImport.getPath()));

            JSONObject jsonObject = (JSONObject) obj;
            //Tasks must be stored within a JSON object of the value "items"
            JSONArray itemList = (JSONArray) jsonObject.get("items");

            for (JSONObject object : (Iterable<JSONObject>) itemList) {
                BigDecimal price = new BigDecimal(object.get("price").toString());
                String serial = object.get("serialNumber").toString();
                String name = object.get("name").toString();

                //Verify our serial number to make sure we don't have serial number duplicates
                if(methods.verifySerialNumber(serial)) {
                    ItemHolder.itemList.add(new Item(price, serial, name));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Method to import items from a .html file into our ItemHolder
    public boolean importHtml(File toImport){

        try{
            Document doc = Jsoup.parse(toImport, "UTF-8", "");
            Element table = doc.select("table").get(0);
            Elements rows = table.select("tr");

            //Go through our table and parse every single row
            for (int i = 1; i < rows.size(); i++) {

                Elements tds = rows.select("td");
                BigDecimal price = new BigDecimal(tds.get(0).text());
                String serial = tds.get(1).text();
                String name = tds.get(2).text();

                if(methods.verifySerialNumber(serial)){
                    ItemHolder.itemList.add(new Item(price, serial, name));
                }
            }
            return true;
        }
        catch(IOException exception){
            return false;
        }


    }

    //Method to import items from a .txt file into our ItemHolder
    public boolean importTsv(File toImport){

        try {
            BufferedReader in = new BufferedReader(new FileReader(toImport));
            String str;
            while ((str = in.readLine()) != null) {
                //Split our item values by a tab character
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

    //Method to export the items in our ItemHolder to a .json file
    public boolean exportToJson(File toSave){

        if(ItemHolder.itemList.isEmpty()){
            return false;
        }

        JSONArray allItems = new JSONArray();
        JSONObject toJson = new JSONObject();
        for (int i = 0; i < ItemHolder.itemList.size(); i++) {
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

    //Method to export the items in our ItemHolder to a .html file
    public boolean exportToHtml(File toSave) {

        if(ItemHolder.itemList.isEmpty()){
            return false;
        }

        try {
            PrintWriter writer = new PrintWriter(toSave);
            StringBuilder outputString = new StringBuilder();

            //Set table title and column headers
            outputString.append("<html>" + "<head>table</head>" + "<body>" + "<table border = '1'>");
            outputString.append("<tr>");
            outputString.append("<td>Price</td>");
            outputString.append("<td>Serial Number</td>");
            outputString.append("<td>Name</td>");
            outputString.append("</tr>");
            //Add our items to our table
            for (int i = 0; i < ItemHolder.itemList.size(); i++) {
                Item currentItem = ItemHolder.itemList.get(i);

                outputString.append("<tr>");
                outputString.append("<td>" + currentItem.getPrice() + "</td>");
                outputString.append("<td>" + currentItem.getSerialNumber() + "</td>");
                outputString.append("<td>" + currentItem.getName() + "</td>");
                outputString.append("</tr>");

            }
            //Close out our table element and output to file
            outputString.append("</table></body></html>");
            writer.write(String.valueOf(outputString));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    //Method to export the items in our ItemHolder to a .txt file
    public boolean exportToTsv(File toSave){

        if(ItemHolder.itemList.isEmpty()){
            return false;
        }

        try {
            PrintWriter writer = new PrintWriter(toSave);
            StringBuilder outputString = new StringBuilder();
            for(Item currentItem: ItemHolder.itemList){
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
