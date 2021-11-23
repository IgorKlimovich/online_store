package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.Product;
import org.academy.OnlineStoreDemo.model.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final UtilService utilService;
    private final ProductCategoryService productCategoryService;

    public ShopController(ProductService productService, UtilService utilService,
                          ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.utilService = utilService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public String getShopPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        List<ProductCategory> productCategories = productCategoryService.findAll();
        model.addAttribute("productCategories", productCategories);
        return "shop";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Integer id, Model model) {
        System.out.println(id);
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product";
    }


}
