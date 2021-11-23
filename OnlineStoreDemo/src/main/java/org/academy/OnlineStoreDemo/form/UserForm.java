package org.academy.OnlineStoreDemo.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {


    @NotEmpty(message = "имя не может быть пустое")
    private String firstName;


    @NotEmpty(message = "фамилия не может быть пустой")
    private String lastName;


    @NotEmpty(message = "логин не может быть пустым ")
    private String login;


    @NotEmpty(message = "пароль не может быть пустым")
    private String password;


    @NotEmpty (message = "email не может быть пустым")
    @Email(message = "email должен содержать символы @ и .")
    private String email;


    @Pattern(regexp = "[^a-zA-Z]{10,15}",
            message = "номер не может содержать буквы и должен содержать от 10 до 15 цифр")
    @NotEmpty(message = "номер не может быть пустым")
    private String phoneNumber;


}
