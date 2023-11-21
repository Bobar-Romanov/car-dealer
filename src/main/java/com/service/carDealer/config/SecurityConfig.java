package com.service.carDealer.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/cars/home","/cars/home/*","/registration","/resources/**", "/css/*", "/js/*")
                .permitAll()
                .requestMatchers(("/cars/admin/**")).hasRole("ADMIN")
                .anyRequest()
                .authenticated());

        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/cars/home")
                        .permitAll()
                );

        http.csrf((csrf) -> csrf
                .ignoringRequestMatchers("/***")
        );
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll());

        return http.build();

    }
}