/*
package com.example.cocktails.service;

import com.example.cocktails.component.Cache;
import com.example.cocktails.dto.CocktailDto;
import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.exception.CocktailAlreadyExistException;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.repository.CocktailRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class СocktailServiceTest {

  @Mock
  private CocktailRepository cocktailRepository;

  @Mock
  private Cache<String, CocktailDto> cocktailCache;

  @InjectMocks
  private CocktailService cocktailService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testAddcocktail() {
    Cocktail cocktail = new Cocktail();
    cocktail.setName("Test cocktail");
    cocktail.setCategory("Test Category");
    cocktail.setInstruction("Test Instruction");

    when(cocktailRepository.findByName("Test cocktail")).thenReturn(null);

    assertDoesNotThrow(() -> cocktailService.addCocktail(cocktail));

    when(cocktailRepository.findByName("Test cocktail")).thenReturn(cocktail);

    assertThrows(CocktailAlreadyExistException.class, () -> cocktailService.addCocktail(cocktail));
  }

  @Test
  void testAddcocktailesBulk() {
    // Prepare cocktailes
    List<Cocktail> cocktailes = new ArrayList<>();
    Cocktail existingcocktail = new Cocktail();
    existingcocktail.setName("Existing cocktail");
    Cocktail newcocktail = new Cocktail();
    newcocktail.setName("New cocktail");
    cocktailes.add(existingcocktail);
    cocktailes.add(newcocktail);

    // Mock repository behavior
    when(cocktailRepository.findByName("Existing cocktail")).thenReturn(existingcocktail);
    when(cocktailRepository.findByName("New cocktail")).thenReturn(null);

    // Call the method and assert the result
    List<String> errors = cocktailService.addСocktailesBulk(cocktailes);
    assertEquals(1, errors.size());
    assertEquals("Блюдо 'Existing cocktail' уже существует", errors.get(0));

    // Verify repository save method is called only once
    verify(cocktailRepository, times(1)).save(newcocktail);
  }


  @Test
  void testGetСocktail() {
    String cocktailName = "Test cocktail";
    Сocktail cocktail = new Сocktail();
    cocktail.setName(cocktailName);

    СocktailDTO cocktailDTO = new СocktailDTO();
    cocktailDTO.setName(cocktailName);

    when(cocktailCache.containsKey(cocktailName)).thenReturn(true);
    when(cocktailCache.get(cocktailName)).thenReturn(СocktailDTO);

    assertDoesNotThrow(() -> cocktailService.getСocktail(cocktailName));

    when(cocktailCache.containsKey(cocktailName)).thenReturn(false);
    when(cocktailRepository.findByName(cocktailName)).thenReturn(Сocktail);

    assertDoesNotThrow(() -> {
      try {
        cocktailService.getСocktail(cocktailName);
      } catch (СocktailNotFoundException e) {
        fail("cocktailNotFoundException should not be thrown when cocktail exists");
      }
    });

    when(cocktailCache.containsKey(cocktailName)).thenReturn(false);
    when(cocktailRepository.findByName(cocktailName)).thenReturn(null);

    assertThrows(СocktailNotFoundException.class, () -> cocktailService.getСocktail(cocktailName));
  }

  @Test
  void testGetcocktail_WhenCacheIsEmptyAndcocktailFoundInRepository() throws СocktailNotFoundException {
    String name = "Test cocktail";
    Сocktail cocktail = new Сocktail();
    cocktail.setName(name);
    cocktail.setCategory("Test Category");
    cocktail.setInstruction("Test Instruction");

    when(cocktailCache.containsKey(name)).thenReturn(false);
    when(cocktailRepository.findByName(name)).thenReturn(Сocktail);

    СocktailDTO result = cocktailService.getСocktail(name);

    assertNotNull(result);
    assertEquals(name, result.getName());
    assertEquals("Test Category", result.getCategory());
    assertEquals("Test Instruction", result.getInstruction());

    verify(cocktailCache, times(1)).put(name, result);
  }

  @Test
  void testGetcocktailesWithIngredient() {
    Long ingredientId = 123L;
    String cacheKey = "ingredient_" + ingredientId;

    when(cocktailCache.containsKey(cacheKey)).thenReturn(true);

    assertDoesNotThrow(() -> cocktailService.getcocktailesWithIngredient(ingredientId));

    when(cocktailCache.containsKey(cacheKey)).thenReturn(false);
    when(cocktailRepository.findcocktailesByIngredientList_Id(ingredientId)).thenReturn(new ArrayList<>());

    assertThrows(cocktailNotFoundException.class, () -> cocktailService.getcocktailesWithIngredient(ingredientId));
  }

  @Test
  void testGetcocktailesWithIngredient_WhencocktailesExist() throws cocktailNotFoundException {
    Long ingredientId = 1L;
    String cacheKey = "ingredient_" + ingredientId;
    List<cocktail> cocktailes = new ArrayList<>();
    cocktail cocktail1 = new cocktail();
    cocktail1.setName("cocktail 1");
    cocktail cocktail2 = new cocktail();
    cocktail2.setName("cocktail 2");
    cocktailes.add(cocktail1);
    cocktailes.add(cocktail2);

    when(cocktailCache.containsKey(cacheKey)).thenReturn(false);
    when(cocktailRepository.findcocktailesByIngredientList_Id(ingredientId)).thenReturn(cocktailes);

    List<cocktailDTO> cocktailDTOs = cocktailService.getcocktailesWithIngredient(ingredientId);

    assertEquals(2, cocktailDTOs.size());
    assertEquals("cocktail 1", cocktailDTOs.get(0).getName());
    assertEquals("cocktail 2", cocktailDTOs.get(1).getName());

    verify(cocktailCache, times(1)).putList(cacheKey, cocktailDTOs);
  }

  @Test
  void testGetcocktailesWithIngredient_WhenCacheIsEmptyAndcocktailesFoundInRepository() throws cocktailNotFoundException {
    Long ingredientId = 1L;
    String cacheKey = "ingredient_" + ingredientId;
    List<cocktail> cocktailes = new ArrayList<>();
    cocktail cocktail1 = new cocktail();
    cocktail1.setName("cocktail 1");
    cocktail cocktail2 = new cocktail();
    cocktail2.setName("cocktail 2");
    cocktailes.add(cocktail1);
    cocktailes.add(cocktail2);

    when(cocktailCache.containsKey(cacheKey)).thenReturn(false);
    when(cocktailRepository.findcocktailesByIngredientList_Id(ingredientId)).thenReturn(cocktailes);

    List<cocktailDTO> cocktailDTOs = cocktailService.getcocktailesWithIngredient(ingredientId);

    assertEquals(2, cocktailDTOs.size());
    assertEquals("cocktail 1", cocktailDTOs.get(0).getName());
    assertEquals("cocktail 2", cocktailDTOs.get(1).getName());

    verify(cocktailCache, times(1)).putList(cacheKey, cocktailDTOs);
  }

  @Test
  void testGetByName() throws cocktailNotFoundException {
    String name = "Test Name";

    when(cocktailCache.containsKey(name)).thenReturn(true);

    assertDoesNotThrow(() -> cocktailService.getByName(name));

    when(cocktailCache.containsKey(name)).thenReturn(false);
    when(cocktailRepository.findByName(name)).thenReturn(null);

    assertThrows(cocktailNotFoundException.class, () -> cocktailService.getByName(name));
  }

  @Test
  void testUpdatecocktail() throws cocktailNotFoundException {
    String name = "Test Name";
    cocktail cocktail = new cocktail();
    cocktail.setName(name);

    when(cocktailRepository.findByName(name)).thenReturn(cocktail);

    assertDoesNotThrow(() -> cocktailService.updatecocktail(name, cocktail));

    when(cocktailRepository.findByName(name)).thenReturn(null);

    assertThrows(cocktailNotFoundException.class, () -> cocktailService.updatecocktail(name, cocktail));
  }

  @Test
  void testDeletecocktail() throws cocktailNotFoundException {
    Long id = 123L;
    cocktail cocktail = new cocktail();
    cocktail.setId(id);

    when(cocktailRepository.findById(id)).thenReturn(java.util.Optional.of(cocktail));

    assertDoesNotThrow(() -> cocktailService.deletecocktail(id));

    when(cocktailRepository.findById(id)).thenReturn(java.util.Optional.empty());

    assertThrows(cocktailNotFoundException.class, () -> cocktailService.deletecocktail(id));
  }
}*/
