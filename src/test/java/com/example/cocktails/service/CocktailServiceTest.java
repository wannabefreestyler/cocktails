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

class CocktailServiceTest {

  @Mock
  private CocktailRepository cocktailRepository;

  @Mock
  private Cache<String, CocktailDto> cocktailCache;

  @InjectMocks
  private CocktailService cocktailService;
  private List<Cocktail> cocktails;
  private com.example.cocktails.entity.Cocktail Cocktail;

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
    List<String> errors = cocktailService.addCocktailsBulk(cocktailes);
    assertEquals(1, errors.size());
    assertEquals("Коктейль 'Existing cocktail' уже существует", errors.get(0));

    // Verify repository save method is called only once
    verify(cocktailRepository, times(1)).save(newcocktail);
  }


  @Test
  void testGetСocktail() {
    String cocktailName = "Test cocktail";
    Cocktail cocktail = new Cocktail();
    cocktail.setName(cocktailName);

    CocktailDto cocktailDTO = new CocktailDto();
    cocktailDTO.setName(cocktailName);

    when(cocktailCache.containsKey(cocktailName)).thenReturn(true);
    when(cocktailCache.get(cocktailName)).thenReturn(cocktailDTO);

    assertDoesNotThrow(() -> cocktailService.getCocktail(cocktailName));

    when(cocktailCache.containsKey(cocktailName)).thenReturn(false);
    when(cocktailRepository.findByName(cocktailName)).thenReturn(cocktail);

    assertDoesNotThrow(() -> {
      try {
        cocktailService.getCocktail(cocktailName);
      } catch (CocktailNotFoundException e) {
        fail("cocktailNotFoundException should not be thrown when cocktail exists");
      }
    });

    when(cocktailCache.containsKey(cocktailName)).thenReturn(false);
    when(cocktailRepository.findByName(cocktailName)).thenReturn(null);

    assertThrows(CocktailNotFoundException.class, () -> cocktailService.getCocktail(cocktailName));
  }

  @Test
  void testGetcocktail_WhenCacheIsEmptyAndcocktailFoundInRepository() throws CocktailNotFoundException {
    String name = "Test cocktail";
    Cocktail cocktail = new Cocktail();
    cocktail.setName(name);
    cocktail.setCategory("Test Category");
    cocktail.setInstruction("Test Instruction");

    when(cocktailCache.containsKey(name)).thenReturn(false);
    when(cocktailRepository.findByName(name)).thenReturn(cocktail);

    CocktailDto result = cocktailService.getCocktail(name);

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

    assertDoesNotThrow(() -> cocktailService.getCocktailesWithIngredient(ingredientId));

    when(cocktailCache.containsKey(cacheKey)).thenReturn(false);
    when(cocktailRepository.findCocktailsByIngredientList_Id(ingredientId)).thenReturn(new ArrayList<>());

    assertThrows(CocktailNotFoundException.class, () -> cocktailService.getCocktailesWithIngredient(ingredientId));
  }

  @Test
  void testGetcocktailesWithIngredient_WhencocktailesExist() throws CocktailNotFoundException {
    Long ingredientId = 1L;
    String cacheKey = "ingredient_" + ingredientId;
    List<Cocktail> cocktailes = new ArrayList<>();
    Cocktail cocktail1 = new Cocktail();
    cocktail1.setName("cocktail 1");
    Cocktail cocktail2 = new Cocktail();
    cocktail2.setName("cocktail 2");
    cocktailes.add(cocktail1);
    cocktailes.add(cocktail2);

    when(cocktailCache.containsKey(cacheKey)).thenReturn(false);
    when(cocktailRepository.findCocktailsByIngredientList_Id(ingredientId)).thenReturn(cocktailes);

    List<CocktailDto> cocktailDTOs = cocktailService.getCocktailesWithIngredient(ingredientId);

    assertEquals(2, cocktailDTOs.size());
    assertEquals("cocktail 1", cocktailDTOs.get(0).getName());
    assertEquals("cocktail 2", cocktailDTOs.get(1).getName());

    verify(cocktailCache, times(1)).putList(cacheKey, cocktailDTOs);
  }

  @Test
  void testGetcocktailesWithIngredient_WhenCacheIsEmptyAndcocktailesFoundInRepository() throws CocktailNotFoundException {
    Long ingredientId = 1L;
    String cacheKey = "ingredient_" + ingredientId;
    List<Cocktail> cocktailes = new ArrayList<>();
    Cocktail cocktail1 = new Cocktail();
    cocktail1.setName("cocktail 1");
    Cocktail cocktail2 = new Cocktail();
    cocktail2.setName("cocktail 2");
    cocktailes.add(cocktail1);
    cocktailes.add(cocktail2);

    when(cocktailCache.containsKey(cacheKey)).thenReturn(false);
    when(cocktailRepository.findCocktailsByIngredientList_Id(ingredientId)).thenReturn(List.of(cocktail1, cocktail2));


    List<CocktailDto> cocktailDTOs = cocktailService.getCocktailesWithIngredient(ingredientId);

    assertEquals(2, cocktailDTOs.size());
    assertEquals("cocktail 1", cocktailDTOs.get(0).getName());
    assertEquals("cocktail 2", cocktailDTOs.get(1).getName());

    verify(cocktailCache, times(1)).putList(cacheKey, cocktailDTOs);
  }

  @Test
  void testGetByName() throws CocktailNotFoundException {
    String name = "Test Name";

    when(cocktailCache.containsKey(name)).thenReturn(true);

    assertDoesNotThrow(() -> cocktailService.getByName(name));

    when(cocktailCache.containsKey(name)).thenReturn(false);
    when(cocktailRepository.findByName(name)).thenReturn(null);

    assertThrows(CocktailNotFoundException.class, () -> cocktailService.getByName(name));
  }

  @Test
  void testUpdatecocktail() throws CocktailNotFoundException {
    String name = "Test Name";
    Cocktail cocktail = new Cocktail();
    cocktail.setName(name);

    when(cocktailRepository.findByName(name)).thenReturn(cocktail);

    assertDoesNotThrow(() -> cocktailService.updateCocktail(name, cocktail));

    when(cocktailRepository.findByName(name)).thenReturn(null);

    assertThrows(CocktailNotFoundException.class, () -> cocktailService.updateCocktail(name, cocktail));
  }

  @Test
  void testDeletecocktail() throws CocktailNotFoundException {
    Long id = 123L;
    Cocktail cocktail = new Cocktail();
    cocktail.setId(id);

    when(cocktailRepository.findById(id)).thenReturn(java.util.Optional.of(cocktail));

    //assertDoesNotThrow(() -> cocktailService.deleteCocktail(name));

    when(cocktailRepository.findById(id)).thenReturn(java.util.Optional.empty());

    //assertThrows(CocktailNotFoundException.class, () -> cocktailService.deleteCocktail(name));
  }
}
