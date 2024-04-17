package com.example.cocktails.exception;

public class CocktailAlreadyExistException extends Exception{
    public CocktailAlreadyExistException(String message) {
        super(message);
    }
}
