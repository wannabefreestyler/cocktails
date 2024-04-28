package com.example.cocktails.exception;

public class CocktailNotFoundException extends Exception {
  public CocktailNotFoundException(String message) {
    super(message);
  }
}
