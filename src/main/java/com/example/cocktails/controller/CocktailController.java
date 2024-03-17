package com.example.cocktails.controller;

import com.example.cocktails.dto.CocktailDTO;
import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.exception.CocktailAlreadyExistException;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cocktails")
public class CocktailController {

    private static final String ERROR_MESSAGE = "Ошибка";
    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @PostMapping
    public ResponseEntity<?> addCocktail(@RequestBody Cocktail cocktail) {
        try {
            cocktailService.addCocktail(cocktail);
            return ResponseEntity.ok("Коктейль был добавлен");
        } catch (CocktailAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCocktail(@RequestParam(required = false) String name) {
        try {
            return ResponseEntity.ok(cocktailService.getCocktail(name));
        } catch (CocktailNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/with-ingredient")
    public ResponseEntity<?> getCocktailsWithIngredient(@RequestParam Long ingredientId) {
        try {
            List<CocktailDTO> cocktails = cocktailService.getCocktailesWithIngredient(ingredientId);
            return ResponseEntity.ok(cocktails);
        } catch (CocktailNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/name")
    public ResponseEntity<?> getCocktailByName(@RequestParam String name) {
        try {
            List<CocktailDTO> cocktailDTOS = cocktailService.getByName(name);
            return ResponseEntity.ok(cocktailDTOS);
        } catch (CocktailNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCocktail(@RequestParam String name, @RequestBody Cocktail updatedCocktail) {
        try {
            cocktailService.updateCocktail(name, updatedCocktail);
            return ResponseEntity.ok("Блюдо было успешно изменено");
        } catch (CocktailNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCocktail(@RequestParam Long id) {
        try {
            cocktailService.deleteCocktail(id);
            return ResponseEntity.ok("Коктейль удалён");
        } catch (CocktailNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }
}