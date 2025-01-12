package by.giorgi.jobportalback.service;


import by.giorgi.jobportalback.model.dto.request.UserLoginReq;
import by.giorgi.jobportalback.model.dto.response.AuthResp;
import by.giorgi.jobportalback.model.dto.request.UserRegisterReq;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.model.enums.Role;
import by.giorgi.jobportalback.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public AuthResp register(UserRegisterReq registerRequest) {

        Optional<User> user = userRepository.findByEmail(registerRequest.getEmail());

        if(user.isPresent()) {
            throw new BadCredentialsException("Email already in use");
        }

        User newUser = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .username(registerRequest.getUsername())
                .lastname(registerRequest.getLastname())
                .role(Role.USER)
                .build();
        userRepository.save(newUser);

        String token = jwtService.generateToken(newUser);

        return new AuthResp(token);
    }
    public AuthResp login(UserLoginReq userLoginReq) {
        try {
            System.out.println(userLoginReq.getEmail());
            System.out.println(userLoginReq.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginReq.getEmail(),
                            userLoginReq.getPassword()
                    )
            );

            System.out.println("test");
            User authenticatedUser = (User) authentication.getPrincipal();

            System.out.println(authenticatedUser);

            String token = jwtService.generateToken(authenticatedUser);

            return new AuthResp(token);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password", ex);
        }
    }
}
