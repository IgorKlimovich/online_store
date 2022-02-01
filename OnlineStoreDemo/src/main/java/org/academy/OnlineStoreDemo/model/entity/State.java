package org.academy.OnlineStoreDemo.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
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
    @ToString.Exclude
    private List<User> users;

    public State(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public State(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        State state = (State) o;
        return id != null && Objects.equals(id, state.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
