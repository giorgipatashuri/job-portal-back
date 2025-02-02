package by.giorgi.jobportalback.service;


import by.giorgi.jobportalback.config.WebSocketHandler;
import by.giorgi.jobportalback.mapper.BaseMapper;
import by.giorgi.jobportalback.mapper.BaseMapperImpl;
import by.giorgi.jobportalback.model.dto.UserDto;
import by.giorgi.jobportalback.model.dto.request.UserLoginReq;
import by.giorgi.jobportalback.model.dto.response.AuthResp;
import by.giorgi.jobportalback.model.dto.request.UserRegisterReq;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.model.enums.Role;
import by.giorgi.jobportalback.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Service
@AllArgsConstructor
public class AuthService {
    private static final long TOKEN_EXPIRATION_HOURS = 24;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String,String> redisTemplate;
    private final MailService mailService;
    private final WebSocketHandler webSocketHandler;


    public void register(UserRegisterReq registerRequest) {

        Optional<User> user = userRepository.findByEmail(registerRequest.getEmail());

        if(user.isPresent()) {
            throw new BadCredentialsException("Email already in use");
        }

        User newUser = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .lastname(registerRequest.getLastname())
                .role(Role.USER)
                .build();

        userRepository.save(newUser);
        sendVerificationEmail(newUser.getEmail());

    }
    public AuthResp login(UserLoginReq userLoginReq) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginReq.getEmail(),
                            userLoginReq.getPassword()
                    )
            );
            User authenticatedUser = (User) authentication.getPrincipal();

            System.out.println(authenticatedUser);

            String token = jwtService.generateToken(authenticatedUser);

            return new AuthResp(token);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password", ex);
        }
    }

    public AuthResp verifyEmail(String token) {
        String rediskey = "verification" + token;
        String email = redisTemplate.opsForValue().get(rediskey);
        System.out.println(email);
        if (email == null) {
            throw new IllegalArgumentException("invalid or expired token");
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        user.setVerified(true);

        userRepository.save(user);

        redisTemplate.delete(rediskey);

        String jwtToken = jwtService.generateToken(user);

        webSocketHandler.notifyEmailVerification(email, jwtToken);

        return new AuthResp(jwtToken);
    }


    public UserDto getMe(String username){
        User user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException(username));
        System.out.println(user.getEmail());
        return UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .lastname(user.getLastname())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
    private void sendVerificationEmail(String email) {
        String verificationToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("verification" + verificationToken,email,TOKEN_EXPIRATION_HOURS, TimeUnit.HOURS);
        try{
            mailService.sendVerificationMail(email, verificationToken);
        }catch (Exception e){
            throw new RuntimeException("Failed to send verification email");
        }
    }
}
