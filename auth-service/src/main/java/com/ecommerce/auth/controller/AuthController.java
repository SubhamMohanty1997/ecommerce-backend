package com.ecommerce.auth.controller;

import com.ecommerce.auth.dto.ApiResponse;
import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.entity.User;
import com.ecommerce.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);
        ApiResponse response = new ApiResponse("success", "User registered", user.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(Authentication authentication) {
        if (authentication == null ||
                authentication instanceof AnonymousAuthenticationToken ||
                !authentication.isAuthenticated()) {

            return ResponseEntity.ok("Hello Anonymous");
        }
        return ResponseEntity.ok("Hello " + authentication.getName());
    }
}
