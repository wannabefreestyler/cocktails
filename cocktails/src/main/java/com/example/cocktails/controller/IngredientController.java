package com.example.cocktails.controller;

import com.example.cocktails.entity.Ingredient;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.exception.IngredientAlreadyExistException;
import com.example.cocktails.exception.IngredientNotFoundException;
import com.example.cocktails.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    private static final String ERROR_MESSAGE = "Произошла ошибка";

    @PostMapping
    public ResponseEntity<?> addIngredient(@RequestParam Long cocktailId, @RequestBody Ingredient ingredient) {
        try {
            ingredientService.addIngredient(cocktailId, ingredient);
            return ResponseEntity.ok("Ингредиент был успешно сохранен");
        } catch (CocktailNotFoundException | IngredientAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping
    public ResponseEntity<?> getIngredient(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(ingredientService.getIngredient(id));
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateIngredient(@RequestParam Long id, @RequestBody Ingredient updatedIngredient) {
        try {
            ingredientService.updateIngredient(id, updatedIngredient);
            return ResponseEntity.ok("Ингредиент был успешно изменен");
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteIngredient(@RequestParam Long cocktailId, @RequestParam Long ingredientId) {
        try {
            ingredientService.deleteIngredient(cocktailId, ingredientId);
            return ResponseEntity.ok("Ингредиент был успешно удален");
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }
}