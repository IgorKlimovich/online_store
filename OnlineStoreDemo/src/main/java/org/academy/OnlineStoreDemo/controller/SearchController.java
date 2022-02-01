package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.academy.OnlineStoreDemo.valid.SearchValidatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/search")
public class SearchController {

    private final UtilService utilService;
    private final UserService userService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final SearchValidatorService searchValidatorService;

    @GetMapping()
    public String searchProduct(@RequestParam("category") String productCategoryName,
                                @RequestParam("name") String productName,
                                @RequestParam("minPrice") String minPrice,
                                @RequestParam("maxPrice") String maxPrice,
                                Model model, Principal principal) {

        if (Boolean.TRUE.equals(!productCategoryService.existsProductCategoryByName(productCategoryName))
                || productCategoryName == null) {
            model.addAttribute(NO_CATEGORY, NOT_EXIST_CATEGORY);
            List<ProductDto> productsDto = productService.findAll();
            List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
            model.addAttribute(PRODUCTS_DTO, productsDto)
                    .addAttribute(PRODUCTS_LAST_DTO, productService.findLast())
                    .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoriesDto)
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in search product: category not found by category name{}", productCategoryName);
            return SHOP;
        }
        ProductCategoryDto productCategoryDto = productCategoryService.findByName(productCategoryName);
        List<ProductDto> productsDto = utilService.findBySearchParameters(productCategoryName,
                productName, minPrice, maxPrice);
        String message = searchValidatorService.validateCategory(productsDto);
        model.addAttribute(message, true)
                .addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(PRODUCT_CATEGORY_DTO, productCategoryDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in search product: {} products found by category name {}, product name {}, min price {}, " +
                "max price {}", productsDto.size(), productCategoryName, productName, minPrice, maxPrice);
        return SEARCH;
    }

    @GetMapping("/header")
    public String searchHeader(@RequestParam("searchHeader") String name, Model model, Principal principal) {
        List<ProductDto> filteredProductsDto = utilService.headerSearch(name);
        String message = searchValidatorService.validateSearch(name, filteredProductsDto);
        model.addAttribute(message, EMPTY_MESSAGE)
                .addAttribute(PRODUCTS_DTO, filteredProductsDto)
                .addAttribute(PRODUCT_CATEGORY_DTO, new ProductCategoryDto())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in header search: founded {} products by name {}", filteredProductsDto.size(), name);
        return SEARCH;
    }

    @GetMapping("/searchCategory/{id}")
    public String searchCategory(@PathVariable("id") Integer id, Model model, Principal principal) {
        ProductCategoryDto productCategoryDto = productCategoryService.findById(id);
        List<ProductDto> productsDto = productCategoryDto.getProductsDto();
        String message = searchValidatorService.validateCategory(productsDto);
        model.addAttribute(message, EMPTY_MESSAGE)
                .addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(PRODUCT_CATEGORY_DTO, productCategoryDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in search category: founded {} products in category {}", productsDto.size(), productCategoryDto);
        return SEARCH;
    }
}