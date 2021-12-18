package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/product_category")
public class AdminProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public AdminProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{id}")
    public String getAdminProductCategoryPage(@PathVariable("id") Integer id, Model model) {
        ProductCategoryDto productCategoryDto = productCategoryService.findById(id);
        model.addAttribute("productCategoryDto", productCategoryDto);
        return "adminProductCategory";
    }

    @PostMapping("/update")
    public String updateProductCategory(@ModelAttribute("productCategory") ProductCategoryDto productCategoryDto,
                                        Model model) {
        if (productCategoryDto.getName().trim().equals("")) {
            model.addAttribute("errorCategoryName", "название категории не может быть пустым");
            model.addAttribute("error", 1);
            model.addAttribute("productCategoryDto", productCategoryService.findById(productCategoryDto.getId()));
            return "adminProductCategory";
        }
        if (productCategoryService.existsProductCategoryByName(productCategoryDto.getName())) {
            model.addAttribute("errorCategoryName", "такая категория уже существует");
            model.addAttribute("error", 1);
            model.addAttribute("productCategoryDto", productCategoryService.findById(productCategoryDto.getId()));
            return "adminProductCategory";
        }
        productCategoryService.update(productCategoryDto);
        model.addAttribute("productCategoryDto", productCategoryService.findById(productCategoryDto.getId()));
        return "adminProductCategory";
    }

    @PostMapping("/delete")
    public String deleteProductCategory(@ModelAttribute("productCategoryDto") ProductCategoryDto productCategoryDto, Model model) {

        if (!productCategoryService.findById(productCategoryDto.getId()).getProductsDto().isEmpty()) {
            model.addAttribute("error", 2);
            model.addAttribute("productCategoryDto", productCategoryService.findById(productCategoryDto.getId()));

            return "adminProductCategory";
        }
        productCategoryService.delete(productCategoryDto);
        model.addAttribute("productCategoriesDto", productCategoryService.findAll());
        return "adminProductCategories";

    }
}
