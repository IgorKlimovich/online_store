package org.academy.OnlineStoreDemo.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    private Integer id;

    @NotEmpty(message = "имя не может быть пустое")
    @Size(max = 100, message = "имя не может содержать более 100 символов")
    private String firstName;


    @NotEmpty(message = "фамилия не может быть пустой")
    @Size(max=100, message = "фамилия не может содержать более 100 символов")
    private String lastName;


    @NotEmpty(message = "логин не может быть пустым ")
    @Size(max=100, message = "фамилия не может содержать более 100 символов")
    private String login;


    @NotEmpty(message = "пароль не может быть пустым")
    @Size(max=100, message = "пароль не может содержать более 100 символов")
    private String password;


    @NotEmpty (message = "email не может быть пустым")
    @Email(message = "email должен содержать символы @ и .")
    @Size(max=100, message = "email не может содержать более 100 символов")
    private String email;


    @Pattern(regexp = "[^a-zA-Z]{10,15}",
            message = "номер не может содержать буквы и должен содержать от 10 до 15 цифр")
    private String phoneNumber;


}
