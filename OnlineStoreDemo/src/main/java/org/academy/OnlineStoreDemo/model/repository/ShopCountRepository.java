package org.academy.OnlineStoreDemo.model.repository;

import org.academy.OnlineStoreDemo.model.entity.ShopCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopCountRepository extends JpaRepository<ShopCount, Integer> {

}
