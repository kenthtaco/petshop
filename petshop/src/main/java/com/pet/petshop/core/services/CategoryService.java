package com.pet.petshop.core.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pet.petshop.core.dto.CategoryResponse;
import com.pet.petshop.core.entities.Category;
import com.pet.petshop.core.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        List<Category> category = (List<Category>) categoryRepository.findAll();
        return category.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return mapToCategoryResponse(category);
    }

    public CategoryResponse save(CategoryResponse categoryResponse) {
        Category category = mapToCategory(categoryResponse);
        Category save = categoryRepository.save(category);
        return mapToCategoryResponse(save);
    }

    public CategoryResponse updateAula(Long id, CategoryResponse categoryResponse) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        category.setName(categoryResponse.getName());
        category.setDescription(categoryResponse.getDescription());

        Category update = categoryRepository.save(category);
        return mapToCategoryResponse(update);
    }

    public String delete(Long id){
        categoryRepository.deleteById(id);
        return "Categoria con ID " + id + " eliminada correctamente";
    }   

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    private Category mapToCategory(CategoryResponse categoryResponse) {
        return Category.builder()
                .name(categoryResponse.getName())
                .description(categoryResponse.getDescription())
                .build();
    }

}
