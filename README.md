# Inventory Manager Read-Me

This tutorial will walk you through the program from the top down

## Menu bar:
### Contains the "File" and "Items" menu buttons
File:

    - Save As: Prompts a File Chooser where the user can export the list
    The user can choose from 3 file formats to export to; html, json and txt

    - Load: Promopts a File Chooser where the user can import items from
    The file chosen must have either a .html, .json or .txt extension
    
    - Close: Exits the program

Items:

    - Delete All: Clicking this will delete all items currently in our list

## Search Bar:
### Allows the user to search by keyword
Search:

    - The search bar is constantly updating and typing any characters in it will begin a search

    - In order to have all items reappear, the user must manually clear the search bar

    - Users can ONLY search by the items name or serial number, not price

## Column Headers:
### Tells the user what item information is being displayed within the column

## Table elements:
### Displays the items currently within our Item Holder

    - The tableview will display "No content in table" when there are no items to display

## Add New Item bar:
### Allows the user to add an item to our Item Holder

    - The three fields are editable, in order they are the Serial Number, Name and Price

    - The user must fill out all 3 fields with item information and then click on the
    "Add New item" button to attempt to add it to our Item Holder

    - If the users input DOES NOT meet certain constraints, the constraints will be displayed
    within the output field below the Add Item fields

    - If the users input DOES meet the constraints, clicking "Add New Item" will create
    a new item and add it to our list

## Error field:
### Displays constraints or errors within the program

## Delete selected Item:
### Deletes an item that the user has selected

    - Clicking this button will do nothing if you have not selected an item

    - You can tell if an item is selected before pressing "Delete Selected Item" because
    it will be highlighted within the table when clicked on

    - Deletions are final and can not be recovered