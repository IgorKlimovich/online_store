package org.academy.OnlineStoreDemo.repository;

import org.academy.OnlineStoreDemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByLogin(String login);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    Optional<User> findUserByLogin(String login);


}
