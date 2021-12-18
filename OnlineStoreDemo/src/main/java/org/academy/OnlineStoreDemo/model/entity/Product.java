package org.academy.OnlineStoreDemo.model.entity;

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

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne()
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", isExist=" + isExist +
                ", amount=" + amount +
                '}';
    }
}
