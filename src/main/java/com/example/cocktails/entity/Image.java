package com.example.cocktails.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String picture;

  @ManyToOne
  @JoinColumn(name = "cocktailId")
  private Cocktail cocktail;

  public Image() {
    // No initialization logic needed for this constructor
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Cocktail getCocktail() {
    return cocktail;
  }

  public void setCocktail(Cocktail cocktail) {
    this.cocktail = cocktail;
  }
}