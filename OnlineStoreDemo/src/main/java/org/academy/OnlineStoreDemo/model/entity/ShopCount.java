package org.academy.OnlineStoreDemo.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "shop_count")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShopCount shopCount = (ShopCount) o;
        return id != null && Objects.equals(id, shopCount.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
