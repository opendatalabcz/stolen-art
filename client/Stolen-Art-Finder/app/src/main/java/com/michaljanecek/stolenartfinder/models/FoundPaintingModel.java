package com.michaljanecek.stolenartfinder.models;

public class FoundPaintingModel {

    private Integer id;
    private String name;
    private String image;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return image;
    }
}
