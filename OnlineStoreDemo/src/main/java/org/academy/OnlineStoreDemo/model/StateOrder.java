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
@Table(name = "state_order")
public class StateOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="name", nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "stateOrder", fetch = FetchType.LAZY)
    private List<Order> orders;
}
