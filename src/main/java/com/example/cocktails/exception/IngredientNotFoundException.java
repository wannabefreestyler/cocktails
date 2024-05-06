package com.example.cocktails.exception;

public class IngredientNotFoundException extends Exception {
  public IngredientNotFoundException(String message) {
    super(message);
  }
}