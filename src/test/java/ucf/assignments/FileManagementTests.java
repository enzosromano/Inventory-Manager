package ucf.assignments;

import org.junit.jupiter.api.Test;

import java.io.File;

public class FileManagementTests {

    ItemHolder holder = new ItemHolder();
    FileManagement fileMethods = new FileManagement();

    @Test
    void importJsonTest(){

        //Direct to our hardcoded json file in our main resources folder
        //this will be used as the import to populate our list
        File json = new File("./src/main/resources/JSONImport.json");

        //Call our import json method on our declared file and verify the method doesn't return false,
        //if it does return false, the list could not be imported
        if(!fileMethods.importJson(json)){
            assert(false);
        }
        //Verify that our item list now has a size of 6, the number of items in our file
        if(ItemHolder.itemList.size() != 6){
            assert(false);
        }
        //Verify our first imported item is under the name "Chairs" to match the hardcoded file
        //Verify our last imported item is under the name "Cups" to match the hardcoded file
        if(!ItemHolder.itemList.get(0).getName().equalsIgnoreCase("Chairs")){
            assert(false);
        }
        if(!ItemHolder.itemList.get(5).getName().equalsIgnoreCase("Cups")){
            assert(false);
        }

        //Clear the items in the list so we do not run into issues with duplicated serial numbers
        //for our next test imports
        ItemHolder.itemList.clear();


    }

    @Test
    void importHtmlTest(){

        //Direct to our hardcoded html file in our main resources folder
        //this will be used as the import to populate our list
        File html = new File("./src/main/resources/HTMLImport.html");

        //Call our import html method on our declared file and verify the method doesn't return false,
        //if it does return false, the list could not be imported
        if(!fileMethods.importHtml(html)){
            assert(false);
        }
        //Verify that our item list now has a size of 6, the number of items in our file
        if(ItemHolder.itemList.size() != 6){
            assert(false);
        }
        //Verify our first imported item is under the name "Chairs" to match the hardcoded file
        //Verify our last imported item is under the name "Cups" to match the hardcoded file
        if(!ItemHolder.itemList.get(0).getName().equalsIgnoreCase("Chairs")){
            assert(false);
        }
        if(!ItemHolder.itemList.get(5).getName().equalsIgnoreCase("Cups")){
            assert(false);
        }

        //Clear the items in the list so we do not run into issues with duplicated serial numbers
        //for our next test imports
        ItemHolder.itemList.clear();

    }

    @Test
    void importTsvTest(){

        //Direct to our hardcoded tsv file in our main resources folder
        //this will be used as the import to populate our list
        File tsv = new File("./src/main/resources/TSVImport.txt");

        //Call our import tsv method on our declared file and verify the method doesn't return false,
        //if it does return false, the list could not be imported
        if(!fileMethods.importTsv(tsv)){
            assert(false);
        }
        //Verify that our item list now has a size of 6, the number of items in our file
        if(ItemHolder.itemList.size() != 6){
            assert(false);
        }
        //Verify our first imported item is under the name "Chairs" to match the hardcoded file
        //Verify our last imported item is under the name "Cups" to match the hardcoded file
        if(!ItemHolder.itemList.get(0).getName().equalsIgnoreCase("Chairs")){
            assert(false);
        }
        if(!ItemHolder.itemList.get(5).getName().equalsIgnoreCase("Cups")){
            assert(false);
        }

        //Clear the items in the list so we do not run into issues with duplicated serial numbers
        //for our next test imports
        ItemHolder.itemList.clear();

    }

    @Test
    void exportFiles(){

        //Import one of our priorly made files to initialize items in our list
        File importForList = new File("./src/main/resources/HTMLImport.html");
        if(!fileMethods.importHtml(importForList)){
            assert(false);
        }

        //Create three output file paths in our test resources folder
        File htmlTestFile = new File("./src/test/resources/outputHtmlTest.html");
        File jsonTestFile = new File("./src/test/resources/outputJsonTest.json");
        File tsvTestFile = new File("./src/test/resources/outputTsvTest.txt");

        //Attempt to export a file with our html extension, if it cant be created, fail the test
        if(!fileMethods.exportFile(htmlTestFile)){
            assert(false);
        }
        //Attempt to export a file with our json extension, if it cant be created, fail the test
        if(!fileMethods.exportFile(jsonTestFile)){
            assert(false);
        }
        //Attempt to export a file with our txt extension, if it cant be created, fail the test
        if(!fileMethods.exportFile(tsvTestFile)){
            assert(false);
        }

        //Verify that all of our file paths have files created at them
        if(!htmlTestFile.exists() || !jsonTestFile.exists() || !tsvTestFile.exists()){
            assert(false);
        }

    }

}
