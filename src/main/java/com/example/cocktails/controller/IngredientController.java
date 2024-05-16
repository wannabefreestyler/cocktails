package com.example.cocktails.controller;

import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.entity.Ingredient;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.exception.IngredientAlreadyExistException;
import com.example.cocktails.exception.IngredientNotFoundException;
import com.example.cocktails.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

  private final IngredientService ingredientService;

  public IngredientController(IngredientService ingredientService) {
    this.ingredientService = ingredientService;
  }

  private static final String ERROR_MESSAGE = "Произошла ошибка";
  private final Logger log = LoggerFactory.getLogger(IngredientController.class);

  @PostMapping
  @CrossOrigin
  public ResponseEntity<?> addIngredient(@RequestParam Long cocktailId,
                                         @RequestBody Ingredient ingredient) {
    log.info("ingredient post запрос был вызван");
    try {
      ingredientService.addIngredient(cocktailId, ingredient);
      log.info("Ингредиент был успешно добавлен");
      return ResponseEntity.ok("Ингредиент был успешно сохранен");
    } catch (CocktailNotFoundException | IngredientAlreadyExistException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @GetMapping
  @CrossOrigin
  public ResponseEntity<?> getIngredient(@RequestParam Long id) {
    log.info("ingredient get запрос был вызван");
    try {
      log.info("Ингредиент был успешно получен");
      return ResponseEntity.ok(ingredientService.getIngredient(id));
    } catch (IngredientNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @PutMapping
  @CrossOrigin
  public ResponseEntity<?> updateIngredient(@RequestParam Long id,
                                            @RequestBody Ingredient updatedIngredient) {
    log.info("ingredient put запрос был вызван");
    try {
      ingredientService.updateIngredient(id, updatedIngredient);
      log.info("Ингредиент был успешно изменен");
      return ResponseEntity.ok("Ингредиент был успешно изменен");
    } catch (IngredientNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @DeleteMapping
  @CrossOrigin
  public ResponseEntity<?> deleteIngredient(@RequestParam Long cocktailId,
                                            @RequestParam Long ingredientId) {
    log.info("ingredient delete запрос был вызван");
    try {
      ingredientService.deleteIngredient(cocktailId, ingredientId);
      log.info("Ингредиент был успешно удален");
      return ResponseEntity.ok("Ингредиент был успешно удален");
    } catch (IngredientNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }
}