package org.academy.OnlineStoreDemo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name ="order_product")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    @Column(name = "price_product")
    private Double productPrice;


}
