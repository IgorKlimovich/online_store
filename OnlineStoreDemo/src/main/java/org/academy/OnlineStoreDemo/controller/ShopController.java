package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final PersistentTokenRepository persistentTokenRepository;

    public ShopController(ProductService productService, ProductCategoryService productCategoryService,
                          UserService userService, PersistentTokenRepository persistentTokenRepository) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.userService = userService;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @GetMapping
    public String getShopPage(Model model) {
        List<ProductDto> productsDto = productService.findAll();
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        List<ProductDto> lastListDto=productService.findLast();
        model.addAttribute("productsLastDto", lastListDto);
        model.addAttribute("productCategoriesDto", productCategoriesDto);
        model.addAttribute("productsDto", productsDto);


        return "shop";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Integer id, Model model) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute("productDto", productDto);
        return "product";
    }

    @PostMapping("/delete")
    public String deleteProfile(Principal principal, Authentication authentication,
                                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String login=principal.getName();
        userService.setDelete(login);
        new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse,authentication);
        persistentTokenRepository.removeUserTokens(login);
        return "redirect:/shop";
    }

    @PostMapping("/restore")
    public String restore(@RequestParam("login") String login){

        userService.setActive(login);
        return "redirect:/login";
    }
}
