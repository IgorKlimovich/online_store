package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/admin/product_category")
public class AdminProductCategoryController {

    private static final String ERROR="error";
    private static final String USER_PROF = "userProf";
    private static final String ADMIN_PRODUCT_CATEGORY="adminProductCategory";
    private static final String PRODUCT_CATEGORY_DTO="productCategoryDto";
    private final ProductCategoryService productCategoryService;
    private final UserService userService;


    public AdminProductCategoryController(ProductCategoryService productCategoryService, UserService userService) {
        this.productCategoryService = productCategoryService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getAdminProductCategoryPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        ProductCategoryDto productCategoryDto = productCategoryService.findById(id);
        model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryDto);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in get admin product category page: founded product category {} by id {}",productCategoryDto,id);
        return ADMIN_PRODUCT_CATEGORY;
    }

    @PostMapping("/update")
    public String updateProductCategory(@ModelAttribute("productCategory") ProductCategoryDto productCategoryDto,
                                        Model model,Principal principal) {
        if (productCategoryDto.getName().trim().length()>50){
            model.addAttribute("moreNumber",true);
            model.addAttribute(ERROR, 1);
            model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryService.findById(productCategoryDto.getId()));
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in update product category: quantity characters more than 50");
            return ADMIN_PRODUCT_CATEGORY;
        } 
        if (productCategoryDto.getName().trim().equals("")) {
            model.addAttribute("emptyCategoryName", true);
            model.addAttribute(ERROR, 1);
            model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryService.findById(productCategoryDto.getId()));
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in update product category: empty name product category");
            return ADMIN_PRODUCT_CATEGORY;
        }
        if (Boolean.TRUE.equals(productCategoryService.existsProductCategoryByName(productCategoryDto.getName()))) {
            model.addAttribute("existCategory",true);
            model.addAttribute(ERROR, 1);
            model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryService.findById(productCategoryDto.getId()));
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in update product category: product category name already exist by name {}",
                    productCategoryDto.getName());
            return ADMIN_PRODUCT_CATEGORY;
        }
        productCategoryService.update(productCategoryDto);
        model.addAttribute(PRODUCT_CATEGORY_DTO, productCategoryService.findById(productCategoryDto.getId()));
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in update product category: product category {} ", productCategoryDto);
        return ADMIN_PRODUCT_CATEGORY;
    }

    @PostMapping("/delete")
    public String deleteProductCategory(@ModelAttribute("productCategoryDto") ProductCategoryDto productCategoryDto,
                                        Model model, Principal principal) {

        productCategoryService.delete(productCategoryDto);
        model.addAttribute("productCategoriesDto", productCategoryService.findAll());
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in delete product category: product category {} deleted", productCategoryDto);
        return "adminProductCategories";
    }
}
