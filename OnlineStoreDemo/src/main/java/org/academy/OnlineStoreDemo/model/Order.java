package org.academy.OnlineStoreDemo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Column(name = "date")
    private Date date;

    @Column( name = "full_price")
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne
    private StateOrder stateOrder;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="order_product",
            joinColumns={@JoinColumn(name = "order_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id",referencedColumnName = "id")})
    private List<Product> products;
}
