package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.model.Product;
import org.academy.OnlineStoreDemo.model.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final UtilService utilService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public SearchController(UtilService utilService, ProductService productService,
                            ProductCategoryService productCategoryService) {
        this.utilService = utilService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping()
    public String search(@RequestParam("category") String productCategoryName,
                         @RequestParam("name") String productName,
                         @RequestParam("minPrice") String minPrice,
                         @RequestParam("maxPrice") String maxPrice, Model model) {
        System.out.println(minPrice);
        System.out.println(productCategoryName + productName + minPrice + maxPrice);
        if (!utilService.isExistProductCategoryByName(productCategoryName) || productCategoryName == null) {
            model.addAttribute("noCategory", "такой категории нету");
            List<Product> products = productService.findAll();
            List<ProductCategory> productCategories = productCategoryService.findAll();
            model.addAttribute("products", products);
            model.addAttribute("productCategories", productCategories);
            return "/shop";
        }
        if (!utilService.isExistProductByName(productName) || productName == null) {
            model.addAttribute("noName", "такого названия нету");
            List<Product> products = productService.findAll();
            List<ProductCategory> productCategories = productCategoryService.findAll();
            model.addAttribute("products", products);
            model.addAttribute("productCategories", productCategories);
            return "/shop";
        }

        System.out.println(minPrice);
        if (!minPrice.matches("[0-9]*") || minPrice.equals("")) {
            model.addAttribute("noMinPrice", "введите только цифры");
            List<Product> products = productService.findAll();
            List<ProductCategory> productCategories = productCategoryService.findAll();
            model.addAttribute("products", products);
            model.addAttribute("productCategories", productCategories);
            return "/shop";
        }
        if (!maxPrice.matches("[0-9]*") || maxPrice.equals("")) {
            model.addAttribute("noMaxPrice", "введите только цифры");
            List<Product> products = productService.findAll();
            List<ProductCategory> productCategories = productCategoryService.findAll();
            model.addAttribute("products", products);
            model.addAttribute("productCategories", productCategories);
            return "/shop";
        }
        List<Product> products = utilService.findBySearchParameters(productCategoryName,
                productName, minPrice, maxPrice);
        if (products.isEmpty()){
            System.out.println("emptyuyyyyyyy");
            model.addAttribute("empty", "не найдено ни одного товара");
        }
        model.addAttribute("products", products);
        return "search";
    }
}
