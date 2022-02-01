package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
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

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/shop")
public class ShopController {

    private final UserService userService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final PersistentTokenRepository persistentTokenRepository;

    @GetMapping
    public String getShopPage(Model model, Principal principal) {
        List<ProductDto> productsDto = productService.findAll();
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        List<ProductDto> lastListDto = productService.findLast();
        model.addAttribute(PRODUCTS_LAST_DTO, lastListDto)
                .addAttribute(PRODUCT_CATEGORIES_DTO, productCategoriesDto)
                .addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get shop page: founded {} products, {} product categories, {}last product",
                productsDto.size(), productCategoriesDto.size(), lastListDto.size());
        return SHOP;
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Integer id, Model model, Principal principal) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute(PRODUCT_DTO, productDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get one: show product {}", productDto);
        return PRODUCT;
    }

    @PostMapping("/delete")
    public String deleteProfile(Principal principal, Authentication authentication,
                                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String login = principal.getName();
        userService.setDelete(login);
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        persistentTokenRepository.removeUserTokens(login);
        log.info("in delete profile: user set deleted by login{}, user logout and redirected to shop page", login);
        return "redirect:/shop";
    }

    @PostMapping("/restore")
    public String restore(@RequestParam("login") String login) {
        userService.setActive(login);
        log.info("in restore:user by login {} set active", login);
        return "redirect:/login";
    }
}
