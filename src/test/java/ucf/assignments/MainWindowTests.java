package ucf.assignments;

import org.json.simple.ItemList;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class MainWindowTests {

    MainWindowMethods windowMethods = new MainWindowMethods();

    @Test
    void verifyNameTest(){

        //Create and append a string buffer that is greater than 256 characters using a loop
        StringBuffer overLimitName = new StringBuffer(256);
        for (int i = 0; i < 257; i++){
            overLimitName.append(" ");
        }

        //Verify that a name less than 2 characters will not pass verification
        if(windowMethods.verifyName("a")){
            assert(false);
        }
        //Verify that a name in between 2 and 256 characters will pass verification
        else if(!windowMethods.verifyName("valid name")){
            assert(false);
        }
        //Verify that a name greater than 256 characters will not pass verification
        else if(windowMethods.verifyName(overLimitName.toString())){
            assert(false);
        }

    }

    @Test
    void verifySerialTest(){

        //Add a placeholder item to our list with the serial number "aaaaaaaaa"
        //This items serial number will be used to verify duplicates cannot be created
        ItemHolder.itemList.add(new Item(new BigDecimal("256"), "aaaaaaaaaa", "placeholder"));

        //Verify that a serial number that is not exactly 10 characters will not pass verification
        if(windowMethods.verifySerialNumber("not-ten")){
            assert(false);
        }
        //Verify that a serial number that is exactly 10 characters will pass verification
        else if(!windowMethods.verifySerialNumber("0123456789")){
            assert(false);
        }
        //Verify that a serial number that is already assigned to another task will not pass verification
        else if(windowMethods.verifySerialNumber("aaaaaaaaaa")){
            assert(false);
        }

    }

    @Test
    void verifyPriceTest(){

        /*Price verification only relates to the value entered not being null,
        since we have intialized the price column within the tableview as one that holds
        a BigDecimal value, the built in java constraints make sure you don't enter characters*/

        BigDecimal nullTest = null;

        //Verify that passing a null value in will not pass verification
        if(windowMethods.verifyPrice(nullTest)){
            assert(false);
        }

    }

    @Test
    void makeNewTaskTest(){

        //Verify that the method will create a new task with values that meet the constraints
        if(!windowMethods.makeNewTaskMethod(new BigDecimal("256"), "item", "bbbbbbbbbb")){
            assert(false);
        }
        //Verify that the method will not create a task with a serial number that doesn't meet constraints
        else if(windowMethods.makeNewTaskMethod(new BigDecimal("256"), "item", "")){
            assert(false);
        }
        //Verify that the method will not create a task with a price that doesn't meet constraints
        else if(windowMethods.makeNewTaskMethod(null, "item", "cccccccccc")){
            assert(false);
        }
        //Verify that the method will not create a task with a name that doesn't meet constraints
        else if(windowMethods.makeNewTaskMethod(new BigDecimal("256"), "i", "dddddddddd")){
            assert(false);
        }
        //Verify that the method will not create a task with a duplicate serial number
        else if(windowMethods.makeNewTaskMethod(new BigDecimal("256"), "item", "aaaaaaaaaa")){
            assert(false);
        }

    }

    @Test
    void deleteTaskTest(){

        /*Create two new items, add one to out list but don't add the other
        we will try to delete the second item and verify we cannot delete an item from the list
        that does not exist within it*/
        Item withinlist = new Item(new BigDecimal("256"), "eeeeeeeeee", "placeholder");
        Item notWithinlist = new Item(new BigDecimal("256"), "aaaaaaaaaa", "placeholder");
        ItemHolder.itemList.add(withinlist);

        //Verify that we can delete the added item from the list
        if(!windowMethods.deleteItemMethod(withinlist)){
            assert(false);
        }
        //Verify that we cant delete the item that was not added to the list
        else if(windowMethods.deleteItemMethod(notWithinlist)){
            assert(false);
        }

    }

}
