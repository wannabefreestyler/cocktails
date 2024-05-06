package com.example.cocktails.dto;

import com.example.cocktails.entity.Image;

public class ImageDto {
  private String picture;

  public static ImageDto toModel(Image entity) {
    ImageDto model = new ImageDto();
    model.setPicture(entity.getPicture());
    return model;
  }

  public ImageDto() {
    // No initialization logic needed for this constructor
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }
}