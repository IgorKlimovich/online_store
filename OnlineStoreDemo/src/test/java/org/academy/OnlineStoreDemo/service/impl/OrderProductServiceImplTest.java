package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.model.repository.*;
import org.academy.OnlineStoreDemo.service.OrderProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderProductServiceImplTest {

    @Autowired
    private OrderProductService orderProductService;

    @MockBean
    private OrderProductRepository orderProductRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductCategoryRepository productCategoryRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private OrderProduct orderProduct;

    private OrderProductDto orderProductDto;

    private ProductDto productDto;

    private OrderDto orderDto;

    private ProductCategoryDto productCategoryDto;

    @BeforeEach
    public void setUp (){
        orderProduct = new OrderProduct();
        orderProduct.setId(1);
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setId(2);
        productDto= new ProductDto();
        orderDto = new OrderDto();
        productDto.setId(1);
        productDto.setAmount(5);
        productDto.setIsExist(true);
        productDto.setPrice(50.0);
        ProductDto productDto1 = new ProductDto();
        productDto1.setId(2);
        orderDto.setId(1);
        orderDto.setFullPrice(200.0);
        orderProductDto = new OrderProductDto();
        orderProductDto.setId(1);
        orderProductDto.setNewProductPrice(50.0);
        orderProductDto.setOrderDto(orderDto);
        OrderProductDto orderProductDto1 = new OrderProductDto();
        orderProductDto1.setId(2);
        orderProductDto.setProductDto(productDto);
        orderProductDto1.setProductDto(productDto1);
        List<OrderProductDto> orderProductsDto = new ArrayList<>();
        orderProductsDto.add(orderProductDto);
        orderProductsDto.add(orderProductDto1);
        orderDto.setOrderProductsDto(orderProductsDto);
        productCategoryDto= new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto.setAmount(10);
        productDto.setProductCategoryDto(productCategoryDto);
    }

    @Test
    void removeProductFromOrder() {
        User user = new User();
        user.setId(1);
        user.setLogin("login");
        Order order = modelMapper.map(orderDto,Order.class);
        order.setStateOrder(new StateOrder(1, "NEW"));
        List<Order> orders= new ArrayList<>();
        orders.add(order);
        user.setOrders(orders);
        Product product = modelMapper.map(productDto,Product.class);
        ProductCategory productCategory = modelMapper.map(productCategoryDto, ProductCategory.class);
        when(userRepository.findUserByLogin("login")).thenReturn(Optional.of(user));
        when(productCategoryRepository.getById(1)).thenReturn(productCategory);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(orderRepository.save(order)).thenReturn(order);
        orderProductService.removeProductFromOrder(product.getId(),user.getLogin());
        OrderProduct orderProduct = modelMapper.map(orderProductDto,OrderProduct.class);
        verify(productRepository,times(1)).save(product);
        verify(productCategoryRepository,times(1)).save(productCategory);
        verify(orderRepository,times(1)).save(order);
        verify(orderProductRepository,times(1)).delete(orderProduct);
    }

    @Test
    void saveProductToOrder() {
        Order order = modelMapper.map(orderDto, Order.class);
        ProductCategory productCategory = modelMapper.map(productCategoryDto, ProductCategory.class);
        Product product = modelMapper.map(productDto, Product.class);
        //  OrderProduct orderProduct = modelMapper.map(orderProductDto,OrderProduct.class);
        orderProductService.saveProductToOrder(orderDto, productDto);
        verify(orderRepository, times(1)).save(order);
        verify(productCategoryRepository, times(1)).save(productCategory);
        verify(productRepository, times(1)).save(product);
//        verify(orderProductRepository,times(1)).save(orderProduct);
    }

}