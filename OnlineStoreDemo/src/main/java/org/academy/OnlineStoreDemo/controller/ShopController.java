package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
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

@Slf4j
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
    public String getShopPage(Model model, Principal principal) {
        List<ProductDto> productsDto = productService.findAll();
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        List<ProductDto> lastListDto=productService.findLast();
        model.addAttribute("productsLastDto", lastListDto);
        model.addAttribute("productCategoriesDto", productCategoriesDto);
        model.addAttribute("productsDto", productsDto);
        if (principal!=null){
            model.addAttribute("userProf" , userService.findByLogin(principal.getName()));}
        log.info("in get shop page: founded {} products, {} product categories, {}last product",
                productsDto.size(),productCategoriesDto.size(),lastListDto.size());
        return "shop";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Integer id, Model model, Principal principal) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute("productDto", productDto);
        if (principal!=null){
            model.addAttribute("userProf" , userService.findByLogin(principal.getName()));}
        log.info("in get one: show product {}", productDto);
        return "product";
    }

    @PostMapping("/delete")
    public String deleteProfile(Principal principal, Authentication authentication,
                                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String login=principal.getName();
        userService.setDelete(login);
        new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse,authentication);
        persistentTokenRepository.removeUserTokens(login);
        log.info("in delete profile: user set deleted by login{}, user logout and redirected to shop page",login);
        return "redirect:/shop";
    }

    @PostMapping("/restore")
    public String restore(@RequestParam("login") String login){
        userService.setActive(login);
        log.info("in restore:user by login {} set active", login);
        return "redirect:/login";
    }
}
