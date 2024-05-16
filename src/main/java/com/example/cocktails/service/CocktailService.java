package com.example.cocktails.service;

import com.example.cocktails.component.Cache;
import com.example.cocktails.dto.CocktailDto;
import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.exception.CocktailAlreadyExistException;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.repository.CocktailRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Component
public class CocktailService {

  private final ObjectMapper objectMapper;
  private final CocktailRepository cocktailRepository;
  private final Cache<String, CocktailDto> cocktailCache;
  private static final String DRINKS_STRING = "drinks";
  private static final String COCKTAIL_NOT_FOUND_STRING = "такого коктейля нет ;(";

  @Autowired
  public CocktailService(ObjectMapper objectMapper, CocktailRepository cocktailRepository,
                         Cache<String, CocktailDto> cocktailCache) {
    this.objectMapper = objectMapper;
    this.cocktailRepository = cocktailRepository;
    this.cocktailCache = cocktailCache;
  }

  public void addCocktail(Cocktail cocktail) throws CocktailAlreadyExistException {
    if (cocktailRepository.findByName(cocktail.getName()) != null) {
      throw new CocktailAlreadyExistException("такой коктейль уже существует");
    }
    cocktailRepository.save(cocktail);
  }

  public List<String> addCocktailsBulk(List<Cocktail> cocktails) {
    List<String> errors = new ArrayList<>();

    cocktails.stream()
        .filter(cocktail -> cocktailRepository.findByName(cocktail.getName()) != null)
        .forEach(cocktail -> errors.add("Коктейль '" + cocktail.getName() + "' уже существует"));

    cocktails.stream()
        .filter(cocktail -> cocktailRepository.findByName(cocktail.getName()) == null)
        .forEach(cocktail -> cocktailRepository.save(cocktail));

    return errors;
  }

  public CocktailDto getCocktail(String name) throws CocktailNotFoundException {
    if (cocktailCache.containsKey(name)) {
      return cocktailCache.get(name);
    } else {
      Cocktail cocktail = cocktailRepository.findByName(name);
      if (cocktail == null) {
        throw new CocktailNotFoundException(COCKTAIL_NOT_FOUND_STRING);
      }
      CocktailDto cocktailDto = CocktailDto.toModel(cocktail);
      cocktailCache.put(name, cocktailDto);
      return cocktailDto;
    }
  }

  public List<CocktailDto> getCocktailesWithIngredient(Long ingredientId)
      throws CocktailNotFoundException {
    String cacheKey = "ingredient_" + ingredientId;
    if (cocktailCache.containsKey(cacheKey)) {
      return cocktailCache.getList(cacheKey);
    } else {
      List<Cocktail> cocktails = cocktailRepository.findCocktailsByIngredientList_Id(ingredientId);
      if (cocktails.isEmpty()) {
        throw new CocktailNotFoundException(COCKTAIL_NOT_FOUND_STRING);
      }
      List<CocktailDto> cocktailDtos = cocktails.stream().map(CocktailDto::toModel).toList();
      cocktailCache.putList(cacheKey, cocktailDtos);
      return cocktailDtos;
    }
  }

  public List<CocktailDto> getByName(String name)
      throws CocktailNotFoundException, JsonProcessingException {

    if (cocktailCache.containsKey(name)) {
      return cocktailCache.getList(name);
    } else {
      String apiUrl = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s="
          + URLEncoder.encode(name, StandardCharsets.UTF_8);

      RestTemplate restTemplate = new RestTemplate();

      String jsonString = restTemplate.getForObject(apiUrl, String.class);

      ObjectMapper mapper = new ObjectMapper();

      JsonNode jsonNode = mapper.readTree(jsonString);

      if (jsonNode.has(DRINKS_STRING) && jsonNode.get(DRINKS_STRING).isArray()
          && !jsonNode.get(DRINKS_STRING).isEmpty()) {
        List<CocktailDto> cocktailDtos = new ArrayList<>();
        for (JsonNode mealNode : jsonNode.get(DRINKS_STRING)) {
          Cocktail cocktail = new Cocktail(mealNode);
          cocktailDtos.add(CocktailDto.toModel(cocktail));
        }
        return cocktailDtos;
      } else {
        throw new CocktailNotFoundException(COCKTAIL_NOT_FOUND_STRING);
      }
    }
  }

  public void updateCocktail(String name, Cocktail cocktail) throws CocktailNotFoundException {
    Cocktail cocktailEntity = cocktailRepository.findByName(name);
    if (cocktailEntity == null) {
      throw new CocktailNotFoundException(COCKTAIL_NOT_FOUND_STRING);
    }
    cocktailEntity.setName(cocktail.getName());
    cocktailEntity.setTag(cocktail.getTag());
    cocktailEntity.setCategory(cocktail.getCategory());
    cocktailEntity.setInstruction(cocktail.getInstruction());
    cocktailRepository.save(cocktailEntity);
  }

  public void deleteCocktailByName(String name) throws CocktailNotFoundException {
    Cocktail cocktail = cocktailRepository.findByName(name);
    if (cocktail == null) {
      throw new CocktailNotFoundException("Коктейль не найден");
    }
    cocktailRepository.delete(cocktail);
  }
}