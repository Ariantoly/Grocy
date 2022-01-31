package com.hms.grocy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GroceryStore implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("groceries")
    private ArrayList<Grocery> groceries;

    public GroceryStore(int id, String name, String address, String city, ArrayList<Grocery> groceries) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.groceries = groceries;
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

    public ArrayList<Grocery> getGroceries() {
        return groceries;
    }

    public void setGroceries(ArrayList<Grocery> groceries) {
        this.groceries = groceries;
    }

    public int getGroceryCount() {
        return groceries.size();
    }
}
