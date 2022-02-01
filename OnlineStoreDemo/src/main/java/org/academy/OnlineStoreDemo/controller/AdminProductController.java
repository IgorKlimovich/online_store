package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.valid.ProductValidatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private final UserService userService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductValidatorService productValidatorService;

    @GetMapping("/{id}")
    public String getAdminProductPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute(PRODUCT_DTO, productDto)
                .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get admin product page: found product {}", productDto);
        return ADMIN_PRODUCT;
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                Model model, Principal principal) {
        String message = productValidatorService.validateProduct(productDto, productDto.getProductCategoryDto().getName());
        if (!message.isEmpty()) {
            model.addAttribute(message, true)
                    .addAttribute(ERROR, FLAG_ERR)
                    .addAttribute(PRODUCT_DTO, productService.findById(productDto.getId()))
                    .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in update product: fields haves errors");
            return ADMIN_PRODUCT;
        }
        ProductDto productDtoUpdated = productService.update(productDto);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(PRODUCT_DTO, productDtoUpdated)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in update product: product {} updated", productDtoUpdated);
        return ADMIN_PRODUCT;
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute("productDto") ProductDto productDto,
                                Model model, Principal principal) {
        productService.delete(productDto);
        model.addAttribute(PRODUCTS_DTO, productService.findAll())
                .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in delete product: product {} deleted", productDto);
        return ADMIN_PRODUCTS;
    }

    @PostMapping("/add_photo")
    public String addPhoto(@RequestParam("file") MultipartFile multipartFile,
                           @RequestParam("id") Integer id, Model model, Principal principal) {
        if (multipartFile.isEmpty()) {
            model.addAttribute(PRODUCT_DTO, productService.findById(id))
                    .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            return ADMIN_PRODUCT;
        }
        log.info("in add photo product: photo saved for product with id {}", id);
        model.addAttribute(PRODUCT_DTO, productService.addPhoto(id, multipartFile))
                .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        return ADMIN_PRODUCT;
    }

    @PostMapping("delete_photo")
    public String deletePhotoProduct(@RequestParam("id") Integer id, Principal principal, Model model) {
        model.addAttribute(PRODUCT_DTO, productService.deletePhoto(id))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        return ADMIN_PRODUCT;
    }
}
