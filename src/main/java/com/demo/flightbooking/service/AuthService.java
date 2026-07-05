package com.flightbooking.services;

import com.flightbooking.dto.AuthResponse;
import com.flightbooking.dto.LoginRequest;
import com.flightbooking.dto.RegisterRequest;
import com.flightbooking.enums.Role;
import com.flightbooking.exceptions.DuplicateEmailException;
import com.flightbooking.exceptions.UserNotFoundException;
import com.flightbooking.models.User;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // Generates constructor for all 'final' fields
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    // =====================================================================
    // @RequiredArgsConstructor replaces this constructor (commented out):
    // =====================================================================
    //
    // public AuthService(UserRepository userRepository, JwtService jwtService,
    //                    AuthenticationManager authenticationManager,
    //                    PasswordEncoder passwordEncoder) {
    //     this.userRepository = userRepository;
    //     this.jwtService = jwtService;
    //     this.authenticationManager = authenticationManager;
    //     this.passwordEncoder = passwordEncoder;
    // }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Login successful")
                .build();
    }
}
