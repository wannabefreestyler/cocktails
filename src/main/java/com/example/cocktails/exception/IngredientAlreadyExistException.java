package com.example.cocktails.exception;

public class IngredientAlreadyExistException extends Exception {
  public IngredientAlreadyExistException(String message) {
    super(message);
  }
}