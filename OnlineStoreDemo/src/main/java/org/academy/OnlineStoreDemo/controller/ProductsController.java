package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        List<ProductCategoryDto> productCategoriesDto= productCategoryService.findAll();
        model.addAttribute("productCategoriesDto", productCategoriesDto);
        return "products";
    }

    @GetMapping("/list/{name}")
    public String getListProductByCategory(@PathVariable("name") String productCategoryName,
                                           Model model) {

        List<ProductDto> productsDto = productCategoryService.findByName(productCategoryName).getProductsDto();
        model.addAttribute("productsDto", productsDto);
        return "list";
    }
}
