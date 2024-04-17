package com.example.cocktails.dto;

import com.example.cocktails.entity.Image;

public class ImageDTO {
    private String picture;

    public static ImageDTO toModel(Image entity){
        ImageDTO model = new ImageDTO();
        model.setPicture(entity.getPicture());
        return model;
    }

    public ImageDTO() {
        // No initialization logic needed for this constructor
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}