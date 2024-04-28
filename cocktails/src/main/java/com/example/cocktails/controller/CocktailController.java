package com.example.cocktails.controller;

import com.example.cocktails.dto.CocktailDto;
import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.exception.CocktailAlreadyExistException;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.service.CocktailService;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/cocktails")
public class CocktailController {

  private static final String ERROR_MESSAGE = "Ошибка";
  private static final String GETTING_SUCCESS_MESSAGE = "Коктейль был успешно получен";
  private final Logger log = LoggerFactory.getLogger(CocktailController.class);
  private final CocktailService cocktailService;

  public CocktailController(CocktailService cocktailService) {
    this.cocktailService = cocktailService;
  }

  @PostMapping
  public ResponseEntity<?> addCocktail(@RequestBody Cocktail cocktail) {
    log.info("cocktail post запрос был вызван");
    try {
      cocktailService.addCocktail(cocktail);
      log.info("коктейль был успешно добавлен");
      return ResponseEntity.ok("Коктейль был добавлен");
    } catch (CocktailAlreadyExistException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/bulk")
  public ResponseEntity<List<String>> addCocktailsBulk(@RequestBody List<Cocktail> cocktails) {
    log.info("cocktail bulk post запрос был вызван");
    try {
      List<String> errors = cocktailService.addCocktailsBulk(cocktails);
      if (!errors.isEmpty()) {
        return ResponseEntity.badRequest().body(errors);
      }
      log.info("Коктейли были успешно добавлены");
      return ResponseEntity.ok(Collections.singletonList("Коктейли были успешно добавлены"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Collections.singletonList(ERROR_MESSAGE));
    }
  }

  @GetMapping
  public ResponseEntity<?> getCocktail(@RequestParam(required = false) String name) {
    log.info("cocktail get запрос был вызван");
    try {
      log.info(GETTING_SUCCESS_MESSAGE);
      return ResponseEntity.ok(cocktailService.getCocktail(name));
    } catch (CocktailNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/with-ingredient")
  public ResponseEntity<?> getCocktailsWithIngredient(@RequestParam Long ingredientId) {
    log.info("cocktail get /with-ingredient запрос был вызван");
    try {
      List<CocktailDto> cocktails = cocktailService.getCocktailesWithIngredient(ingredientId);
      log.info(GETTING_SUCCESS_MESSAGE);
      return ResponseEntity.ok(cocktails);
    } catch (CocktailNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @GetMapping("/name")
  public ResponseEntity<?> getCocktailByName(@RequestParam String name) {
    log.info("cocktail get /name запрос был вызван");
    try {
      List<CocktailDto> cocktailDtos = cocktailService.getByName(name);
      log.info(GETTING_SUCCESS_MESSAGE);
      return ResponseEntity.ok(cocktailDtos);
    } catch (CocktailNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping
  public ResponseEntity<?> updateCocktail(@RequestParam String name,
                                          @RequestBody Cocktail updatedCocktail) {
    log.info("cocktail put запрос был вызван");
    try {
      cocktailService.updateCocktail(name, updatedCocktail);
      log.info("Коктейль был успешно изменён");
      return ResponseEntity.ok("Коктейль был изменён");
    } catch (CocktailNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @DeleteMapping
  public ResponseEntity<?> deleteCocktail(@RequestParam Long id) {
    log.info("cocktail delete запрос был вызван");
    try {
      cocktailService.deleteCocktail(id);
      log.info("Коктейль был успешно удален");
      return ResponseEntity.ok("Коктейль удалён");
    } catch (CocktailNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }
}