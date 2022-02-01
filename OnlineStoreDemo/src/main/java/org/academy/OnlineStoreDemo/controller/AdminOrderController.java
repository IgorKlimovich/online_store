package org.academy.OnlineStoreDemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.OrderProductDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.OrderService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/{id}")
    public String getOrderPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        OrderDto orderDto = orderService.findById(id);
        List<OrderProductDto> orderProductList = orderDto.getOrderProductsDto();
        List<ProductDto> productListDto = orderProductList
                .stream().map(OrderProductDto::getProductDto).collect(Collectors.toList());
        model.addAttribute(ORDER_DTO, orderDto)
                .addAttribute(PRODUCTS_DTO, productListDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in get orders page: founded {} products at order {}", productListDto.size(), orderDto);
      return ADMIN_ORDER;
    }

    @PostMapping("admin/order/deliver/{id}")
    public String deliverOrder(@PathVariable("id") Integer id, Model model, Principal principal) {
        List<OrderProductDto> orderProductListDto = orderService.setDelivered(id).getOrderProductsDto();
        List<ProductDto> productListDto = orderProductListDto
                .stream().map(OrderProductDto::getProductDto).collect(Collectors.toList());
        OrderDto orderDtoAfterDeliver = orderService.findById(id);
        model.addAttribute(ORDER_DTO, orderDtoAfterDeliver)
                .addAttribute(PRODUCTS_DTO, productListDto)
                .addAttribute(USER_PROF, userService.findByLogin(principal));
        log.info("in deliver order: order {} set delivered", orderDtoAfterDeliver);
        return ADMIN_ORDER;
    }
}
