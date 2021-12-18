package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

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
    public String getAdminPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "admin";
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("parameter") String parameter,
                             @RequestParam("name") String name, Model model) {
        if (parameter.equals("all")) {
            List<UserDto> usersDto = userService.findAll();
            model.addAttribute("usersDto", usersDto);
            return "users";
        }

        UserDto userDto = utilService.findUserByParameters(parameter, name);
        if (userDto == null) {
            model.addAttribute("error", "такого пользователя не найдено");
            model.addAttribute("userDto", new UserDto());
            return "/admin";
        } else {
            model.addAttribute("userDto", userDto);
        }
        return "user";
    }

    @GetMapping("/users")
    public String getUsersPage(@RequestParam("parameter") String parameter,
                               Model model) {

        List<UserDto> sortedUsersDto = utilService.sortUsersByParameters(userService.findAll(), parameter);
        model.addAttribute("usersDto", sortedUsersDto);
        return "users";
    }

    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable Integer id, Model model) {
        model.addAttribute("userDto", userService.findById(id));
        return "user";
    }

    @PostMapping("/user/{id}/ban")
    public String ban(@PathVariable("id") Integer id, Model model) {
        userService.toBan(id);
        model.addAttribute("userDto", userService.findById(id));
        return "redirect:/admin/user/{id}";
    }

    @PostMapping("/user/{id}/unban")
    public String unBan(@PathVariable("id") Integer id, Model model) {
        userService.unBan(id);
        model.addAttribute("userDto", userService.findById(id));
        return "redirect:/admin/user/{id}";
    }

    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam("parameterProd") String parameter,
                                 @RequestParam("nameProd") String name, Model model) {
        if (parameter.equals("allProd")) {
            List<ProductDto> productsDto = productService.findAll();
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("productsDto", productsDto);
            model.addAttribute("productDto", new ProductDto());
            return "adminProducts";
        }


        if (parameter.equals("category")) {
            ProductCategoryDto productCategoryDto = productCategoryService.findByName(name.trim());
            if (productCategoryDto == null) {
                model.addAttribute("errorProduct", "такой категории не найдено");
                return "admin";
            }
            if (productCategoryDto.getProductsDto().isEmpty()) {
                model.addAttribute("errorProduct", "в этой категории нету товаров");
                return "admin";
            }
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("productsDto", productCategoryDto.getProductsDto());
            return "adminProducts";
        }
        if (parameter.equals("nameProd")) {
            List<ProductDto> productsDto = productService.findAllByName(name.trim());
            if (productsDto.isEmpty()) {
                model.addAttribute("errorProductDto", "такого товара не найдено");
                return "admin";
            }

            model.addAttribute("productsDto", productsDto);
            return "adminProducts";
        }

        return "admin";
    }

    @PostMapping("/sortProducts")
    public String getProductsPage(HttpServletRequest request, @RequestParam("parameter")
            String parameter, Model model) {

        String[] strings = request.getParameterMap().get("ids");
        List<String> id = Arrays.asList(strings);
        List<Integer> integers = id.stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        List<ProductDto> productsDto = productService.findAllByIds(integers);
        List<ProductDto> sortedProductsDto = utilService.sortProductByParameters(productsDto, parameter);
        model.addAttribute("productCategoriesDto", productCategoryService.findAll());
        model.addAttribute("productsDto", sortedProductsDto);
        return "adminProducts";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@RequestParam("addCategory") String name, Model model) {
        if (name.equals("")) {
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorNameCategory"
                    , "категория не может быть пустой");
            model.addAttribute("error", 1);
            return "adminProductCategories";
        }
        if (productCategoryService.existsProductCategoryByName(name)) {
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorNameCategory"
                    , "такая категория уже существует");
            model.addAttribute("error", 1);
            return "adminProductCategories";
        }
        productCategoryService.save(name);
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        model.addAttribute("productCategoriesDto", productCategoriesDto);
        return "adminProductCategories";
    }


    @GetMapping("/searchProductCategories")
    public String getProductCategoriesPage(@RequestParam("parameterProductCategory") String parameter,
                                           @RequestParam("nameProdCat") String name, Model model) {
        if (parameter.equals("allCategories")) {
            List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
            model.addAttribute("productCategoriesDto", productCategoriesDto);
            model.addAttribute("flag", true);
            return "adminProductCategories";
        }
        if (parameter.equals("categoryName")) {
            ProductCategoryDto productCategoryDto = productCategoryService.findByName(name.trim());
            if (productCategoryDto == null) {
                model.addAttribute("errorCategory", "токой категории нету");
                return "admin";
            }

            model.addAttribute("productCategoryDto",productCategoryDto);
            return "adminProductCategory";
        }
        return "adminProductCategories";
    }

    @PostMapping("/sortProductCategories")
    public String sortProductCategories(HttpServletRequest request, @RequestParam("parameterCategory")
            String parameter, Model model) {
        String[] strings = request.getParameterMap().get("idsCategory");
        List<String> idCategory = Arrays.asList(strings);
        List<Integer> integers = idCategory.stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAllByIds(integers);
        List<ProductCategoryDto> sortedProductsDto = utilService
                .sortProductCategoriesByParameters(productCategoriesDto, parameter);
        model.addAttribute("productCategoriesDto", sortedProductsDto);
        return "adminProductCategories";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(
            @ModelAttribute("productDto") ProductDto productDto,
            @RequestParam("category") String categoryName,
            Model model) {
        if (productDto.getName().trim().equals("")) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorName"
                    , "название не может быть пустым");
            model.addAttribute("error", 1);
            return "adminProducts";
        }
        if (productService.existsProductByName(productDto.getName().trim())) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorName"
                    , "такое название уже существует");
            model.addAttribute("error", 1);
            return "adminProducts";
        }


        if (productDto.getPrice() == null) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorPrice"
                    , "цена не может быть пустой");
            model.addAttribute("error", 1);
            return "adminProducts";
        }

        if (productDto.getPrice() < 0) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorPrice"
                    , "цена не может быть отрицательной");
            model.addAttribute("error", 1);
            return "adminProducts";
        }
        if (productDto.getAmount() == null) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorAmount"
                    , "колличество не может быть пустым");
            model.addAttribute("error", 1);
            return "adminProducts";
        }
        if (productDto.getAmount() < 0) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorAmount"
                    , "колличество не может быть отрицательным");
            model.addAttribute("error", 1);
            return "adminProducts";
        }
        if (categoryName.trim().equals("")) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorCategory"
                    , "категория не может быть пустой");
            model.addAttribute("error", 1);
            return "adminProducts";
        }

        if (!productCategoryService.existsProductCategoryByName(categoryName.trim())) {
            model.addAttribute("productsDto", productService.findAll());
            model.addAttribute("productCategoriesDto", productCategoryService.findAll());
            model.addAttribute("errorCategory"
                    , "такой категории не существует");
            model.addAttribute("error", 1);
            return "adminProducts";
        }

        productService.saveWithCategoryName(productDto, categoryName);
        model.addAttribute("productsDto", productService.findAll());
        model.addAttribute("productCategoriesDto", productCategoryService.findAll());
        return "adminProducts";
    }

    @GetMapping("/searchOrders")
    public String getAdminOrdersPage(@RequestParam("parameterOrder") String parameterOrder, Model model) {

        List<OrderDto> ordersDto = orderService.findAll();

        if (parameterOrder.equals("all")) {
            model.addAttribute("ordersDto", ordersDto);
        }

        if (parameterOrder.equals("paid")) {
            List<OrderDto> paidOrdersDto = ordersDto
                    .stream()
                    .filter(item -> item.getStateOrderDto().getName().equals("PAID"))
                    .collect(Collectors.toList());
            model.addAttribute("ordersDto", paidOrdersDto);
        }

        if (parameterOrder.equals("new")) {
            List<OrderDto> newOrdersDto = ordersDto
                    .stream()
                    .filter(item -> item.getStateOrderDto().getName().equals("NEW"))
                    .collect(Collectors.toList());
            model.addAttribute("ordersDto", newOrdersDto);
        }

        if (parameterOrder.equals("delivered")) {
            List<OrderDto> deliveredOrdersDto = ordersDto
                    .stream()
                    .filter(item -> item.getStateOrderDto().getName().equals("DELIVERED"))
                    .collect(Collectors.toList());
            model.addAttribute("ordersDto", deliveredOrdersDto);
        }


        return "adminOrders";
    }

}
