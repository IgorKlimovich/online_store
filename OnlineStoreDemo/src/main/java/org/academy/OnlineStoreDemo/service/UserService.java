package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.User;

import java.util.List;

public interface UserService {
    void create(UserForm userForm);

    User findByLogin (String login);
    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
    List<User> findAll();
    User findById(Integer id);
    void toBan(Integer id);
    void unBan(Integer id);
}
