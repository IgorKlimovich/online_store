package org.academy.OnlineStoreDemo.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "current_dates")
    private Date date;

    @Column( name = "full_price")
    private Double fullPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne
    private StateOrder stateOrder;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name="order_product",
//            joinColumns={@JoinColumn(name = "order_id",referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "product_id",referencedColumnName = "id")})

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts ;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", fullPrice=" + fullPrice +
                '}';
    }
}
