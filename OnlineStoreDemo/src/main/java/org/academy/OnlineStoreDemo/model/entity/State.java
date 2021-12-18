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
@Table(name="state")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    private List<User> users;
}
