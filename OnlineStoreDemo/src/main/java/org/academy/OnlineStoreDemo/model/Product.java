package org.academy.OnlineStoreDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name="price",nullable = false)
    private Double price;

    @Column(name="is_exist", nullable = false)
    private Boolean isExist;

    @ManyToOne()
   // @Column(name="category_id")
    private ProductCategory productCategory;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;
}
