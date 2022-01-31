package com.hms.grocy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartItem implements Serializable {

    @SerializedName("grocery")
    private Grocery grocery;

    @SerializedName("quantity")
    private int qty;

    private boolean isChecked;

    public CartItem(Grocery grocery, int qty) {
        this.grocery = grocery;
        this.qty = qty;
    }

    public Grocery getGrocery() {
        return grocery;
    }

    public void setGrocery(Grocery grocery) {
        this.grocery = grocery;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
