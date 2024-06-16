package com.pet.petshop.auth.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pet.petshop.auth.dto.AuthResponse;
import com.pet.petshop.auth.dto.LoginRequest;

import com.pet.petshop.auth.dto.UpdateUserProfileRequest;

import com.pet.petshop.auth.entity.User;
import com.pet.petshop.auth.jwt.JwtService;
import com.pet.petshop.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(new User());
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Integer id, UpdateUserProfileRequest request) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }


    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials", e);
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtService.getToken(user);
        String username = user.getUsername();
       String email = user.getEmail();
        String role = user.getRole().name();

        return AuthResponse.builder()
                .token(token)
                .username(username)
                .email(email)
                .role(role)
                .build();
    }
    
      

}
