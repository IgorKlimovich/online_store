package org.academy.OnlineStoreDemo.security.details;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> candidate =userRepository.findUserByLogin(login);
        if (candidate.isPresent()){
            log.info("in load user by username: user with login {} loaded",login);
            return new UserDetailsImpl(candidate.get());
        }
        else throw new  UsernameNotFoundException("user not found");
    }
}
