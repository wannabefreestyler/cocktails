/*
package com.example.cocktailes.service;

import com.example.cocktailes.entity.cocktail;
import com.example.cocktailes.entity.Ingredient;
import com.example.cocktailes.exception.cocktailNotFoundException;
import com.example.cocktailes.exception.IngredientAlreadyExistException;
import com.example.cocktailes.exception.IngredientNotFoundException;
import com.example.cocktailes.repository.cocktailRepository;
import com.example.cocktailes.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class IngredientServiceTest {

  @Mock
  private IngredientRepository ingredientRepository;

  @Mock
  private cocktailRepository cocktailRepository;

  @InjectMocks
  private IngredientService ingredientService;

  private cocktail cocktail;
  private Ingredient ingredient;

  @BeforeEach
  void setUp() {
    cocktail = new cocktail();
    ingredient = new Ingredient();
  }

  @Test
  void testAddIngredient_Success() throws cocktailNotFoundException, IngredientAlreadyExistException {
    when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
    when(ingredientRepository.findByName(anyString())).thenReturn(null);

    assertDoesNotThrow(() -> ingredientService.addIngredient(1L, ingredient));

    verify(ingredientRepository, times(1)).save(ingredient);
  }

  @Test
  void testAddIngredient_cocktailNotFound() {
    assertThrows(cocktailNotFoundException.class, () -> ingredientService.addIngredient(1L, ingredient));
  }

  @Test
  void testAddIngredient_IngredientAlreadyExist() {
    Ingredient existingIngredient = new Ingredient();
    existingIngredient.setName("Salt");
    cocktail.getIngredientList().add(existingIngredient);

    when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
    when(ingredientRepository.findByName(existingIngredient.getName())).thenReturn(existingIngredient);

    assertThrows(IngredientAlreadyExistException.class, () -> ingredientService.addIngredient(1L, existingIngredient));
  }


  @Test
  void testGetIngredient_Success() throws IngredientNotFoundException {
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
    assertNotNull(ingredientService.getIngredient(1L));
  }

  @Test
  void testGetIngredient_IngredientNotFound() {
    assertThrows(IngredientNotFoundException.class, () -> ingredientService.getIngredient(1L));
  }

  @Test
  void testUpdateIngredient_Success() throws IngredientNotFoundException {
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
    Ingredient updatedIngredient = new Ingredient();
    updatedIngredient.setName("new_name");
    assertDoesNotThrow(() -> ingredientService.updateIngredient(1L, updatedIngredient));
    assertEquals("new_name", ingredient.getName());
  }

  @Test
  void testUpdateIngredient_IngredientNotFound() {
    assertThrows(IngredientNotFoundException.class, () -> ingredientService.updateIngredient(1L, new Ingredient()));
  }

  @Test
  void testDeleteIngredient_Success() throws IngredientNotFoundException, cocktailNotFoundException {
    cocktail cocktail = new cocktail();
    cocktail.setId(1L);
    Ingredient ingredient = new Ingredient();
    ingredient.setId(1L);
    cocktail.getIngredientList().add(ingredient);

    when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

    ingredientService.deleteIngredient(1L, 1L);

    verify(cocktailRepository, times(1)).save(cocktail);
    verify(ingredientRepository, times(1)).save(ingredient);
    verify(ingredientRepository, never()).deleteById(anyLong());
    assertThat(cocktail.getIngredientList()).doesNotContain(ingredient);
  }

  @Test
  void testDeleteIngredient_IngredientNotFound() {
    assertThrows(IngredientNotFoundException.class, () -> ingredientService.deleteIngredient(1L, 1L));
  }

  @Test
  void testDeleteIngredient_cocktailNotFound() {
    cocktail cocktail = new cocktail();
    cocktail.setId(1L);
    when(cocktailRepository.findById(1L)).thenReturn(Optional.empty());
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(new Ingredient()));

    assertThrows(cocktailNotFoundException.class, () -> ingredientService.deleteIngredient(1L, 1L));
  }
}

*/
