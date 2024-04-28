
package com.example.cocktails.dto;

import com.example.cocktails.entity.Ingredient;

public class IngredientDto {

  private String name;

  public static IngredientDto toModel(Ingredient entity) {
    IngredientDto model = new IngredientDto();
    model.setName(entity.getName());
    return model;
  }

  public IngredientDto() {
    // No initialization logic needed for this constructor
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}