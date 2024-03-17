package com.example.cocktails.entity;

import com.example.cocktails.entity.Cocktail;
import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Cocktail_INGREDIENT_MAPPING",
            joinColumns = @JoinColumn(name  = "ingredientId"),
            inverseJoinColumns = @JoinColumn(name = "cocktailId"))
    private List<Cocktail> cocktailList = new ArrayList<>();

    public Ingredient() {
        // No initialization logic needed for this constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cocktail> getCocktailList() {
        return cocktailList;
    }

    public void setCocktailList(List<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }
}