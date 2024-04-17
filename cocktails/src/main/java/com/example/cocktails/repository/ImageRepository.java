package com.example.cocktails.repository;

import com.example.cocktails.entity.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
    Image findByPicture(String picture);
}