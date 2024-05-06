package com.example.cocktails.service;

import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.entity.Image;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.exception.ImageAlreadyExistException;
import com.example.cocktails.exception.ImageNotFoundException;
import com.example.cocktails.repository.CocktailRepository;
import com.example.cocktails.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

  @Mock
  private ImageRepository imageRepository;

  @Mock
  private CocktailRepository cocktailRepository;

  @InjectMocks
  private ImageService imageService;

  private Cocktail cocktail;
  private Image image;

  @BeforeEach
  void setUp() {
    cocktail = new Cocktail();
    image = new Image();
  }

  @Test
  void testAddImage_Success() throws CocktailNotFoundException, ImageAlreadyExistException {
    when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
    when(imageRepository.findByPicture(any())).thenReturn(null);

    assertDoesNotThrow(() -> imageService.addImage(1L, image));

    verify(imageRepository, times(1)).save(image);
  }

  @Test
  void testAddImage_cocktailNotFound() {
    when(cocktailRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(CocktailNotFoundException.class, () -> imageService.addImage(1L, image));
  }

  @Test
  void testAddImage_ImageAlreadyExist() {
    Cocktail cocktail = new Cocktail();
    cocktail.setId(1L);
    Image image = new Image();
    image.setPicture("existing_picture");
    image.setCocktail(cocktail);

    when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
    when(imageRepository.findByPicture("existing_picture")).thenReturn(image);

    assertThrows(ImageAlreadyExistException.class, () -> imageService.addImage(1L, image));
  }

  @Test
  void testGetImage_Success() throws ImageNotFoundException {
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
    assertNotNull(imageService.getImage(1L));
  }

  @Test
  void testGetImage_ImageNotFound() {
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ImageNotFoundException.class, () -> imageService.getImage(1L));
  }

  @Test
  void testUpdateImage_Success() throws ImageNotFoundException {
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
    Image updatedImage = new Image();
    updatedImage.setPicture("new_picture");
    assertDoesNotThrow(() -> imageService.updateImage(1L, updatedImage));
    assertEquals("new_picture", image.getPicture());
  }

  @Test
  void testUpdateImage_ImageNotFound() {
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ImageNotFoundException.class, () -> imageService.updateImage(1L, new Image()));
  }

  @Test
  void testDeleteImage_Success() throws ImageNotFoundException {
    Cocktail cocktail = new Cocktail();
    cocktail.setId(1L);
    cocktail.setImageList(new ArrayList<>());

    Image image = new Image();
    image.setId(1L);
    image.setCocktail(cocktail);
    cocktail.getImageList().add(image);

    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

    assertDoesNotThrow(() -> imageService.deleteImage(1L));
    verify(imageRepository, times(1)).deleteById(1L);
  }


  @Test
  void testDeleteImage_ImageNotFound() {
    when(imageRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(1L));
  }
}
