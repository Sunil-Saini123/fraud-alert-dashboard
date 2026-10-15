package com.example.fraudalert.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Actuator health is public
                .requestMatchers("/actuator/health").permitAll()
                // H2 console — admin only in dev
                .requestMatchers("/h2-console/**").hasRole("ADMIN")
                // User management — admin only
                .requestMatchers("/users/**").hasRole("ADMIN")
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Allow H2 console iframes in dev
            .headers(headers -> headers.frameOptions(fo -> fo.sameOrigin()))
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            );

        return http.build();
    }
}
