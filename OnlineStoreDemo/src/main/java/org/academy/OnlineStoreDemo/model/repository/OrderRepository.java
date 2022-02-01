package org.academy.OnlineStoreDemo.model.repository;

import org.academy.OnlineStoreDemo.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
