package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductsController {

    private final UserService userService;
    private final ProductCategoryService productCategoryService;

    @GetMapping
    public String getProductsPage(Model model, Principal principal) {
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoriesDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get product page: return to product page");
        return PRODUCTS;
    }
}
