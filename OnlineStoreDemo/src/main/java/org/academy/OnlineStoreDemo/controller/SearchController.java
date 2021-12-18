package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        if (Boolean.TRUE.equals(!productCategoryService.existsProductCategoryByName(productCategoryName))
                || productCategoryName == null) {
            model.addAttribute("noCategory", "такой категории нету");
            List<ProductDto> productsDto = productService.findAll();
            List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
            model.addAttribute("productsLastDto", productService.findLast());
            model.addAttribute("productsDto", productsDto);
            model.addAttribute("productCategoriesDto", productCategoriesDto);
            return "/shop";
        }

        List<ProductDto> productsDto = utilService.findBySearchParameters(productCategoryName,
                productName, minPrice, maxPrice);
        if (productsDto.isEmpty()) {
            model.addAttribute("empty", "не найдено ни одного товара");
            return "search";
        }
        model.addAttribute("productsDto", productsDto);
        model.addAttribute("notEmpty", true);
        return "search";
    }

    @GetMapping("/header")
    public String searchHeader(@RequestParam ("searchHeader") String name, Model model){
        List<ProductDto> productsDto=productService.findAllByName(name);
        if (productsDto.isEmpty()) {
            model.addAttribute("empty", "не найдено ни одного товара");
            return "search";
        }
        model.addAttribute("productsDto", productsDto);
        model.addAttribute("notEmpty", true);
        return "search";
    }

    @GetMapping("/searchCategory/{id}")
    public String searchCategory(@PathVariable("id") Integer id,Model model){
        ProductCategoryDto productCategoryDto=productCategoryService.findById(id);

       List<ProductDto> productsDto = productCategoryDto.getProductsDto();

        if (productsDto.isEmpty()) {
            model.addAttribute("empty", "не найдено ни одного товара");
            return "search";
        }
        model.addAttribute("productsDto", productsDto);
        model.addAttribute("notEmpty", true);
        return "search";
    }
}
