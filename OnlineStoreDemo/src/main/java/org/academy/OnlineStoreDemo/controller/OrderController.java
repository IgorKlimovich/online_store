package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller()
@RequestMapping("/orders")
public class OrderController {

    private static final String CARD_DTO = "cardDto";
    private static final String CARDS_DTO = "cardsDto";
    private static final String USER_PROF = "userProf";
    private static final String ORDER = "order";
    private static final String ERROR = "error";
    private static final String ORDER_DTO = "orderDto";
    private static final String PRODUCTS_DTO = "productsDto";

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final CardService cardService;

    public OrderController(UserService userService, ProductService productService,
                           OrderService orderService, OrderProductService orderProductService,
                           CardService cardService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.cardService = cardService;
    }

    @GetMapping
    public String getOrdersPage(Principal principal, Model model,
                                Authentication authentication) {
        if (authentication == null) {
            log.warn("in get orders page: user not authenticated, return to login page");
            return "login";
        }
        UserDto userDto = userService.findByLogin(principal.getName());
        model.addAttribute("userDto", userDto);
        List<OrderDto> ordersDto = userDto.getOrdersDto();
        model.addAttribute("ordersDto", ordersDto);
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in get orders page: founded {} orders for user {}", ordersDto.size(), userDto);
        return "/orders";
    }

    @PostMapping("/add")
    public String addProductToOrder(@RequestParam("id") Integer productId, Principal principal,
                                    Model model) {
        if (principal == null) {
            log.warn("in add product to orders: user not authenticated return to login page");
            return "redirect:/login";
        }
        UserDto userDto = userService.findByLogin(principal.getName());
        OrderDto orderDto = orderService.createOrderIfNotActive(userDto);
        ProductDto productDto = productService.findById(productId);
        if (Boolean.FALSE.equals(productDto.getIsExist())) {
            model.addAttribute(ERROR, 3);
            model.addAttribute("productDto", productDto);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.warn("in add product to order: product {} not exist", productDto);
            return "product";
        }
        orderProductService.saveProductToOrder(orderDto, productDto);
        ProductDto productDtoAfterSave = productService.findById(productId);
        model.addAttribute("productDto", productDtoAfterSave);
        model.addAttribute(ERROR, 12);
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in add product to order: product {} added to order {} for user {}", productDto, orderDto, userDto);
        return "product";
    }

    @PostMapping("/remove")
    public String removeProductFromOrder(@RequestParam("id") Integer productId, Model model,
                                         Principal principal) {
        if (principal==null){
            log.info("in remove product from order: authentication == null, redirect to shop page");
            return "redirect:/shop";
        }
        UserDto userDto = userService.findByLogin(principal.getName());
        ProductDto productDto = productService.findById(productId);
        OrderDto orderDto = orderService.createOrderIfNotActive(userDto);
        orderProductService.removeProductFromOrder(orderDto, productDto);
        List<OrderProductDto> orderProductsDto = orderProductService
                .findByOrderId(orderService.createOrderIfNotActive(userDto).getId());
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        UserDto userDtoAfterRemove = userService.findByLogin(principal.getName());
        OrderDto orderDtoAfterRemove = orderService.createOrderIfNotActive(userDtoAfterRemove);
        model.addAttribute(ORDER_DTO, orderDtoAfterRemove);
        model.addAttribute(PRODUCTS_DTO, productsDto);
        model.addAttribute(CARD_DTO, new CardDto());
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in remove product from order: product {} removed from order {}", productDto, orderDto);
        return ORDER;
    }

