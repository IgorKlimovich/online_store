package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/search")
public class SearchController {

    private static final String SEARCH = "search";
    private static final String USER_PROF = "userProf";
    private static final String NOT_EMPTY="notEmpty";
    private static final String EMPTY= "empty";
    private static final String EMPTY_MESSAGE="не найдено ни одного товара";
    private static final String PRODUCTS_DTO="productsDto";
    private static final String PRODUCT_CATEGORY_DTO = "productCategoryDto";

    private final UtilService utilService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;

    public SearchController(UtilService utilService, ProductService productService,
                            ProductCategoryService productCategoryService,
                            UserService userService) {
        this.utilService = utilService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.userService = userService;
    }

    @GetMapping()
    public String searchProduct(@RequestParam("category") String productCategoryName,
                                @RequestParam("name") String productName,
                                @RequestParam("minPrice") String minPrice,
                                @RequestParam("maxPrice") String maxPrice,
                                Model model, Principal principal) {

        if (Boolean.TRUE.equals(!productCategoryService.existsProductCategoryByName(productCategoryName))
                || productCategoryName == null) {
            model.addAttribute("noCategory", "такой категории нету");
            List<ProductDto> productsDto = productService.findAll();
            List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
            model.addAttribute("productsLastDto", productService.findLast());
            model.addAttribute(PRODUCTS_DTO, productsDto);
            model.addAttribute("productCategoriesDto", productCategoriesDto);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in search product: category not found by category name{}", productCategoryName);
            return "/shop";
        }

        ProductCategoryDto productCategoryDto=productCategoryService.findByName(productCategoryName);
        List<ProductDto> productsDto = utilService.findBySearchParameters(productCategoryName,
                productName, minPrice, maxPrice);
        if (productsDto.isEmpty()) {

            model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryDto);
            model.addAttribute(EMPTY, EMPTY_MESSAGE);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in search product: product not fonded by category name {}," +
                    " product name {}, min price {}, max price {}", productCategoryName, productName, minPrice, maxPrice);
            return SEARCH;
        }
        model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryDto);
        model.addAttribute(PRODUCTS_DTO, productsDto);
        model.addAttribute(NOT_EMPTY, true);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in search product: {} products found by category name {}," +
                        " product name {}, min price {}, max price {}",
                productsDto.size(), productCategoryName, productName, minPrice, maxPrice);
        return SEARCH;
    }

    @GetMapping("/header")
    public String searchHeader(@RequestParam("searchHeader") String name, Model model, Principal principal) {
        List<ProductDto> productsDto = productService.findAllByName(name.trim());
        if (productsDto.isEmpty()) {
            model.addAttribute(EMPTY, EMPTY_MESSAGE);
            log.warn("in header search: product not found by name {}", name);
        } else {
            model.addAttribute(NOT_EMPTY, true);
        }
        model.addAttribute(PRODUCT_CATEGORY_DTO, new ProductCategoryDto());
        model.addAttribute(PRODUCTS_DTO, productsDto);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in header search: founded {} products by name {}", productsDto.size(), name);
        return SEARCH;
    }

    @GetMapping("/searchCategory/{id}")
    public String searchCategory(@PathVariable("id") Integer id, Model model, Principal principal) {
        ProductCategoryDto productCategoryDto = productCategoryService.findById(id);
        List<ProductDto> productsDto = productCategoryDto.getProductsDto();
        if (productsDto.isEmpty()) {
            model.addAttribute(EMPTY, EMPTY_MESSAGE);
            log.warn("in search category: product not found in product category {}", productCategoryDto);
        } else {
            model.addAttribute(NOT_EMPTY, true);
        }
        model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryDto);
        model.addAttribute(PRODUCTS_DTO, productsDto);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in search category: founded {} products in category {}", productsDto.size(), productCategoryDto);
        return SEARCH;
    }
}
