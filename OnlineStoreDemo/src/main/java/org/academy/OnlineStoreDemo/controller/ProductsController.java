package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.Product;
import org.academy.OnlineStoreDemo.model.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private final ProductCategoryService productCategoryService;

    public ProductsController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }


    @GetMapping
    public String getProductsPage(Model model) {
        List<ProductCategory> productCategories = productCategoryService.findAll();
        model.addAttribute("productCategories", productCategories);
        return "products";
    }

    @GetMapping("/list/{name}")
    public String getListProductByCategory(@PathVariable("name") String productCategoryName,
                                           Model model) {

        List<Product> products = productCategoryService.findByName(productCategoryName).getProducts();
        model.addAttribute("products", products);
        return "list";
    }
}
