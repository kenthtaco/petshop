package com.pet.petshop.core.repositories;

import com.pet.petshop.core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM order_detail WHERE product_id = ?1", nativeQuery = true)
    void deleteOrderDetailsByProductId(Long productId);

    @Override
    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = ?1")
    void deleteById(Long id);
}
