package org.academy.OnlineStoreDemo.repository;

import org.academy.OnlineStoreDemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findById(Integer id);
    Product findByName(String name);
}
