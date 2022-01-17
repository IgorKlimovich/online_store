package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.OrderProductDto;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.OrderProduct;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.repository.OrderProductRepository;
import org.academy.OnlineStoreDemo.model.repository.OrderRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductRepository;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Autowired
    private ModelMapper modelMapper;

    private OrderProduct orderProduct;

    private OrderProductDto orderProductDto;

    private List<OrderProduct> orderProducts;

    private ProductDto productDto;

    private OrderDto orderDto;

    private ProductCategoryDto productCategoryDto;

    @BeforeEach
    public void setUp (){
        orderProduct = new OrderProduct();
        orderProduct.setId(1);
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setId(2);
        orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct);
        orderProducts.add(orderProduct1);
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
    void save() {
        orderProductService.save(orderProduct);
        verify(orderProductRepository,times(1)).save(orderProduct);
    }

    @Test
    void removeProductFromOrder() {
        Product product = modelMapper.map(productDto,Product.class);
        ProductCategory productCategory = modelMapper.map(productCategoryDto, ProductCategory.class);
        when(productCategoryRepository.getById(1)).thenReturn(productCategory);
        orderProductService.removeProductFromOrder(orderDto,productDto);
        Order order = modelMapper.map(orderDto,Order.class);
        OrderProduct orderProduct = modelMapper.map(orderProductDto,OrderProduct.class);
        verify(productRepository,times(1)).save(product);
        verify(productCategoryRepository,times(1)).save(productCategory);
        verify(orderRepository,times(1)).save(order);
        verify(orderProductRepository,times(1)).delete(orderProduct);
    }

    @Test
    void saveProductToOrder() {
        Order order = modelMapper.map(orderDto,Order.class);
        ProductCategory productCategory = modelMapper.map(productCategoryDto,ProductCategory.class);
        Product product = modelMapper.map(productDto,Product.class);
      //  OrderProduct orderProduct = modelMapper.map(orderProductDto,OrderProduct.class);
        orderProductService.saveProductToOrder(orderDto,productDto);
        verify(orderRepository,times(1)).save(order);
        verify(productCategoryRepository,times(1)).save(productCategory);
        verify(productRepository,times(1)).save(product);
//        verify(orderProductRepository,times(1)).save(orderProduct);
    }

    @Test
    void findByOrderId() {
        when(orderProductRepository.findAllByOrderId(1)).thenReturn(orderProducts);
        List<OrderProductDto> orderProductsDto = orderProductService.findByOrderId(1);
        verify(orderProductRepository,times(1)).findAllByOrderId(1);
        assertEquals(2,orderProductsDto.size());
        assertEquals(1,orderProductsDto.get(0).getId());
    }

    @Test
    void findByOrderIdFail() {
        when(orderProductRepository.findAllByOrderId(1)).thenReturn(Collections.emptyList());
        List<OrderProductDto> orderProductsDto = orderProductService.findByOrderId(1);
        verify(orderProductRepository,times(1)).findAllByOrderId(1);
        assertEquals(0,orderProductsDto.size());
    }
}