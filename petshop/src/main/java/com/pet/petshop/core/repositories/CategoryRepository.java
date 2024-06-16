package com.pet.petshop.core.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.pet.petshop.core.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findAll();

}
