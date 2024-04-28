package com.example.cocktails.service;

import com.example.cocktails.dto.IngredientDto;
import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.entity.Ingredient;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.exception.IngredientAlreadyExistException;
import com.example.cocktails.exception.IngredientNotFoundException;
import com.example.cocktails.repository.CocktailRepository;
import com.example.cocktails.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IngredientService {

  private final IngredientRepository ingredientRepository;
  private final CocktailRepository cocktailRepository;
  private static final String INGREDIENT_NOT_FOUND_STRING = "Ингредиент не найден";

  @Autowired
  public IngredientService(IngredientRepository ingredientRepository,
                           CocktailRepository cocktailRepository) {
    this.ingredientRepository = ingredientRepository;
    this.cocktailRepository = cocktailRepository;
  }

  @Transactional
  public void addIngredient(Long cocktailId, Ingredient ingredient)
      throws CocktailNotFoundException, IngredientAlreadyExistException {
    Cocktail cocktail = cocktailRepository.findById(cocktailId).orElse(null);
    if (cocktail == null) {
      throw new CocktailNotFoundException("Не удалось добавить ингредиент. Блюдо не найдено");
    }

    if (cocktail.getIngredientList().stream().anyMatch(
        existingIngredient -> existingIngredient.getName().equals(ingredient.getName()))) {
      throw new IngredientAlreadyExistException("Ингредиент уже есть в данном блюде");
    }

    Ingredient existingIngredient = ingredientRepository.findByName(ingredient.getName());

    if (existingIngredient != null) {
      cocktail.getIngredientList().add(existingIngredient);
    } else {
      ingredientRepository.save(ingredient);
      cocktail.getIngredientList().add(ingredient);
    }

    cocktailRepository.save(cocktail);
  }

  public IngredientDto getIngredient(Long id) throws IngredientNotFoundException {
    Ingredient ingredient = ingredientRepository.findById(id).orElse(null);
    if (ingredient != null) {
      return IngredientDto.toModel(ingredient);
    } else {
      throw new IngredientNotFoundException(INGREDIENT_NOT_FOUND_STRING);
    }
  }

  public void updateIngredient(Long id, Ingredient ingredient) throws IngredientNotFoundException {
    Ingredient ingredientEntity = ingredientRepository.findById(id).orElse(null);
    if (ingredientEntity != null) {
      ingredientEntity.setName(ingredient.getName());
      ingredientRepository.save(ingredientEntity);
    } else {
      throw new IngredientNotFoundException(INGREDIENT_NOT_FOUND_STRING);
    }
  }

  @Transactional
  public void deleteIngredient(Long cocktailId, Long ingredientId)
      throws IngredientNotFoundException, CocktailNotFoundException {
    Cocktail cocktail = cocktailRepository.findById(cocktailId).orElse(null);
    if (cocktail == null) {
      throw new CocktailNotFoundException("Блюдо не найдено");
    }

    Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
    if (ingredient == null) {
      throw new IngredientNotFoundException(INGREDIENT_NOT_FOUND_STRING);
    }

    cocktail.getIngredientList().remove(ingredient);
    cocktailRepository.save(cocktail);
    ingredient.getCocktailList().remove(cocktail);
    ingredientRepository.save(ingredient);
  }
}