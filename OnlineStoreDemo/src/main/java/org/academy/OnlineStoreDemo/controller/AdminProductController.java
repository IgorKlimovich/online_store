package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public AdminProductController(ProductService productService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/{id}")
    public String getAdminProductPage(@PathVariable("id") Integer id, Model model){
        ProductDto productDto = productService.findById(id);
        model.addAttribute("productDto", productDto);
        model.addAttribute("productCategoriesDto", productCategoryService.findAll());
        return "adminProduct";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto, Model model){

        if (productDto.getName().trim().equals("")){
            model.addAttribute("errorName", "название не может быть пустым");
            model.addAttribute("error", 2);
            model.addAttribute("productDto", productService.findById(productDto.getId()));
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            return "adminProduct";
        }

        if (productDto.getPrice()==null){
            model.addAttribute("errorPrice", "цена не может быть пустой");
            model.addAttribute("error", 2);
            model.addAttribute("productDto", productService.findById(productDto.getId()));
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            return "adminProduct";
        }
        if (productDto.getPrice()<0){
            model.addAttribute("errorPrice", "цена не может быть отрицательной");
            model.addAttribute("error", 2);
            model.addAttribute("productDto", productService.findById(productDto.getId()));
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            return "adminProduct";
        }

        if (productDto.getAmount()==null){
            model.addAttribute("errorAmount", "колличество не может быть пустым");
            model.addAttribute("error", 2);
            model.addAttribute("productDto", productService.findById(productDto.getId()));
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            return "adminProduct";
        }
        if (productDto.getAmount()<0){
            model.addAttribute("errorAmount", "колличество неo может быть отрицательным");
            model.addAttribute("error", 2);
            model.addAttribute("productDto", productService.findById(productDto.getId()));
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            return "adminProduct";
        }


        if (!productCategoryService.existsProductCategoryByName(productDto.getProductCategoryDto().getName().trim())){
            model.addAttribute("errorCategory", "такой категории не существует");
            model.addAttribute("error", 2);
            model.addAttribute("productDto", productService.findById(productDto.getId()));
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            return "adminProduct";
        }
        productService.update(productDto);


        model.addAttribute("productCategoriesDto", productCategoryService.findAll());
        System.out.println(productDto.getName());
        model.addAttribute("productDto" ,productDto);
        return "adminProduct";
    }


    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute("productDto") ProductDto productDto, Model model){
        productService.delete(productDto);

        model.addAttribute("productsDto", productService.findAll());
        model.addAttribute("productCategoriesDto",productCategoryService.findAll());
        return "adminProducts";
    }
}