    @GetMapping("/show_products")
    public String showProductsOrder(@RequestParam("id") Integer id, Model model, Principal principal) {

        if (principal==null||
                userService.findByLogin(principal.getName()).getOrdersDto().stream()
                        .noneMatch(item->item.getId().equals(id))){
            log.warn("in show products: redirect to shop page");
            return "redirect:/shop";
        }
        OrderDto orderDto = orderService.findById(id);
        List<OrderProductDto> orderProductsDto = orderDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        model.addAttribute(PRODUCTS_DTO, productsDto);
        model.addAttribute(ORDER_DTO, orderDto);
        model.addAttribute(CARD_DTO, new CardDto());
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in show products order: founded {} products in order {} for user {}",
                productsDto.size(), orderDto, userService.findByLogin(principal.getName()));
        return ORDER;
    }


    @PostMapping("/pay")
    public String getPayForm(@ModelAttribute("orderDto") OrderDto orderDto, Principal principal, Model model) {

        UserDto userDto = userService.findByLogin(principal.getName());
        OrderDto orderFromDbDto = orderService.findById(orderDto.getId());
        List<OrderProductDto> orderProductsDto = orderFromDbDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        List<CardDto> cardsDto = userDto.getCardsDto();

        if (userDto.getCardsDto().isEmpty()) {
            model.addAttribute(ERROR, 4);
            log.warn("in get pay form: cards not found for user {}", userDto);
        } else {
            model.addAttribute(ERROR, 6);
            log.info("in get pay form:found card for user {}", userDto);
        }
        model.addAttribute(PRODUCTS_DTO, productsDto);
        model.addAttribute(ORDER_DTO, orderFromDbDto);
        model.addAttribute(CARD_DTO, new CardDto());
        model.addAttribute(CARDS_DTO, cardsDto);
        model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
        log.info("in get pay form: return pay form for user {}", userDto);
        return ORDER;
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                           Principal principal, Model model) {
        OrderDto orderFromDbDto = orderService.createOrderIfNotActive(userService.findByLogin(principal.getName()));
        List<OrderProductDto> orderProductsDto = orderFromDbDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());

        if (bindingResult.hasErrors()) {
            model.addAttribute(PRODUCTS_DTO, productsDto);
            model.addAttribute(ORDER_DTO, orderFromDbDto);
            model.addAttribute(ERROR, 5);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.warn("in save card: binding result has {} errors", bindingResult.getErrorCount());
        } else {
            UserDto userDto = userService.findByLogin(principal.getName());
            cardService.save(cardDto, userDto);
            model.addAttribute(PRODUCTS_DTO, productsDto);
            model.addAttribute(ORDER_DTO, orderFromDbDto);
            model.addAttribute(CARD_DTO, cardDto);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            log.info("in save card: card {} saved for user {}", cardDto, userDto);
        }
        return ORDER;
    }

    @PostMapping("/pay_order")
    public String payOrderCard(@ModelAttribute("cardDto") CardDto cardDto, Principal principal, Model model) {
        String result = null;
        UserDto userDto = userService.findByLogin(principal.getName());
        OrderDto orderFromDbDto = orderService.createOrderIfNotActive(userService.findByLogin(userDto.getLogin()));
        List<OrderProductDto> orderProductsDto = orderFromDbDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        if (userDto.getCardsDto().stream().noneMatch(item -> item.getNumber().equals(cardDto.getNumber()))) {
            model.addAttribute(PRODUCTS_DTO, productsDto);
            model.addAttribute(ORDER_DTO, orderFromDbDto);
            model.addAttribute("noCard",true);
            model.addAttribute(CARD_DTO, new CardDto());
            model.addAttribute(CARDS_DTO, userDto.getCardsDto());
            model.addAttribute(ERROR, 6);
            model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
            result = ORDER;
            log.warn("in pay order: card {} not found", cardDto);
        } else {
            if (userDto.getCardsDto()
                    .stream()
                    .filter(item -> item.getNumber().equals(cardDto.getNumber()))
                    .findFirst()
                    .orElse(new CardDto()).getTotalAmount() < orderFromDbDto.getFullPrice()) {
                model.addAttribute(PRODUCTS_DTO, productsDto);
                model.addAttribute(ORDER_DTO, orderFromDbDto);
                model.addAttribute(CARD_DTO, new CardDto());
                model.addAttribute(CARDS_DTO, userDto.getCardsDto());
                model.addAttribute(ERROR, 7);
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
                result = ORDER;
                log.warn("in pay order: error pay, no money");
            }

            if (result == null) {
                orderService.payOrder(orderFromDbDto, cardDto);
                OrderDto orderDtoAfterPay = orderService.findById(orderFromDbDto.getId());
                model.addAttribute(PRODUCTS_DTO, productsDto);
                model.addAttribute(ORDER_DTO, orderDtoAfterPay);
                model.addAttribute(CARD_DTO, new CardDto());
                model.addAttribute(CARDS_DTO, userDto.getCardsDto());
                model.addAttribute(USER_PROF, userService.findByLogin(principal.getName()));
                result = ORDER;
                log.info("in pay order: order {} paid by card {} for user {}",orderDtoAfterPay, cardDto,userDto);
            }
        }
        return result;
    }
}
