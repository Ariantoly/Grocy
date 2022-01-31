package com.hms.grocy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Grocery implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("store")
    private GroceryStore store;

    @SerializedName("category")
    private Category category;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private int price;

    @SerializedName("stock")
    private int stock;

    @SerializedName("sold")
    private int sold;

    @SerializedName("image")
    private String image;

    public Grocery(int id, GroceryStore store, Category category, String name, String description, int price, int stock, int sold, String image) {
        this.id = id;
        this.store = store;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sold = sold;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroceryStore getStore() {
        return store;
    }

    public void setStore(GroceryStore store) {
        this.store = store;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
