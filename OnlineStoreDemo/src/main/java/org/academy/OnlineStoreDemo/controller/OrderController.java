package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.model.entity.*;
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

@Controller()
@RequestMapping("/orders")
public class OrderController {



    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final CardService cardService;

    public OrderController(UserService userService, ProductService productService,
                           OrderService orderService, OrderProductService orderProductService, CardService cardService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.cardService = cardService;
    }

    @GetMapping
    public String getOrdersPage(Principal principal, Model model, Authentication authentication) {
        if (authentication == null) {
            return "login";
        }
        String login = principal.getName();
        UserDto userDto = userService.findByLogin(login);
        model.addAttribute("userDto", userDto);
        List<OrderDto> ordersDto = userDto.getOrdersDto();
        model.addAttribute("ordersDto", ordersDto);
        return "/orders";
    }

    @PostMapping("/add")
    public String addProductToOrder(@RequestParam("id") Integer productId, Principal principal,
                                    Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        UserDto userDto = userService.findByLogin(principal.getName());
        OrderDto orderDto = orderService.createOrderIfNotActive(userDto);
        ProductDto productDto = productService.findById(productId);
        if (Boolean.FALSE.equals(productDto.getIsExist())) {
            model.addAttribute("error", 3);
            model.addAttribute("productDto", productDto);
            return "product";
        }
        orderProductService.saveProductToOrder(orderDto, productDto);
        ProductDto productDtoAfterSave = productService.findById(productId);
        model.addAttribute("productDto", productDtoAfterSave);
        model.addAttribute("error", 12);
        return "product";
     //   return "redirect:/orders";
    }

    @PostMapping("/remove")
    public String removeProductFromOrder(@RequestParam("id") Integer productId, Model model,
                                         Principal principal) {
        UserDto userDto = userService.findByLogin(principal.getName());
        ProductDto productDto = productService.findById(productId);
        OrderDto orderDto = orderService.createOrderIfNotActive(userDto);
        orderProductService.removeProductFromOrder(orderDto, productDto);
        List<OrderProductDto> orderProductsDto = orderProductService
                .findByOrderId(orderService.createOrderIfNotActive(userDto).getId());
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        UserDto userDtoAfterRemove = userService.findByLogin(principal.getName());
        OrderDto orderDtoAfterRemove=orderService.createOrderIfNotActive(userDtoAfterRemove);
        model.addAttribute("orderDto", orderDtoAfterRemove);
     model.addAttribute("productsDto", productsDto);
        model.addAttribute("cardDto", new CardDto());
        return "order";
    }

    @GetMapping("/show_products")
    public String showProductsOrder(@RequestParam("id") Integer id, Model model) {

        OrderDto orderDto = orderService.findById(id);
        List<OrderProductDto> orderProductsDto = orderDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        model.addAttribute("productsDto", productsDto);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("cardDto", new CardDto());
        return "order";
    }


    @PostMapping("/pay")
    public String payOrder(@ModelAttribute("orderDto") OrderDto orderDto, Principal principal, Model model) {

        UserDto userDto = userService.findByLogin(principal.getName());
        OrderDto orderFromDbDto = orderService.findById(orderDto.getId());
        List<OrderProductDto> orderProductsDto = orderFromDbDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        List<CardDto> cardsDto = userDto.getCardsDto();

        if (userDto.getCardsDto().isEmpty()){
            model.addAttribute("error",4);
        }
        else {
            model.addAttribute("error" ,6);
        }

//        UserDto userDtoAfterPay = userService.findByLogin(principal.getName());
//        OrderDto orderDtoAfterPay=orderService.createOrderIfNotActive(userDtoAfterPay);
//        System.out.println(orderDtoAfterPay.getStateOrderDto().getName());
        model.addAttribute("productsDto", productsDto);
        model.addAttribute("orderDto", orderFromDbDto);
        model.addAttribute("cardDto", new CardDto());
        model.addAttribute("cardsDto", cardsDto);
        return "order";
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("cardDto") @Valid CardDto cardDto, BindingResult bindingResult,
                           Principal principal, Model model) {

        OrderDto orderFromDbDto = orderService.createOrderIfNotActive(userService.findByLogin(principal.getName()));
        List<OrderProductDto> orderProductsDto = orderFromDbDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());

        if (bindingResult.hasErrors()) {
            model.addAttribute("productsDto", productsDto);
            model.addAttribute("orderDto", orderFromDbDto);
            model.addAttribute("error", 5);
            model.addAttribute("cardDto", cardDto);
            return "order";
        }
        UserDto userDto = userService.findByLogin(principal.getName());
        cardService.save(cardDto, userDto);
        model.addAttribute("productsDto", productsDto);
        model.addAttribute("orderDto", orderFromDbDto);
        model.addAttribute("cardDto", cardDto);

        return "order";
    }


    @PostMapping("/pay_order")
    public String payOrderCard(@ModelAttribute ("cardDto") CardDto cardDto, Principal principal, Model model) {

        UserDto userDto = userService.findByLogin(principal.getName());
        OrderDto orderFromDbDto = orderService.createOrderIfNotActive(userService.findByLogin(userDto.getLogin()));
        List<OrderProductDto> orderProductsDto = orderFromDbDto.getOrderProductsDto();
        List<ProductDto> productsDto = orderProductsDto.stream()
                .map(OrderProductDto::getProductDto).collect(Collectors.toList());
        if (userDto.getCardsDto().stream().noneMatch(item -> item.getNumber().equals(cardDto.getNumber()))) {

            model.addAttribute("productsDto", productsDto);
            model.addAttribute("orderDto", orderFromDbDto);
            model.addAttribute("errorName", "такой карты нету");
            model.addAttribute("cardDto", new CardDto());
            model.addAttribute("cardsDto", userDto.getCardsDto());
            model.addAttribute("error", 6);
            return "order";
        }
        try {
            if (userDto.getCardsDto()
                    .stream()
                    .filter(item->item.getNumber().equals(cardDto.getNumber()))
                    .findFirst()
                    .orElseThrow(Exception::new).getTotalAmount()<orderFromDbDto.getFullPrice()){
                model.addAttribute("productsDto", productsDto);
                model.addAttribute("orderDto", orderFromDbDto);
                model.addAttribute("cardDto", new CardDto());
                model.addAttribute("cardsDto", userDto.getCardsDto());
                model.addAttribute("error", 7);
                return "order";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderService.payOrder(orderFromDbDto,cardDto);
        OrderDto orderDtoAfterPay =orderService.findById(orderFromDbDto.getId());
        model.addAttribute("productsDto", productsDto);
        model.addAttribute("orderDto", orderDtoAfterPay);
        model.addAttribute("cardDto", new CardDto());
        model.addAttribute("cardsDto", userDto.getCardsDto());
        return "order";
    }
}
