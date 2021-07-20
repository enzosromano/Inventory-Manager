/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Enzo Romano
 */

package ucf.assignments;

import java.math.BigDecimal;

public class Item {

    private BigDecimal price;
    private String serialNumber;
    private String name;

    public Item(){
        this.price = BigDecimal.valueOf(0);
        this.serialNumber = "";
        this.name = "";
    }
    public Item(BigDecimal price, String serialNumber, String name){
        this.price = price;
        this.serialNumber = serialNumber;
        this.name = name;
    }

    public BigDecimal getPrice(){
        return price;
    }
    public void setPrice(BigDecimal itemPrice){
        this.price = itemPrice;
    }

    public String getSerialNumber(){
        return serialNumber;
    }
    public void setSerialNumber(String itemSerialNumber){
        this.serialNumber = itemSerialNumber;
    }

    public String getName(){
        return name;
    }
    public void setName(String itemName){
        this.name = itemName;
    }

}
