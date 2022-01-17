package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private static final String ERROR = "error";
    private static final String USER_PROF = "userProf";
    private static final String PRODUCT_DTO = "productDto";
    private static final String ADMIN_PRODUCT = "adminProduct";
    private static final String PRODUCT_CATEGORIES_DTO = "productCategoriesDto";

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final UtilService utilService;


    public AdminProductController(ProductService productService, ProductCategoryService productCategoryService,
                                  UserService userService, UtilService utilService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.userService = userService;

        this.utilService = utilService;
    }

    @GetMapping("/{id}")
    public String getAdminProductPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute(PRODUCT_DTO, productDto);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
        if (principal != null) {
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        }
        log.info("in get admin product page: found product {}", productDto);
        return ADMIN_PRODUCT;
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                Model model, Principal principal) {

        if (productDto.getName().trim().equals("")) {
            model.addAttribute("emptyProductName", true);
            model.addAttribute(ERROR, 2);
            model.addAttribute(PRODUCT_DTO, productService.findById(productDto.getId()));
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update product: empty product name");
            return ADMIN_PRODUCT;

        }

        if (productDto.getPrice() == null) {
            model.addAttribute("emptyPriceProduct", true);
            model.addAttribute(ERROR, 2);
            model.addAttribute(PRODUCT_DTO, productService.findById(productDto.getId()));
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update product: empty product price");
            return ADMIN_PRODUCT;
        }


        if (productDto.getAmount() == null) {
            model.addAttribute("emptyAmountProduct", "колличество не может быть пустым");
            model.addAttribute(ERROR, 2);
            model.addAttribute(PRODUCT_DTO, productService.findById(productDto.getId()));
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update product: empty product amount");
            return ADMIN_PRODUCT;
        }

        if (Boolean.FALSE.equals(productCategoryService
                .existsProductCategoryByName(productDto.getProductCategoryDto().getName().trim()))) {
            model.addAttribute("categoryNotExist", true);
            model.addAttribute(ERROR, 2);
            model.addAttribute(PRODUCT_DTO, productService.findById(productDto.getId()));
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            log.warn("in update product: product category {} not found", productDto.getProductCategoryDto().getName());
            return ADMIN_PRODUCT;
        }
        productService.update(productDto);

        ProductDto productDtoUpdated = productService.findById(productDto.getId());
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
        model.addAttribute(PRODUCT_DTO, productDtoUpdated);
        if (principal != null) {
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        }
        log.info("in update product: product {} updated", productDtoUpdated);
        return ADMIN_PRODUCT;
    }


    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute("productDto") ProductDto productDto,
                                Model model, Principal principal) {
        productService.delete(productDto);

        model.addAttribute("productsDto", productService.findAll());
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
        if (principal != null) {
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        }
        log.info("in delete product: product {} deleted", productDto);
        return "adminProducts";
    }


    @PostMapping("/add_photo")
    public String addPhoto(@RequestParam("file") MultipartFile multipartFile,
                           @RequestParam("id") Integer id, Model model, Principal principal) {

        if (multipartFile.isEmpty()) {
            model.addAttribute(PRODUCT_DTO, productService.findById(id));
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            if (principal != null) {
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            }
            return ADMIN_PRODUCT;
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        ProductDto productDto = productService.findById(id);
        String uploadDir = "./product-photos/" + productDto.getId();
        utilService.savePhotoWithPath(uploadDir, fileName, multipartFile);
        productDto.setNamePhoto(fileName);
        productService.addPhoto(productDto);
        log.info("in add photo product: photo name {} saved for product {}", fileName, productDto);
        model.addAttribute(PRODUCT_DTO, productDto);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
        if (principal != null) {
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        }

        return ADMIN_PRODUCT;
    }


    @PostMapping("delete_photo")
    public String deletePhotoProduct(@RequestParam("id") Integer id, Principal principal, Model model) {

        ProductDto productDto = productService.findById(id);
        productDto.setNamePhoto(null);
        productService.deletePhoto(productDto);
        model.addAttribute(PRODUCT_DTO, productDto);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
        if (principal != null) {
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        }

        return ADMIN_PRODUCT;
    }

}
