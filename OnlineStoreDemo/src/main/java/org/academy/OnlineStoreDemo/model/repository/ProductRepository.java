package org.academy.OnlineStoreDemo.model.repository;

import org.academy.OnlineStoreDemo.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findById(Integer id);
    Product findByName(String name);
    Boolean existsProductByName(String name);
    List<Product> findAllByName(String name);
    Product findByNamePhoto(String photoName);
//    List<Product>  findAllByIds (List<Integer> ids);
}
