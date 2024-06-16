package com.pet.petshop.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.petshop.auth.dto.AuthResponse;
import com.pet.petshop.auth.dto.RegisterRequest;
import com.pet.petshop.auth.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping(value = "admin")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerAdmin(request));
    }      

    @PostMapping(value = "seller")
    public ResponseEntity<AuthResponse> registerSeller(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerSeller(request));
    }

    @PostMapping(value = "customer")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerCustomer(request));
    }
  

}
