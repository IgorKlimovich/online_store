package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN = "admin";
    private static final String USER_DTO = "userDto";
    private static final String USER_PROF = "userProf";
    private static final String ORDERS_DTO = "ordersDto";
    private static final String PRODUCTS_DTO = "productsDto";
    private static final String ERROR = "error";
    private static final String ADMIN_PRODUCTS = "adminProducts";
    private static final String PRODUCT_CATEGORIES_DTO = "productCategoriesDto";
    private static final String ADMIN_PRODUCT_CATEGORIES = "adminProductCategories";

    private final UserService userService;
    private final UtilService utilService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final OrderService orderService;


    public AdminController(UserService userService,
                           UtilService utilService,
                           ProductService productService,
                           ProductCategoryService productCategoryService,
                           OrderService orderService) {
        this.userService = userService;
        this.utilService = utilService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getAdminPage(Model model, Principal principal) {
        model.addAttribute(USER_DTO, new UserDto());
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in get admin page: returned admin page");
        return ADMIN;
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("parameter") String parameter, Principal principal,
                             @RequestParam("name") String name, Model model) {
        if (parameter.equals("all")) {
            List<UserDto> usersDto = userService.findAll();
            model.addAttribute("usersDto", usersDto);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.info("in search user: founded {} users by parameter {}", usersDto.size(), parameter);
            return "users";
        }

        if (utilService.findUserByParameters(parameter, name) == null) {
            model.addAttribute("userNotFound",true);
            model.addAttribute(USER_DTO, new UserDto());
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in search user: user not found by name {}", name);
            return ADMIN;
        } else {
            UserDto userDto = utilService.findUserByParameters(parameter,name);
            model.addAttribute(USER_DTO, userDto);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.info("in search user: founded user {} by name {}", userDto, name);
        }
        return "user";
    }

    @GetMapping("/users")
    public String getUsersPage(@RequestParam("parameter") String parameter, Principal principal,
                               Model model) {
        List<UserDto> sortedUsersDto = utilService.sortUsersByParameters(userService.findAll(), parameter);
        model.addAttribute("usersDto", sortedUsersDto);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in get users page: founded {} sorted users by parameter {}", sortedUsersDto.size(), parameter);
        return "users";
    }

    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable Integer id, Model model, Principal principal) {
        model.addAttribute(USER_DTO, userService.findById(id));
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in get user page: return page user {}", userService.findById(id));
        return "user";
    }

    @PostMapping("/user/{id}/ban")
    public String ban(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.toBan(id);
        UserDto userDto = userService.findById(id);
        model.addAttribute(USER_DTO, userDto);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in ban user: user {} set banned", userDto);
        return "redirect:/admin/user/{id}";
    }

    @PostMapping("/user/{id}/unban")
    public String unBan(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.unBan(id);
        UserDto userDto = userService.findById(id);
        model.addAttribute(USER_DTO, userDto);
        if (principal!=null){
            model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
        log.info("in unban user: user {} set active", userDto);
        return "redirect:/admin/user/{id}";
    }

    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam("parameterProd") String parameter, Principal principal,
                                 @RequestParam("nameProd") String name, Model model) {
        if (parameter.equals("allProd")) {
            List<ProductDto> productsDto = productService.findAll();
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute(PRODUCTS_DTO, productsDto);
            model.addAttribute("productDto", new ProductDto());
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.info("in search products: founded {} products", productsDto.size());
            return ADMIN_PRODUCTS;
        }


        if (parameter.equals("category")) {
            ProductCategoryDto productCategoryDto = productCategoryService.findByName(name.trim());
            if (productCategoryDto == null) {
                model.addAttribute("categoryNotFound", true);
                if (principal!=null){
                    model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
                log.warn("in search products: product category not found by name {}", name);
                return ADMIN;
            }
            if (productCategoryDto.getProductsDto().isEmpty()) {
                model.addAttribute("emptyCategory", true);
                if (principal!=null){
                    model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
                log.warn("in search products: product category {} does not contain products", productCategoryDto);
                return ADMIN;
            }
            List<ProductDto> productsDto = productCategoryDto.getProductsDto();
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute(PRODUCTS_DTO, productsDto);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.info("in search products: founded {} products in category {}", productsDto.size(), productCategoryDto);
            return ADMIN_PRODUCTS;
        }
        if (parameter.equals("nameProd")) {
            List<ProductDto> productsDto = productService.findAllByName(name.trim());
            if (productsDto.isEmpty()) {
                model.addAttribute("productNotFound", true);
                if (principal!=null){
                    model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
                log.warn("in search products: products not found by name {}", name);
                return ADMIN;
            }
            model.addAttribute(PRODUCTS_DTO, productsDto);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.info("in search products: founded {} products", productsDto.size());
            return ADMIN_PRODUCTS;
        }
        return ADMIN;
    }

    @PostMapping("/sortProducts")
    public String getSortedProductsPage(HttpServletRequest request, @RequestParam("parameter")
            String parameter, Model model, Principal principal) {
        String[] strings = request.getParameterMap().get("ids");
        List<String> id = Arrays.asList(strings);
        List<Integer> integers = id.stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        List<ProductDto> productsDto = productService.findAllByIds(integers);
        List<ProductDto> sortedProductsDto = utilService.sortProductByParameters(productsDto, parameter);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
        model.addAttribute(PRODUCTS_DTO, sortedProductsDto);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in get sorted products page: sorted {} products by parameter {}",
                sortedProductsDto.size(), parameter);
        return ADMIN_PRODUCTS;
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@RequestParam("addCategory") String name, Model model, Principal principal) {
        if (name.trim().length()>50){
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("moreNumber", true);
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in save category: quantity characters more than 50");
            return ADMIN_PRODUCT_CATEGORIES;
        }

        if (name.trim().equals("")) {
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("emptyCategoryName", true);
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in save category: empty product category");
            return ADMIN_PRODUCT_CATEGORIES;
        }
        if (Boolean.TRUE.equals(productCategoryService.existsProductCategoryByName(name.trim()))) {
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("existCategory", true);
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in save category: product category name {} already exist", name);
            return ADMIN_PRODUCT_CATEGORIES;
        }
        productCategoryService.save(name);
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoriesDto);
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in save category: product category saved by name {}", name);
        return ADMIN_PRODUCT_CATEGORIES;
    }

    @GetMapping("/searchProductCategories")
    public String getProductCategoriesPage(@RequestParam("parameterProductCategory") String parameter, Principal principal,
                                           @RequestParam("nameProdCat") String name, Model model) {
        if (parameter.equals("allCategories")) {
            List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoriesDto);
            model.addAttribute("flag", true);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.info("in get product category page: founded {} product categories", productCategoriesDto.size());
            return ADMIN_PRODUCT_CATEGORIES;
        }
        if (parameter.equals("categoryName")) {
            ProductCategoryDto productCategoryDto = productCategoryService.findByName(name.trim());
            if (productCategoryDto == null) {
                model.addAttribute("noCategory", true);
                if (principal!=null){
                    model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
                log.warn("in get product category page: category not found by name {}", name);
                return ADMIN;
            }

            model.addAttribute("productCategoryDto", productCategoryDto);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.info("in get product category page: founded product category by name {}", name);
            return "adminProductCategory";
        }
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        return ADMIN_PRODUCT_CATEGORIES;
    }

    @PostMapping("/sortProductCategories")
    public String sortProductCategories(HttpServletRequest request, @RequestParam("parameterCategory")
            String parameter, Model model, Principal principal) {
        String[] strings = request.getParameterMap().get("idsCategory");
        List<String> idCategory = Arrays.asList(strings);
        List<Integer> integers = idCategory.stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAllByIds(integers);
        List<ProductCategoryDto> sortedProductsDto = utilService
                .sortProductCategoriesByParameters(productCategoriesDto, parameter);
        model.addAttribute(PRODUCT_CATEGORIES_DTO, sortedProductsDto);
        if (principal!=null){
            model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
        log.info("in sort product categories: sorted {} products category", sortedProductsDto.size());
        return ADMIN_PRODUCT_CATEGORIES;
    }

    @PostMapping("/saveProduct")
    public String saveProduct(
            @ModelAttribute("productDto") ProductDto productDto,
            @RequestParam("category") String categoryName,
            @RequestParam("file") MultipartFile multipartFile, Principal principal,
            Model model) {
        if (productDto.getName().trim().equals("")) {
            model.addAttribute(PRODUCTS_DTO, productService.findAll());
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("emptyProductName", true);
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in save product: empty name product");
            return ADMIN_PRODUCTS;
        }
        if (Boolean.TRUE.equals(productService.existsProductByName(productDto.getName().trim()))) {
            model.addAttribute(PRODUCTS_DTO, productService.findAll());
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("existProductName",true);
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in save product: name product {} already exist", productDto.getName());
            return ADMIN_PRODUCTS;
        }
        if (productDto.getPrice() == null) {
            model.addAttribute(PRODUCTS_DTO, productService.findAll());
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("emptyPriceProduct",true);
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in save product: empty price product");
            return ADMIN_PRODUCTS;
        }

        if (productDto.getAmount() == null) {
            model.addAttribute(PRODUCTS_DTO, productService.findAll());
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("emptyAmountProduct", "колличество не может быть пустым");
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.warn("in save product: empty amount product");
            return ADMIN_PRODUCTS;
        }

        if (Boolean.FALSE.equals(productCategoryService.existsProductCategoryByName(categoryName.trim()))) {
            model.addAttribute(PRODUCTS_DTO, productService.findAll());
            model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
            model.addAttribute("categoryNotExist",true);
            model.addAttribute(ERROR, 1);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.warn("in save product: category not found by name {}", categoryName);
            return ADMIN_PRODUCTS;
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        if (fileName.equals("")){
            productDto.setNamePhoto(null);
            productService.saveWithCategoryName(productDto, categoryName);
        }else {
            productDto.setNamePhoto(fileName);
            productService.saveWithCategoryName(productDto, categoryName);
            ProductDto productDtoAfterSave = productService.findByPhotoName(fileName);
            String uploadDir = "./product-photos/" + productDtoAfterSave.getId();
            utilService.savePhotoWithPath(uploadDir,fileName,multipartFile);
        }
        model.addAttribute(PRODUCTS_DTO, productService.findAll());
        model.addAttribute(PRODUCT_CATEGORIES_DTO, productCategoryService.findAll());
        if (principal!=null){
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
        log.info("in save product: product {} saved", productDto);
        return ADMIN_PRODUCTS;
    }

    @GetMapping("/searchOrders")
    public String getAdminOrdersPage(@RequestParam("parameterOrder") String parameterOrder,
                                     Model model, Principal principal) {
        List<OrderDto> ordersDto = orderService.findAll();
        if (parameterOrder.equals("all")) {
            model.addAttribute(ORDERS_DTO, ordersDto);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.info("in get admin orders page: founded all {} orders", ordersDto.size());
        }
        if (parameterOrder.equals("paid")) {
            List<OrderDto> paidOrdersDto = ordersDto
                    .stream()
                    .filter(item -> item.getStateOrderDto().getName().equals("PAID"))
                    .collect(Collectors.toList());
            model.addAttribute(ORDERS_DTO, paidOrdersDto);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.info("in get admin orders page: founded {} paid orders", ordersDto.size());
        }
        if (parameterOrder.equals("new")) {
            List<OrderDto> newOrdersDto = ordersDto
                    .stream()
                    .filter(item -> item.getStateOrderDto().getName().equals("NEW"))
                    .collect(Collectors.toList());
            model.addAttribute(ORDERS_DTO, newOrdersDto);
            if (principal!=null){
                model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
            log.info("in get admin orders page: founded {} new orders", ordersDto.size());
        }
        if (parameterOrder.equals("delivered")) {
            List<OrderDto> deliveredOrdersDto = ordersDto
                    .stream()
                    .filter(item -> item.getStateOrderDto().getName().equals("DELIVERED"))
                    .collect(Collectors.toList());
            model.addAttribute(ORDERS_DTO, deliveredOrdersDto);
            if (principal!=null){
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));}
            log.info("in get admin orders page: founded {} delivered orders", ordersDto.size());
        }
        if (principal!=null){
            model.addAttribute(USER_PROF , userService.findByLogin(principal.getName()));}
        return "adminOrders";
    }
}
