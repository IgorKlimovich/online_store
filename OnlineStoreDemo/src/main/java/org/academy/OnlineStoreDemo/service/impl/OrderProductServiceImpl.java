package org.academy.OnlineStoreDemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.exception.OrderProductNotFoundException;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.model.repository.OrderProductRepository;
import org.academy.OnlineStoreDemo.model.repository.OrderRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductRepository;
import org.academy.OnlineStoreDemo.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    @Transactional
    public OrderDto removeProductFromOrder(Integer productId, String login) {
        ProductDto productDto = productService.findById(productId);
        OrderDto orderDto = orderService.createOrderIfNotActive(userService.findByLogin(login));
        Product product = modelMapper.map(productDto, Product.class);
        Order order = modelMapper.map(orderDto, Order.class);
        List<OrderProduct> orderProducts = order.getOrderProducts();
        OrderProduct orderProduct = orderProducts
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new OrderProductNotFoundException("order product not found"));
        product.setAmount(product.getAmount() + 1);
        product.setIsExist(product.getAmount() > 0);
        ProductCategory productCategory = productCategoryRepository.getById(product.getProductCategory().getId());
        productCategory.setAmount(productCategory.getAmount() + 1);
        order.setFullPrice(order.getFullPrice() - orderProduct.getNewProductPrice());
        productRepository.save(product);
        productCategoryRepository.save(productCategory);
        orderProducts.remove(orderProduct);
        orderProductRepository.delete(orderProduct);
        log.info("in remove product from order: product {} removed from order {}", product, order);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Override
    @Transactional
    public void saveProductToOrder(OrderDto orderDto, ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Order order = modelMapper.map(orderDto, Order.class);
        order.setFullPrice(order.getFullPrice() + product.getPrice());
        orderRepository.save(order);
        product.setAmount(product.getAmount() - 1);
        product.setIsExist(product.getAmount() > 0);
        ProductCategory productCategory = product.getProductCategory();
        productCategory.setAmount(productCategory.getAmount() - 1);
        productCategoryRepository.save(productCategory);
        productRepository.save(product);
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setNewProductPrice(product.getPrice());
        orderProductRepository.save(orderProduct);
        log.info("in save product to order: product {} saved to order {}", product, order);
    }
}
