package com.example.fraudalert.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.fraudalert.model.AppUser;
import com.example.fraudalert.model.Role;
import com.example.fraudalert.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----- UserDetailsService -----

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    // ----- CRUD -----

    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Creates a new user with the plain-text password encoded automatically.
     */
    public AppUser createUser(String username, String plainPassword, Role role) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(plainPassword));
        user.setRole(role);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AppUser findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
