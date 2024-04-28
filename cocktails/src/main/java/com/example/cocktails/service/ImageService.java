package com.example.cocktails.service;

import com.example.cocktails.dto.ImageDto;
import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.entity.Image;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.exception.ImageAlreadyExistException;
import com.example.cocktails.exception.ImageNotFoundException;
import com.example.cocktails.repository.CocktailRepository;
import com.example.cocktails.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ImageService {

  private final ImageRepository imageRepository;
  private final CocktailRepository cocktailRepository;
  private static final String IMAGE_NOT_FOUND_STRING = "Изображение не найдено";

  @Autowired
  public ImageService(ImageRepository imageRepository, CocktailRepository cocktailRepository) {
    this.imageRepository = imageRepository;
    this.cocktailRepository = cocktailRepository;
  }

  public void addImage(Long id, Image image)
      throws CocktailNotFoundException, ImageAlreadyExistException {
    Cocktail cocktail = cocktailRepository.findById(id).orElse(null);
    if (cocktail != null) {
      image.setCocktail(cocktail);
      if (imageRepository.findByPicture(image.getPicture()) != null) {
        throw new ImageAlreadyExistException("Такое изображение уже существует");
      }
      imageRepository.save(image);
    } else {
      throw new CocktailNotFoundException("Не удалось добавить изображение. Блюдо не найдено");
    }
  }

  public ImageDto getImage(Long id) throws ImageNotFoundException {
    Image image = imageRepository.findById(id).orElse(null);
    if (image != null) {
      return ImageDto.toModel(image);
    } else {
      throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
    }
  }

  public void updateImage(Long id, Image image) throws ImageNotFoundException {
    Image imageEntity = imageRepository.findById(id).orElse(null);
    if (imageEntity != null) {
      imageEntity.setPicture(image.getPicture());
      imageRepository.save(imageEntity);
    } else {
      throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
    }
  }

  @Transactional
  public void deleteImage(Long id) throws ImageNotFoundException {
    Image image = imageRepository.findById(id).orElse(null);
    if (image != null) {
      image.getCocktail().getImageList().remove(image);
      cocktailRepository.save(image.getCocktail());
      imageRepository.deleteById(id);
    } else {
      throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
    }
  }
}