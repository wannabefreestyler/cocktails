package com.example.cocktails.service;

import com.example.cocktails.component.Cache;
import com.example.cocktails.dto.CocktailDTO;
import com.example.cocktails.entity.Cocktail;
import com.example.cocktails.exception.CocktailAlreadyExistException;
import com.example.cocktails.exception.CocktailNotFoundException;
import com.example.cocktails.repository.CocktailRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class CocktailService {

    private final ObjectMapper objectMapper;
    private final CocktailRepository cocktailRepository;
    private final Cache<String, CocktailDTO> cocktailCache;
    private static final String DRINKS_STRING = "drinks";
    private static final String COCKTAIL_NOT_FOUND_STRING = "такого коктейля нет ;(";

    @Autowired
    public CocktailService(ObjectMapper objectMapper, CocktailRepository cocktailRepository, Cache<String, CocktailDTO> cocktailCache) {
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

    public CocktailDTO getCocktail(String name) throws CocktailNotFoundException {
        if (cocktailCache.containsKey(name)) {
            return cocktailCache.get(name);
        } else {
            Cocktail cocktail = cocktailRepository.findByName(name);
            if (cocktail == null) {
                throw new CocktailNotFoundException(COCKTAIL_NOT_FOUND_STRING);
            }
            CocktailDTO cocktailDTO = CocktailDTO.toModel(cocktail);
            cocktailCache.put(name, cocktailDTO);
            return cocktailDTO;
        }
    }

    public List<CocktailDTO> getCocktailesWithIngredient(Long ingredientId) throws CocktailNotFoundException {
        String cacheKey = "ingredient_" + ingredientId;
        if (cocktailCache.containsKey(cacheKey)) {
            return cocktailCache.getList(cacheKey);
        } else {
            List<Cocktail> cocktails = cocktailRepository.findCocktailsByIngredientList_Id(ingredientId);
            if (cocktails.isEmpty()) {
                throw new CocktailNotFoundException(COCKTAIL_NOT_FOUND_STRING);
            }
            List<CocktailDTO> cocktailDTOs = cocktails.stream().map(CocktailDTO::toModel).toList();
            cocktailCache.putList(cacheKey, cocktailDTOs);
            return cocktailDTOs;
        }
    }

    public List<CocktailDTO> getByName(String name) throws CocktailNotFoundException, JsonProcessingException {

        if (cocktailCache.containsKey(name)) {
            return cocktailCache.getList(name);
        } else {
            String apiUrl = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + URLEncoder.encode(name, StandardCharsets.UTF_8);

            RestTemplate restTemplate = new RestTemplate();

            String jsonString = restTemplate.getForObject(apiUrl, String.class);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree(jsonString);

            if (jsonNode.has(DRINKS_STRING) && jsonNode.get(DRINKS_STRING).isArray() && !jsonNode.get(DRINKS_STRING).isEmpty()) {
                List<CocktailDTO> cocktailDTOS = new ArrayList<>();
                for (JsonNode mealNode : jsonNode.get(DRINKS_STRING)) {
                    Cocktail cocktail = new Cocktail(mealNode);
                    cocktailDTOS.add(CocktailDTO.toModel(cocktail));
                }
                return cocktailDTOS;
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
        cocktailEntity.setCountry(cocktail.getCountry());
        cocktailEntity.setCategory(cocktail.getCategory());
        cocktailEntity.setInstruction(cocktail.getInstruction());
        cocktailRepository.save(cocktailEntity);
    }

    public void deleteCocktail(Long id) throws CocktailNotFoundException {
        Cocktail cocktail = cocktailRepository.findById(id).orElse(null);
        if (cocktail != null) {
            cocktailRepository.deleteById(id);
        } else {
            throw new CocktailNotFoundException(COCKTAIL_NOT_FOUND_STRING);
        }
    }
}