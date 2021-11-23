package org.academy.OnlineStoreDemo.security;

import org.academy.OnlineStoreDemo.security.details.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final PersistentTokenRepository jdbcTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(PersistentTokenRepository jdbcTokenRepository,
                           UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jdbcTokenRepository = jdbcTokenRepository;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/shop/**").permitAll()
                .antMatchers("/demo/**").permitAll()
                .antMatchers("/sign-up/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("login")
                .defaultSuccessUrl("/shop")
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .tokenRepository(jdbcTokenRepository);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
