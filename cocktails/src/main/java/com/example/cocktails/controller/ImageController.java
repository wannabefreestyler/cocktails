
package com.example.cocktails.controller;

import com.example.cocktails.entity.Image;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.exception.ImageAlreadyExistException;
import com.example.cocktails.exception.ImageNotFoundException;
import com.example.cocktails.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    private static final String ERROR_MESSAGE = "Произошла ошибка";

    @PostMapping
    public ResponseEntity<?> addImage(@RequestParam Long cocktailId, @RequestBody Image image) {
        try {
            imageService.addImage(cocktailId, image);
            return ResponseEntity.ok("Изображение было успешно сохранено");
        } catch (CocktailNotFoundException | ImageAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }


    @GetMapping
    public ResponseEntity<?> getImage(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(imageService.getImage(id));
        } catch (ImageNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateImage(@RequestParam Long id, @RequestBody Image updatedImage) {
        try {
            imageService.updateImage(id, updatedImage);
            return ResponseEntity.ok("Изображение было успешно изменено");
        } catch (ImageNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImage(@RequestParam Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.ok("Изображение было успешно удалено");
        } catch (ImageNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }
}
