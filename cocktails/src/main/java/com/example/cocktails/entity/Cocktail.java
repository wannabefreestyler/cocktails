package com.example.cocktails.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String tag;
    private String category;
    private String instruction;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "cocktail")
    private List<Image> imageList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "COCKTAIL_INGREDIENT_MAPPING",
            joinColumns = @JoinColumn(name = "cocktailId"),
            inverseJoinColumns = @JoinColumn(name = "ingredientId"))
    private List<Ingredient> ingredientList = new ArrayList<>();

    public Cocktail() {
        // No initialization logic needed for this constructor
    }

    public Cocktail(JsonNode jsonNode) {
        this.name = jsonNode.get("strDrink").asText();
        this.tag = jsonNode.get("strTags").asText();
        this.category = jsonNode.get("strAlcoholic").asText();
        this.instruction = jsonNode.get("strInstructions").asText();
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}