package com.pet.petshop.core.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.petshop.core.dto.ProductResponse;
import com.pet.petshop.core.entities.Category;
import com.pet.petshop.core.entities.Product;
import com.pet.petshop.core.repositories.CategoryRepository;
import com.pet.petshop.core.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductResponse> getAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapToProductResponse(product);
    }

    @Transactional
    public ProductResponse save(ProductResponse productResponse) {
        Product product = mapToProduct(productResponse);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductResponse productResponse) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setName(productResponse.getName());
        product.setDescription(productResponse.getDescription());
        product.setPrice(productResponse.getPrice());
        product.setStockQuantity(productResponse.getStockQuantity());

        if (productResponse.getCategoryId() != null) {
            Category category = categoryRepository.findById(productResponse.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return mapToProductResponse(updatedProduct);
    }

    public String delete(Long id){
        productRepository.deleteById(id);
        return "Producto con ID " + id + " eliminado correctamente";
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .build();
    }

    private Product mapToProduct(ProductResponse productResponse) {
        Product product = Product.builder()
                .name(productResponse.getName())
                .description(productResponse.getDescription())
                .price(productResponse.getPrice())
                .stockQuantity(productResponse.getStockQuantity())
                .build();

        if (productResponse.getCategoryId() != null) {
            Category category = categoryRepository.findById(productResponse.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }

        return product;
    }
}
