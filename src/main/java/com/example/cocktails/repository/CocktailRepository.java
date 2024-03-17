package com.example.cocktails.repository;

import com.example.cocktails.entity.Cocktail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CocktailRepository extends CrudRepository<Cocktail, Long> {
    Cocktail findByName(String name);

    @Query("SELECT d FROM Cocktail d JOIN d.ingredientList i WHERE i.id = :ingredientId")
    List<Cocktail> findCocktailsByIngredientList_Id(Long ingredientId);
}