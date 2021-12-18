package org.academy.OnlineStoreDemo.model.repository;

import org.academy.OnlineStoreDemo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByLogin(String login);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    Optional<User> findUserByLogin(String login);

    Boolean existsUserByEmail(String email);

    Boolean existsUserByLogin(String login);

    Boolean existsUserByPhoneNumber(String phoneNumber);


}
