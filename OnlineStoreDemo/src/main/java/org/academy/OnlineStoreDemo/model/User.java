package org.academy.OnlineStoreDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "user")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name",nullable = false)
    @NotEmpty (message = "имя не может быть пустое")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "фамилия не может быть пустой")
    private String lastName;

    @Column(name="login", nullable = false,unique = true)
    @NotEmpty(message = "логин не может быть пустым ")
    private String login;

    @Column(name = "password", nullable = false, unique = true)
    @NotEmpty(message = "пароль не может быть пустым")
    private String password;

    @Column(name="email", nullable = false, unique = true)
    @NotEmpty (message = "email не может быть пустым")
    @Email(message = "email должен содержать символы @ и .")
    private String email;

    @Column(name = "phone_number",nullable = false, unique = true)
    @Pattern(regexp = "[^a-zA-Z]{10,15}",
            message = "номер не может содержать буквы и должен содержать от 10 до 15 цифр")
    @NotEmpty(message = "номер не может быть пустым")
    private String phoneNumber;

    @ManyToOne
    private Role role;

    @ManyToOne
  //  @Column (name="state_id")
    private State state;

    @OneToMany(mappedBy = "user")
    List<Order> orders;
}
