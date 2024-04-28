/*
package com.example.cocktails.service;

import com.example.cocktailes.entity.cocktail;
import com.example.cocktailes.entity.Image;
import com.example.cocktailes.exception.cocktailNotFoundException;
import com.example.cocktailes.exception.ImageAlreadyExistException;
import com.example.cocktailes.exception.ImageNotFoundException;
import com.example.cocktailes.repository.cocktailRepository;
import com.example.cocktailes.repository.ImageRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageServiceTest {

  @Mock
  private ImageRepository imageRepository;

  @Mock
  private cocktailRepository cocktailRepository;

  @InjectMocks
  private ImageService imageService;

  private cocktail cocktail;
  private Image image;

  @BeforeEach
  void setUp() {
    cocktail = new cocktail();
    image = new Image();
  }

  @Test
  void testAddImage_Success() throws cocktailNotFoundException, ImageAlreadyExistException {
    when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
    when(imageRepository.findByPicture(anyString())).thenReturn(null);

    assertDoesNotThrow(() -> imageService.addImage(1L, image));

    verify(imageRepository, times(1)).save(image);
  }

  @Test
  void testAddImage_cocktailNotFound() {
    assertThrows(cocktailNotFoundException.class, () -> imageService.addImage(1L, image));
  }

  @Test
  void testAddImage_ImageAlreadyExist() {
    cocktail cocktail = new cocktail();
    cocktail.setId(1L);
    Image image = new Image();
    image.setPicture("existing_picture");
    image.setcocktail(cocktail);

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
    assertThrows(ImageNotFoundException.class, () -> imageService.updateImage(1L, new Image()));
  }

  @Test
  void testDeleteImage_Success() throws ImageNotFoundException {
    cocktail cocktail = new cocktail();
    cocktail.setId(1L);
    cocktail.setImageList(new ArrayList<>());

    Image image = new Image();
    image.setId(1L);
    image.setcocktail(cocktail);
    cocktail.getImageList().add(image);

    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

    assertDoesNotThrow(() -> imageService.deleteImage(1L));
    verify(imageRepository, times(1)).deleteById(1L);
  }


  @Test
  void testDeleteImage_ImageNotFound() {
    assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(1L));
  }
}
*/
