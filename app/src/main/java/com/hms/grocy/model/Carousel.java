package com.hms.grocy.model;

import com.google.gson.annotations.SerializedName;

public class Carousel {

    @SerializedName("id")
    private int id;

    @SerializedName("image")
    private String image;

    public Carousel(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
