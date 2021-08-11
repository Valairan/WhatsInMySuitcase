package com.valairan.Abstract;

public class Item {
    public String getItemName() {
        return itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getItemType() {
        return itemType;
    }

    public String getNotes() {
        return notes;
    }

    public String itemName;
    public String itemQuantity;
    public String itemLocation;
    public String itemType;
    public String notes;

    public void Item(){

    }


    public Item(String name, String quantity, String location, String type, String splNotes){
        itemName = name;
        itemQuantity = quantity;
        itemLocation = location;
        itemType = type;
        notes = splNotes;
    }

}
