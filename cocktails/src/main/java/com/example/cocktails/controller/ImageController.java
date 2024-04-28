
package com.example.cocktails.controller;

import com.example.cocktails.entity.Image;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.exception.ImageAlreadyExistException;
import com.example.cocktails.exception.ImageNotFoundException;
import com.example.cocktails.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/images")
public class ImageController {

  private final ImageService imageService;
  private final Logger log = LoggerFactory.getLogger(ImageController.class);

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  private static final String ERROR_MESSAGE = "Произошла ошибка";

  @PostMapping
  public ResponseEntity<?> addImage(@RequestParam Long cocktailId, @RequestBody Image image) {
    log.info("image post запрос был вызван");
    try {
      imageService.addImage(cocktailId, image);
      log.info("Изображение было успешно добавлено");
      return ResponseEntity.ok("Изображение было успешно сохранено");
    } catch (CocktailNotFoundException | ImageAlreadyExistException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }


  @GetMapping
  public ResponseEntity<?> getImage(@RequestParam Long id) {
    log.info("image get запрос был вызван");
    try {
      log.info("Изображение было успешно получено");
      return ResponseEntity.ok(imageService.getImage(id));
    } catch (ImageNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @PutMapping
  public ResponseEntity<?> updateImage(@RequestParam Long id, @RequestBody Image updatedImage) {
    log.info("image put запрос был вызван");
    try {
      imageService.updateImage(id, updatedImage);
      log.info("Изображение было успешно изменено");
      return ResponseEntity.ok("Изображение было успешно изменено");
    } catch (ImageNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @DeleteMapping
  public ResponseEntity<?> deleteImage(@RequestParam Long id) {
    log.info("image delete запрос был вызван");
    try {
      imageService.deleteImage(id);
      log.info("Изображение было успешно удалено");
      return ResponseEntity.ok("Изображение было успешно удалено");
    } catch (ImageNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }
}
