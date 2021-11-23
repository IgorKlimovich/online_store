package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.model.Product;
import org.academy.OnlineStoreDemo.repository.ProductRepository;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(){
       return productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        Product candidate=null;
        try {
             candidate =productRepository.findById(id).orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }


}
