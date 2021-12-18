package org.academy.OnlineStoreDemo.security.details;

import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> candidate =userRepository.findUserByLogin(login);
        if (candidate.isPresent()){
            return new UserDetailsImpl(candidate.get());
        }
        else throw new  UsernameNotFoundException("user not found");
    }
}
