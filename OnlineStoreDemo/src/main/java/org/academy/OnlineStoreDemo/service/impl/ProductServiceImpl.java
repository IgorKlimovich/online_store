package org.academy.OnlineStoreDemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductRepository;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productsDto = new ArrayList<>();
        for (Product product : products) {
            ProductDto map = modelMapper.map(product, ProductDto.class);
            productsDto.add(map);
        }
        log.info("in find all product:founded {} products",products.size());
        return productsDto;
    }

    @Override
    public ProductDto findById(Integer id) {
        Product candidate = null;
        try {
            candidate = productRepository.findById(id).orElseThrow(Exception::new);
        } catch (Exception e) {
            log.error("in find product by id: product not found by id {}", id);
            return null;
        }
        log.info("in find product by id: product {} founded by id {}", candidate, id);
        return modelMapper.map(candidate, ProductDto.class);
    }

    @Override
    public Boolean existsProductByName(String name) {
        return productRepository.existsProductByName(name);
    }

    @Override
    public List<ProductDto> findAllByName(String name) {
        List<Product> products = productRepository.findAllByName(name);
        if (products.isEmpty()){
            log.warn("find all by name: products not found by name {}",name);
            return Collections.emptyList();
        }
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto map = modelMapper.map(product, ProductDto.class);
            productDtos.add(map);
        }
        log.info("in find all products by name: founded {} products by name {}", products.size(), name);
        return productDtos;
    }


    public List<ProductDto> findAllByIds(List<Integer> id) {
        List<Product> products = productRepository.findAll();
        List<Product> res = new ArrayList<>();
        for (Product product : products) {
            if (id.contains(product.getId())) {
                res.add(product);
            }
        }
        List<ProductDto> productsDto = new ArrayList<>();
        for (Product product : res) {
            ProductDto map = modelMapper.map(product, ProductDto.class);
            productsDto.add(map);
        }

        log.info("in find all products by ids: founded {} products", res.size());
        return productsDto;
    }

    @Override
    public void saveWithCategoryName(ProductDto productDto, String categoryName) {
        Product product=modelMapper.map(productDto,Product.class);
        ProductCategory productCategory = productCategoryRepository.findByName(categoryName.trim());
        productCategory.setAmount(productCategory.getAmount() + product.getAmount());
        product.setProductCategory(productCategory);
        product.setName(productDto.getName().trim());
        product.setDescription(productDto.getDescription().trim());
        productCategoryRepository.save(productCategory);
        product.setIsExist(product.getAmount() > 0);
        log.info("in save product: product {} saved to product category {}", product, productCategory);
        productRepository.save(product);
    }

    @Override
    public void update(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).get();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        ProductCategory oldProductCategory=product.getProductCategory();
        oldProductCategory.setAmount(oldProductCategory.getAmount()-product.getAmount());
        product.setAmount(productDto.getAmount());
        if (product.getAmount()>0){
            product.setIsExist(true);
        }
        if (product.getAmount()<=0){
            product.setIsExist(false);
        }
        ProductCategory newProductCategory =
                productCategoryRepository.findByName(productDto.getProductCategoryDto().getName());
        newProductCategory.setAmount(newProductCategory.getAmount()+productDto.getAmount());
        product.setProductCategory(newProductCategory);
        productCategoryRepository.save(oldProductCategory);
        productCategoryRepository.save(newProductCategory);
        productRepository.save(product);
        log.info("in update product: updated product {}", product);
    }

    @Override
    public void delete(ProductDto productDto) {
        Product productFromDb = productRepository.getById(productDto.getId());
        ProductCategory productCategory = productFromDb.getProductCategory();
        productCategory.setAmount(productCategory.getAmount() - productFromDb.getAmount());
        productCategoryRepository.save(productCategory);
        productRepository.delete(productFromDb);
        log.info("in delete product: product {} deleted", productFromDb);
    }

    public List<ProductDto> findLast() {
        List<Product> listFromDb = productRepository.findAll();
        if (listFromDb.size() < 5) {
            listFromDb.sort(Comparator.comparing(Product::getId, Comparator.reverseOrder()));
            log.info("in find last product: founded < 5 products in reverse order");
            List<ProductDto> productsDto = new ArrayList<>();
            for (Product product : listFromDb) {
                ProductDto map = modelMapper.map(product, ProductDto.class);
                productsDto.add(map);
            }
            return productsDto;
        }
        List<Product> lastElement = listFromDb
                .subList(listFromDb.size() - 5, listFromDb.size());
        lastElement.sort(Comparator.comparing(Product::getId, Comparator.reverseOrder()));
        log.info("in find last product: founded last 5 products in reverse order");
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : lastElement) {
            ProductDto map = modelMapper.map(product, ProductDto.class);
            productDtos.add(map);
        }
        return productDtos;
    }

    @Override
    public ProductDto findByPhotoName(String photoName) {
       Product product= productRepository.findByNamePhoto(photoName);
       if (product==null){
           log.warn("in find product by photo name: product not found by photo name {}",photoName);
           return null;
       }
        log.info("in find product by photo name: founded product{} by photo name {}",product, photoName);
       return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public void addPhoto(ProductDto productDto) {
        productRepository.save(modelMapper.map(productDto,Product.class));
    }

    @Override
    public void deletePhoto(ProductDto productDto) {
        productRepository.save(modelMapper.map(productDto,Product.class));
    }
}
