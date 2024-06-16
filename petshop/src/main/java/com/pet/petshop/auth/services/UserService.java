package com.pet.petshop.auth.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pet.petshop.auth.dto.AuthResponse;
import com.pet.petshop.auth.dto.RegisterRequest;
import com.pet.petshop.auth.entity.Role;
import com.pet.petshop.auth.entity.User;
import com.pet.petshop.auth.jwt.JwtService;
import com.pet.petshop.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    //Rol Admin
    public AuthResponse registerAdmin(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())             
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);

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

    //Rol vendedor
    public AuthResponse registerSeller(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())             
                .role(Role.SELLER)
                .build();

        userRepository.save(user);

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

    //rol cliente
    public AuthResponse registerCustomer(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nombre de usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())             
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

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
