package com.example.cocktails.repository;

import com.example.cocktails.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
  Ingredient findByName(String name);
}