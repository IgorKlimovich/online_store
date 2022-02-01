package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.service.*;
import org.academy.OnlineStoreDemo.valid.ProductCategoryValidatorService;
import org.academy.OnlineStoreDemo.valid.ProductValidatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UtilService utilService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductValidatorService productValidatorService;
    private final ProductCategoryValidatorService productCategoryValidatorService;

    @GetMapping
    public String getAdminPage(Model model, Principal principal) {
        model.addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get admin page: returned admin page");
        return ADMIN;
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("parameter") String parameter, Principal principal,
                             @RequestParam("name") String name, Model model) {
        List<UserDto> usersDto = utilService.findUserByParameters(parameter, name);
        if (usersDto == null || usersDto.isEmpty()) {
            model.addAttribute(USER_NOT_FOUND, true)
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in search user: user not found by name {}", name);
            return ADMIN;
        }
        model.addAttribute(USERS_DTO, usersDto);
        model.addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in search user: founded  {} users by name {}", usersDto.size(), name);
        return USERS;
    }

    @GetMapping("/users")
    public String getUsersPage(@RequestParam("parameter") String parameter, Principal principal, Model model) {
        model.addAttribute(USERS_DTO, utilService.sortUsersByParameters(parameter))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get users page:  sort user by parameter {}", parameter);
        return USERS;
    }

    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable Integer id, Model model, Principal principal) {
        model.addAttribute(USER_DTO, userService.findById(id))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get user page: return page user {}", userService.findById(id));
        return USER;
    }

    @PostMapping("/user/{id}/ban")
    public String ban(@PathVariable("id") Integer id, Model model, Principal principal) {
        model.addAttribute(USER_DTO, userService.toBan(id))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in ban user: user with id {} set banned", id);
        return "redirect:/admin/user/{id}";
    }

    @PostMapping("/user/{id}/unban")
    public String unBan(@PathVariable("id") Integer id, Model model, Principal principal) {
        model.addAttribute(USER_DTO, userService.unBan(id))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in unban user: user with id {} set active", id);
        return "redirect:/admin/user/{id}";
    }

    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam("parameterProd") String parameter, Principal principal,
                                 @RequestParam("nameProd") String name, Model model) {

        String message = productValidatorService.validateSearchProduct(parameter, name);
        if (!message.isEmpty()) {
            model.addAttribute(message, true)
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            return ADMIN;
        }
        List<ProductDto> productsDto = utilService.findProductsByParameters(parameter, name);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in search products: founded {} products", productsDto.size());
        return ADMIN_PRODUCTS;
    }

    @PostMapping("/sortProducts")
    public String getSortedProductsPage(HttpServletRequest request, @RequestParam("parameter")
            String parameter, Model model, Principal principal) {
        List<Integer> integers = Arrays.asList(request.getParameterMap().get("ids")).stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        List<ProductDto> productsDto = productService.findAllByIds(integers);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(PRODUCTS_DTO, utilService.sortProductByParameters(productsDto, parameter))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get sorted products page: sorted  products by parameter {}", parameter);
        return ADMIN_PRODUCTS;
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@RequestParam("addCategory") String name, Model model, Principal principal) {
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        String message = productCategoryValidatorService.validateProductCategory(name);
        if (!message.isEmpty()) {
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoriesDto)
                    .addAttribute(message, true)
                    .addAttribute(ERROR, FLAG)
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in save category: quantity characters more than 50");
            return ADMIN_PRODUCT_CATEGORIES;
        }
        productCategoryService.save(name);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in save category: product category saved by name {}", name);
        return ADMIN_PRODUCT_CATEGORIES;
    }

    @GetMapping("/searchProductCategories")
    public String searchProductCategories(@RequestParam("parameterProductCategory") String parameter, Principal principal,
                                          @RequestParam("nameProdCat") String name, Model model) {
        List<ProductCategoryDto> productCategoriesDto = utilService.findProductCategoriesByParameters(parameter, name);
        if (productCategoriesDto == null || productCategoriesDto.isEmpty()) {
            model.addAttribute(NO_CATEGORY, true)
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in get product category page: category not found by name {}", name);
            return ADMIN;
        }
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoriesDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get product category page: founded product categories by name {}", name);
        return ADMIN_PRODUCT_CATEGORIES;
    }

    @PostMapping("/sortProductCategories")
    public String sortProductCategories(HttpServletRequest request, @RequestParam("parameterCategory")
            String parameter, Model model, Principal principal) {
        List<Integer> integers = Arrays.stream(request.getParameterMap().get("idsCategory"))
                .map(Integer::parseInt).collect(Collectors.toList());
        List<ProductCategoryDto> sortedProductCategoriesDto =
                utilService.sortProductCategoriesByParameters(integers, parameter);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, sortedProductCategoriesDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in sort product categories: sorted {} products category", sortedProductCategoriesDto.size());
        return ADMIN_PRODUCT_CATEGORIES;
    }

    @PostMapping("/saveProduct")
    public String saveProduct(
            @ModelAttribute("productDto") ProductDto productDto,
            @RequestParam("category") String categoryName,
            @RequestParam("file") MultipartFile multipartFile, Principal principal,
            Model model) {
        String message = productValidatorService.validateProduct(productDto, categoryName);
        if (!message.isEmpty()) {
            model.addAttribute(PRODUCTS_DTO, productService.findAll())
                    .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                    .addAttribute(message, true).addAttribute(ERROR, FLAG)
                    .addAttribute(USER_PROF, userService.findByLogin(principal));
            log.warn("in save product: fields have errors");
            return ADMIN_PRODUCTS;
        }
        productService.save(productDto, categoryName, multipartFile);
        model.addAttribute(PRODUCTS_DTO, productService.findAll())
                .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in save product: product {} saved", productDto);
        return ADMIN_PRODUCTS;
    }

    @GetMapping("/searchOrders")
    public String searchAdminOrders(@RequestParam("parameterOrder") String parameterOrder,
                                    Model model, Principal principal) {
        model.addAttribute(ORDERS_DTO, utilService.findOrdersByParameters(parameterOrder))
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        return ADMIN_ORDERS;
    }
}
