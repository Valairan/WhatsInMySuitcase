package com.valairan.inventory;

public class Bag {
    public String fullName;
    public String capacity;
    public String specialNotes;

    public Bag(){

    }

    public Bag(String Name, String capacity, String notes){
        if(!Name.isEmpty()){
            this.fullName = Name;
        }else {
            this.fullName = "Bag";
        }
        if(!capacity.isEmpty()){
            this.capacity = capacity;
        }else {
            this.capacity = "Unknown";
        }
        if(!notes.isEmpty()){
            this.specialNotes= notes;
        }else {
            this.specialNotes = "None";
        }

    }
}
