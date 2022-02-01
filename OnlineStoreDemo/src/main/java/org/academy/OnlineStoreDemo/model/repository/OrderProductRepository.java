package org.academy.OnlineStoreDemo.model.repository;


import org.academy.OnlineStoreDemo.model.entity.OrderProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
}
