package org.academy.OnlineStoreDemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.OrderProductDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.service.OrderService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AdminOrderController(OrderService orderService, UserService userService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public String getOrderPage(@PathVariable("id") Integer id, Model model, Principal principal){
        OrderDto orderDto=orderService.findById(id);
        List<OrderProductDto> orderProductList = orderDto.getOrderProductsDto();
        List<ProductDto> productListDto=orderProductList.stream().map(OrderProductDto::getProductDto).collect(Collectors.toList());
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("productsDto",productListDto);
        if (principal!=null){
            model.addAttribute("userProf" ,userService.findByLogin(principal.getName()));
                //    modelMapper.map(userService.findByLogin(principal.getName()), UserForm.class));
        }
        log.info("in get orders page: founded {} products at order {}", productListDto.size(),orderDto);
        return "adminOrder";
    }

    @PostMapping("admin/order/deliver/{id}")
    public String deliverOrder(@PathVariable("id")Integer id, Model model, Principal principal){

        OrderDto  orderDto=orderService.findById(id);
        orderService.setDelivered(orderDto);
        List<OrderProductDto> orderProductListDto = orderDto.getOrderProductsDto();
        List<ProductDto> productListDto=orderProductListDto.stream().map(OrderProductDto::getProductDto).collect(Collectors.toList());
        OrderDto orderDtoAfterDeliver=orderService.findById(id);
        model.addAttribute("orderDto", orderDtoAfterDeliver);
        model.addAttribute("productsDto",productListDto);
        if (principal!=null){
            model.addAttribute("userProf" ,userService.findByLogin(principal.getName()));
                 //   modelMapper.map(userService.findByLogin(principal.getName()), UserForm.class));
        }
        log.info("in deliver order: order {} set delivered", orderDto);
        return "adminOrder";
    }
}
