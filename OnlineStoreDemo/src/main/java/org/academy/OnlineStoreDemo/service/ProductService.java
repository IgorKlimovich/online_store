package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.model.Product;


import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Integer id);
}
