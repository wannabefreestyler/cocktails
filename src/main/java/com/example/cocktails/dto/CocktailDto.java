package com.example.cocktails.dto;

import com.example.cocktails.entity.Cocktail;
import java.util.ArrayList;
import java.util.List;

public class CocktailDto {
  private String name;
  private String category;
  private String tag;
  private String instruction;
  private List<ImageDto> imagesList = new ArrayList<>();
  private List<IngredientDto> ingredientsList = new ArrayList<>();

  public static CocktailDto toModel(Cocktail entity) {
    CocktailDto model = new CocktailDto();

    model.setName(entity.getName());
    model.setTag(entity.getTag());
    model.setCategory(entity.getCategory());
    model.setInstruction(entity.getInstruction());
    if (model.getImagesList() != null) {
      model.setImagesList(entity.getImageList().stream().map(ImageDto::toModel).toList());
    }
    if (model.getIngredientsList() != null) {
      model.setIngredientsList(
          entity.getIngredientList().stream().map(IngredientDto::toModel).toList());
    }

    return model;
  }

  public CocktailDto() {
    // No initialization logic needed for this constructor
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public List<ImageDto> getImagesList() {
    return imagesList;
  }

  public void setImagesList(List<ImageDto> imageDtoList) {
    this.imagesList = imageDtoList;
  }

  public List<IngredientDto> getIngredientsList() {
    return ingredientsList;
  }

  public void setIngredientsList(List<IngredientDto> ingredientsList) {
    this.ingredientsList = ingredientsList;
  }
}