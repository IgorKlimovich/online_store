package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductsController {

    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    public ProductsController(ProductCategoryService productCategoryService, UserService userService) {
        this.productCategoryService = productCategoryService;
        this.userService = userService;
    }

    @GetMapping
    public String getProductsPage(Model model, Principal principal) {
        List<ProductCategoryDto> productCategoriesDto= productCategoryService.findAll();
        model.addAttribute("productCategoriesDto", productCategoriesDto);
        if (principal!=null){
            model.addAttribute("userProf" , userService.findByLogin(principal.getName()));}
        log.info("in get product page: return to product page");
        return "products";
    }

    @GetMapping("/list/{name}")
    public String getListProductByCategory(@PathVariable("name") String productCategoryName,
                                           Model model, Principal principal) {

        List<ProductDto> productsDto = productCategoryService.findByName(productCategoryName).getProductsDto();
        model.addAttribute("productsDto", productsDto);
        if (principal!=null){
            model.addAttribute("userProf" , userService.findByLogin(principal.getName()));}
        log.info("in get list product by category: founded {} products by category name {}",
                productsDto.size(),productCategoryName);
        return "list";
    }
}
