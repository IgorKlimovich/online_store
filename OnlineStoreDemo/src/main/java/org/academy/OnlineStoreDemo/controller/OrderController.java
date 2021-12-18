package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductDto;
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
        User user = userService.findByLogin(login);
        model.addAttribute("user", user);
        List<Order> orders = user.getOrders();
        model.addAttribute("orders", orders);
        return "/orders";
    }

    @PostMapping("/add")
    public String addProductToOrder(@RequestParam("id") Integer productId, Principal principal,
                                    Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByLogin(principal.getName());
        Order order = orderService.createOrderIfNotActive(user);
        ProductDto productDto = productService.findById(productId);
        if (Boolean.FALSE.equals(productDto.getIsExist())) {
            model.addAttribute("error", 3);
            model.addAttribute("productDto", productDto);
            return "product";
        }
        orderProductService.saveProductToOrder(order, productDto);
        ProductDto productDtoAfterSave = productService.findById(productId);
        model.addAttribute("productDto", productDtoAfterSave);
        model.addAttribute("error", 12);
        return "product";
     //   return "redirect:/orders";
    }

    @PostMapping("/remove")
    public String removeProductFromOrder(@RequestParam("id") Integer productId, Model model,
                                         Principal principal) {
        User user = userService.findByLogin(principal.getName());
        ProductDto productDto = productService.findById(productId);
        Order order = orderService.createOrderIfNotActive(user);
        orderProductService.removeProductFromOrder(order, productDto);
        List<OrderProduct> orderProducts = orderProductService
                .findByOrderId(orderService.createOrderIfNotActive(user).getId());
        List<Product> products = orderProducts.stream()
                .map(OrderProduct::getProduct).collect(Collectors.toList());
        model.addAttribute("products", products);
        model.addAttribute("order", order);
        model.addAttribute("products", products);
        model.addAttribute("card", new Card());
        return "order";
    }

    @GetMapping("/show_products")
    public String showProductsOrder(@RequestParam("id") Integer id, Model model) {

        Order order = orderService.findById(id);
        List<OrderProduct> orderProducts = order.getOrderProducts();
        List<Product> products = orderProducts.stream()
                .map(OrderProduct::getProduct).collect(Collectors.toList());
        model.addAttribute("products", products);
        model.addAttribute("order", order);
        model.addAttribute("card", new Card());
        return "order";
    }


    @PostMapping("/pay")
    public String payOrder(@ModelAttribute("order") Order order, Principal principal, Model model) {

        User user = userService.findByLogin(principal.getName());
        Order orderFromDb = orderService.findById(order.getId());
        List<OrderProduct> orderProducts = orderFromDb.getOrderProducts();
        List<Product> products = orderProducts.stream()
                .map(OrderProduct::getProduct).collect(Collectors.toList());
        List<Card> cards = user.getCards();

        if (user.getCards().isEmpty()){
            model.addAttribute("error",4);
        }
        else {
            model.addAttribute("error" ,6);
        }
        model.addAttribute("products", products);
        model.addAttribute("order", orderFromDb);
        model.addAttribute("card", new Card());
        model.addAttribute("cards", cards);
        return "order";
    }

    @PostMapping("/save_card")
    public String saveCard(@ModelAttribute("card") @Valid Card card, BindingResult bindingResult,
                           Principal principal, Model model) {

        Order orderFromDb = orderService.createOrderIfNotActive(userService.findByLogin(principal.getName()));
        List<OrderProduct> orderProducts = orderFromDb.getOrderProducts();
        List<Product> products = orderProducts.stream()
                .map(OrderProduct::getProduct).collect(Collectors.toList());

        if (bindingResult.hasErrors()) {
            model.addAttribute("products", products);
            model.addAttribute("order", orderFromDb);
            model.addAttribute("error", 5);
            model.addAttribute("card", card);
            return "order";
        }
        User user = userService.findByLogin(principal.getName());
        cardService.save(card, user);
        model.addAttribute("products", products);
        model.addAttribute("order", orderFromDb);
        model.addAttribute("card", card);

        return "order";
    }


    @PostMapping("/pay_order")
    public String payOrderCard(@ModelAttribute Card card, Principal principal, Model model) {

        User user = userService.findByLogin(principal.getName());
        Order orderFromDb = orderService.createOrderIfNotActive(userService.findByLogin(user.getLogin()));
        List<OrderProduct> orderProducts = orderFromDb.getOrderProducts();
        List<Product> products = orderProducts.stream()
                .map(OrderProduct::getProduct).collect(Collectors.toList());
        if (user.getCards().stream().noneMatch(item -> item.getNumber().equals(card.getNumber()))) {

            model.addAttribute("products", products);
            model.addAttribute("order", orderFromDb);
            model.addAttribute("errorName", "такой карты нету");
            model.addAttribute("card", new Card());
            model.addAttribute("cards", user.getCards());
            model.addAttribute("error", 6);
            return "order";
        }
        try {
            if (user.getCards()
                    .stream()
                    .filter(item->item.getNumber().equals(card.getNumber()))
                    .findFirst()
                    .orElseThrow(Exception::new).getTotalAmount()<orderFromDb.getFullPrice()){
                model.addAttribute("products", products);
                model.addAttribute("order", orderFromDb);
                model.addAttribute("card", new Card());
                model.addAttribute("cards", user.getCards());
                model.addAttribute("error", 7);
                return "order";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderService.payOrder(orderFromDb,card);
        model.addAttribute("products", products);
        model.addAttribute("order", orderFromDb);
        model.addAttribute("card", new Card());
        model.addAttribute("cards", user.getCards());
        return "order";
    }
}
