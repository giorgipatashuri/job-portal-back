package by.giorgi.jobportalback.service;


import by.giorgi.jobportalback.model.dto.request.UserLoginReq;
import by.giorgi.jobportalback.model.dto.response.AuthResp;
import by.giorgi.jobportalback.model.dto.request.UserRegisterReq;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthResp register(UserRegisterReq registerRequest) {
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .username(registerRequest.getUsername())
                .lastname(registerRequest.getLastname())
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResp(token);
    }
    public AuthResp login(UserLoginReq userLoginReq) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginReq.getUsername(), userLoginReq.getPassword())
            );
            User user = userRepository.findByUsername(userLoginReq.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtService.generateToken(user);
            System.out.println("test in login");
            return new AuthResp(token);
    }
}
