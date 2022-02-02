package org.academy.OnlineStoreDemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.exception.ProductCategoryNotFoundException;
import org.academy.OnlineStoreDemo.exception.ProductNotFoundException;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductRepository;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.academy.OnlineStoreDemo.util.UtilListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final UtilService utilService;
    private final UtilListMapper utilListMapper;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        log.info("in find all product:founded {} products", products.size());
        return utilListMapper.mapList(products,ProductDto.class);
    }

    @Override
    public ProductDto findById(Integer id) {
        Product product= productRepository
                .findById(id).orElseThrow(()->new ProductNotFoundException("product not found"));
        log.info("in find product by id: product {} founded by id {}", product, id);
        return modelMapper.map(product, ProductDto.class);
    }

    public Product findProductById(Integer id){
        return productRepository
                .findById(id).orElseThrow(()->new ProductCategoryNotFoundException("product not found"));
    }

    @Override
    public List<ProductDto> findAllByName(String name) {
        List<Product> products = productRepository.findAllByName(name);
        log.info("in find all products by name: founded {} products by name {}", products.size(), name);
        return utilListMapper.mapList(products, ProductDto.class);
    }

    @Override
    public List<ProductDto> findAllByIds(List<Integer> id) {
        List<Product> products = productRepository.findAll();
        List<Product> res = new ArrayList<>();
        for (Product product : products) {
            if (id.contains(product.getId())) {
                res.add(product);
            }
        }
        log.info("in find all products by ids: founded {} products", res.size());
        return utilListMapper.mapList(res,ProductDto.class);
    }

    @Override
    @Transactional
    public void save(ProductDto productDto, String categoryName, MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.equals("")) {
            productDto.setNamePhoto(null);
        } else {
            productDto.setNamePhoto(fileName);
            Product product = modelMapper.map(productDto, Product.class);
            ProductCategory productCategory
                    = productCategoryRepository.findByName(categoryName.trim());
            productCategory.setAmount(productCategory.getAmount() + product.getAmount());
            product.setProductCategory(productCategory);
            product.setName(productDto.getName().trim());
            product.setDescription(productDto.getDescription().trim());
            productCategoryRepository.save(productCategory);
            product.setIsExist(product.getAmount() > 0);
            log.info("in save product: product {} saved to product category {}", product, productCategory);
            Product productAfterSave = productRepository.save(product);
            String uploadDir = "./product-photos/" + productAfterSave.getId();
            utilService.savePhotoWithPath(uploadDir, fileName, file);
        }
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto productDto) {
        Product product = findProductById(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        ProductCategory oldProductCategory = product.getProductCategory();
        oldProductCategory.setAmount(oldProductCategory.getAmount() - product.getAmount());
        product.setAmount(productDto.getAmount());
        product.setIsExist(product.getAmount()>0);
        ProductCategory newProductCategory =
                productCategoryRepository.findByName(productDto.getProductCategoryDto().getName());
        newProductCategory.setAmount(newProductCategory.getAmount() + productDto.getAmount());
        product.setProductCategory(newProductCategory);
        productCategoryRepository.save(oldProductCategory);
        productCategoryRepository.save(newProductCategory);
        Product productAfterUpdate = productRepository.save(product);
        log.info("in update product: updated product {}", product);
        return modelMapper.map(productAfterUpdate,ProductDto.class);
    }

    @Override
    public void delete(ProductDto productDto) {
        Product productFromDb = findProductById(productDto.getId());
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
            return utilListMapper.mapList(listFromDb,ProductDto.class);
        }
        List<Product> lastElement = listFromDb
                .subList(listFromDb.size() - 5, listFromDb.size());
        lastElement.sort(Comparator.comparing(Product::getId, Comparator.reverseOrder()));
        log.info("in find last product: founded last 5 products in reverse order");
        return utilListMapper.mapList(lastElement,ProductDto.class);
    }

    @Override
    public ProductDto addPhoto(Integer id , MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Product product=productRepository.getById(id);
        String uploadDir = "./product-photos/" + id;
        utilService.savePhotoWithPath(uploadDir, fileName, file);
        product.setNamePhoto(fileName);
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductDto deletePhoto(Integer id) {
        Product product=findProductById(id);
        product.setNamePhoto(null);
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }
}
