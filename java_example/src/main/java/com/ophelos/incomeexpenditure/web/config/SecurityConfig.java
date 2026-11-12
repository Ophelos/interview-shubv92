package com.ophelos.incomeexpenditure.web.config;

import com.ophelos.incomeexpenditure.services.IncomeExpenditureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Customized security configuration for using database-backed user authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    IncomeExpenditureService incomeExpenditureService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                com.ophelos.incomeexpenditure.api.User user = incomeExpenditureService.findUserByUsername(username);

                if (user == null) {
                    throw new UsernameNotFoundException("Could not find user");
                }

                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                // Plain password is "p@ssw0rd!";

                return User.withUsername(username)
                        .password(user.getPassword())
                        .roles("USER")
                        .build();
            }
        };
    }

}
