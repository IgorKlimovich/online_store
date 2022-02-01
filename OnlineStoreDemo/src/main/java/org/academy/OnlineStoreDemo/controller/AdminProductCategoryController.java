package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.valid.ProductCategoryValidatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin/product_category")
public class AdminProductCategoryController {

    private final UserService userService;
    private final ProductCategoryService productCategoryService;
    private final ProductCategoryValidatorService productCategoryValidatorService;

    @GetMapping("/{id}")
    public String getAdminProductCategoryPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        ProductCategoryDto productCategoryDto = productCategoryService.findById(id);
        model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get admin product category page: founded product category {} by id {}", productCategoryDto, id);
        return ADMIN_PRODUCT_CATEGORY;
    }

    @PostMapping("/update")
    public String updateProductCategory(@ModelAttribute("productCategory") ProductCategoryDto productCategoryDto,
                                        Model model, Principal principal) {
        String message = productCategoryValidatorService.validateProductCategory(productCategoryDto.getName());
        if (!message.isEmpty()) {
            model.addAttribute(message, true)
                    .addAttribute(ERROR, FLAG)
                    .addAttribute(PRODUCT_CATEGORY_DTO, productCategoryService.findById(productCategoryDto.getId()))
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in update product category: empty name product category");
            return ADMIN_PRODUCT_CATEGORY;
        }
        model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryService.update(productCategoryDto))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in update product category: product category {} ", productCategoryDto);
        return ADMIN_PRODUCT_CATEGORY;
    }

    @PostMapping("/delete")
    public String deleteProductCategory(@ModelAttribute("productCategoryDto") ProductCategoryDto productCategoryDto,
                                        Model model, Principal principal) {
        productCategoryService.delete(productCategoryDto);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in delete product category: product category {} deleted", productCategoryDto);
        return ADMIN_PRODUCT_CATEGORIES;
    }
}
