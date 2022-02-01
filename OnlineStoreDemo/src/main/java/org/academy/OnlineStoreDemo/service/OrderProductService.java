package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;

public interface OrderProductService {

    /**
     * This method removes the product from the user's order
     * @param productId id of the product to be removed from the order
     * @param login login of the user in whose order the product will be removed
     * @return an updated order
     */
    OrderDto removeProductFromOrder(Integer productId, String login);

    /**
     * @param orderDto the order to which the product will be saved
     * @param productDto product to be added to the order
     */
    void saveProductToOrder(OrderDto orderDto, ProductDto productDto);
}
