package com.hms.grocy;

import java.util.ArrayList;

public class Cart {

    private int id;
    private int consumerId;
    private ArrayList<CartItem> items;

    public Cart(int id, int consumerId, ArrayList<CartItem> items) {
        this.id = id;
        this.consumerId = consumerId;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(int consumerId) {
        this.consumerId = consumerId;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }
}
