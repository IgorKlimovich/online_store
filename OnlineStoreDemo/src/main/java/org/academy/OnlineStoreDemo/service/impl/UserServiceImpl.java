package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.form.UserForm;
import org.academy.OnlineStoreDemo.model.Order;
import org.academy.OnlineStoreDemo.model.Role;
import org.academy.OnlineStoreDemo.model.State;
import org.academy.OnlineStoreDemo.model.User;
import org.academy.OnlineStoreDemo.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    @Override
    public void create(UserForm userForm) {
        User user = new User();
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setLogin(userForm.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        Role role = new Role();
        State state = new State();
        List<Order> orderList = new ArrayList<>();
        state.setId(1);
        role.setId(1);
        user.setRole(role);
        user.setState(state);
        user.setOrders(orderList);

        userRepository.save(user);

    }

    public User findByEmail(String email) {
        User user= userRepository.findByEmail(email);
        if (user==null){
            try {
                throw new NullPointerException("user not found");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        return user;
    }

    public User findByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Integer id){
        Optional<User> candidate=userRepository.findById(id);
        return candidate.orElseGet(User::new);
    }

    public void toBan(Integer id){
        User user =findById(id);
        State state = new State();
        state.setId(2);
        state.setName("BANNED");
        user.setState(state);
        System.out.println(user.getState().getName());
        userRepository.save(user);
    }

    public void unBan(Integer id){
        User user =findById(id);
        State state= new State();
        state.setId(1);
        state.setName("ACTIVE");
        user.setState(state);
        userRepository.save(user);
    }
}

