package com.hms.grocy;

import java.util.ArrayList;

public class GroceryStore {

    private int id;
    private String name;
    private String address;
    private String city;
    private ArrayList<Grocery> grocies;

    public GroceryStore(int id, String name, String address, String city, ArrayList<Grocery> grocies) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.grocies = grocies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<Grocery> getGrocies() {
        return grocies;
    }

    public void setGrocies(ArrayList<Grocery> grocies) {
        this.grocies = grocies;
    }
}
