package org.academy.OnlineStoreDemo.controller;

<<<<<<< HEAD
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.OrderProductDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
=======
>>>>>>> origin/master
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.OrderProduct;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public String getOrdersPage(@PathVariable("id") Integer id, Model model){
<<<<<<< HEAD
        OrderDto orderDto=orderService.findById(id);
        List<OrderProductDto> orderProductList = orderDto.getOrderProductsDto();
        List<ProductDto> productListDto=orderProductList.stream().map(OrderProductDto::getProductDto).collect(Collectors.toList());

        model.addAttribute("orderDto", orderDto);
        model.addAttribute("productsDto",productListDto);
=======
        Order order=orderService.findById(id);
        List<OrderProduct> orderProductList = order.getOrderProducts();
        List<Product> productList=orderProductList.stream().map(OrderProduct::getProduct).collect(Collectors.toList());

        model.addAttribute("order", order);
        model.addAttribute("products",productList);
>>>>>>> origin/master
        return "adminOrder";
    }

    @PostMapping("admin/order/deliver/{id}")
    public String deliverOrder(@PathVariable("id")Integer id, Model model){

<<<<<<< HEAD
        OrderDto  orderDto=orderService.findById(id);
        orderService.setDelivered(orderDto);
        List<OrderProductDto> orderProductListDto = orderDto.getOrderProductsDto();
        List<ProductDto> productListDto=orderProductListDto.stream().map(OrderProductDto::getProductDto).collect(Collectors.toList());
        OrderDto orderDtoAfterDeliver=orderService.findById(id);
        model.addAttribute("orderDto", orderDtoAfterDeliver);
        model.addAttribute("productsDto",productListDto);
=======
        Order order=orderService.findById(id);
       orderService.setDelivered(order);
        List<OrderProduct> orderProductList = order.getOrderProducts();
        List<Product> productList=orderProductList.stream().map(OrderProduct::getProduct).collect(Collectors.toList());

        model.addAttribute("order", order);
        model.addAttribute("products",productList);
>>>>>>> origin/master
        return "adminOrder";
    }
}
