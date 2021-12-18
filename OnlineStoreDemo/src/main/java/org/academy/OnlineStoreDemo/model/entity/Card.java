package org.academy.OnlineStoreDemo.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "название не может быть пустым")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "номер не может быть пустым")
    @Pattern(regexp = "[0-9]{16}",
            message = "номер должен содержать 16 цифр")
    @Column(name="number")
    private String number;

    @Pattern(regexp = "[0-9]{3}",
            message = "CVV должен содержать 3 цифры")
    @NotEmpty(message = "CVV не может быть пустым")
    @Column(name="cvv")
    private String cvv;

    @Column(name = "total_amount")
    private Double totalAmount;

    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", CVV='" + cvv + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Card card = (Card) o;
        return id != null && Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
