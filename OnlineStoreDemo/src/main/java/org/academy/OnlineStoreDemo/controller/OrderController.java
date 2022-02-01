package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.service.*;
import org.academy.OnlineStoreDemo.valid.OrderPayValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller()
@RequestMapping("/orders")
public class OrderController {

    private final CardService cardService;
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderPayValidator orderPayValidator;
    private final OrderProductService orderProductService;

    @GetMapping
    public String getOrdersPage(Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal);
        model.addAttribute(USER_DTO, userDto)
                .addAttribute(USER_PROF, userDto);
        log.info("in get orders page: founded orders for user {}", userDto);
        return ORDERS;
    }

    @PostMapping("/add")
    public String addProductToOrder(@RequestParam("id") Integer productId, Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal);
        OrderDto orderDto = orderService.createOrderIfNotActive(userDto);
        ProductDto productDto = productService.findById(productId);
        if (Boolean.FALSE.equals(productDto.getIsExist())) {
            model.addAttribute(ERROR, PRODUCT_NOT_EXIST)
                    .addAttribute(PRODUCT_DTO, productDto)
                    .addAttribute(USER_PROF, userDto);
            log.warn("in add product to order: product {} not exist", productDto);
            return PRODUCT;
        }
        orderProductService.saveProductToOrder(orderDto, productDto);
        ProductDto productDtoAfterSave = productService.findById(productId);
        model.addAttribute(PRODUCT_DTO, productDtoAfterSave)
                .addAttribute(ERROR, PRODUCT_EXIST)
                .addAttribute(USER_PROF, userDto);
        log.info("in add product to order: product {} added to order {} for user {}", productDto, orderDto, userDto);
        return PRODUCT;
    }

    @PostMapping("/remove")
    public String removeProductFromOrder(@RequestParam("id") Integer productId, Model model, Principal principal) {
        OrderDto orderDto = orderProductService.removeProductFromOrder(productId, principal.getName());
        List<ProductDto> productsDto = orderDto.getOrderProductsDto().stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        model.addAttribute(ORDER_DTO, orderDto)
                .addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in remove product from order: product with id {} removed from order {}", productId, orderDto);
        return ORDER;
    }

    @GetMapping("/show_products")
    public String showProductsOrder(@RequestParam("id") Integer id, Model model, Principal principal) {
        OrderDto orderDto = orderService.findById(id);
        List<ProductDto> productsDto = orderDto.getOrderProductsDto().stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        model.addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(ORDER_DTO, orderDto)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in show products order: founded {} products in order {} for user {}",
                productsDto.size(), orderDto, userService.findByLogin(principal));
        return ORDER;
    }

    @PostMapping("/pay")
    public String getPayForm(@ModelAttribute("orderDto") OrderDto orderDto, Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal);
        OrderDto orderFromDbDto = orderService.findById(orderDto.getId());
        List<ProductDto> productsDto = orderFromDbDto.getOrderProductsDto().stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        if (userDto.getCardsDto().isEmpty()) {
            model.addAttribute(ERROR, CARD_NOT_EXIST);
            log.warn("in get pay form: cards not found for user {}", userDto);
        } else {
            model.addAttribute(ERROR, CARD_EXIST);
            log.info("in get pay form:found card for user {}", userDto);
        }
        model.addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(ORDER_DTO, orderFromDbDto)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(USER_PROF, userDto);
        log.info("in get pay form: return pay form for user {}", userDto);
        return ORDER;
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                           Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal);
        OrderDto orderFromDbDto = orderService.createOrderIfNotActive(userDto);
        List<ProductDto> productsDto = orderFromDbDto.getOrderProductsDto().stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        if (bindingResult.hasErrors()) {
            model.addAttribute(PRODUCTS_DTO, productsDto)
                    .addAttribute(ORDER_DTO, orderFromDbDto)
                    .addAttribute(ERROR, RETURN_FORM)
                    .addAttribute(CARD_DTO, cardDto)
                    .addAttribute(USER_PROF, userDto);
            log.warn("in save card: binding result has {} errors", bindingResult.getErrorCount());
        } else {
            cardService.save(cardDto, userDto);
            model.addAttribute(PRODUCTS_DTO, productsDto)
                    .addAttribute(ORDER_DTO, orderFromDbDto)
                    .addAttribute(CARD_DTO, cardDto)
                    .addAttribute(USER_PROF, userDto);
            log.info("in save card: card {} saved for user {}", cardDto, userDto);
        }
        return ORDER;
    }

    @PostMapping("/pay_order")
    public String payOrderCard(@ModelAttribute("cardDto") CardDto cardDto, Principal principal, Model model) {
        UserDto userDto = userService.findByLogin(principal);
        OrderDto orderFromDbDto = orderService.createOrderIfNotActive(userDto);
        List<ProductDto> productsDto = orderFromDbDto.getOrderProductsDto().stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        Integer message = orderPayValidator.validatePay(userDto, cardDto, orderFromDbDto);
        if (message != null) {
            model.addAttribute(PRODUCTS_DTO, productsDto).addAttribute(ORDER_DTO, orderFromDbDto)
                    .addAttribute(NO_CARD, true)
                    .addAttribute(CARD_DTO, new CardDto())
                    .addAttribute(ERROR, message)
                    .addAttribute(USER_PROF, userDto);
            log.warn("in pay order: order {} do not paid by card {} for user {}", orderFromDbDto, cardDto, userDto);
            return ORDER;
        }
        orderService.payOrder(orderFromDbDto, cardDto);
        OrderDto orderDtoAfterPay = orderService.findById(orderFromDbDto.getId());
        model.addAttribute(PRODUCTS_DTO, productsDto)
                .addAttribute(ORDER_DTO, orderDtoAfterPay)
                .addAttribute(CARD_DTO, new CardDto())
                .addAttribute(USER_PROF, userDto);
        log.info("in pay order: order {} paid by card {} for user {}", orderDtoAfterPay, cardDto, userDto);
        return ORDER;
    }
}

