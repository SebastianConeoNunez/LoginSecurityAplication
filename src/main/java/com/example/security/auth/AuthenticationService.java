package com.example.security.auth;

import com.example.security.config.JwtService;
import com.example.security.users.Role;
import com.example.security.users.User;
import com.example.security.users.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
   @Autowired
    private JwtService jwtService;
   @Autowired
    private AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .firstname(request.getFirstname())
                .Lastname(request.getLastname())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse Authenticate(AuthenticationRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwToken)
                .build();


    }
}
