package by.giorgi.jobportalback.service;


import by.giorgi.jobportalback.config.WebSocketHandler;
import by.giorgi.jobportalback.model.dto.UserDto;
import by.giorgi.jobportalback.model.dto.request.CompanyRegisterReq;
import by.giorgi.jobportalback.model.dto.request.UserLoginReq;
import by.giorgi.jobportalback.model.dto.response.AuthResp;
import by.giorgi.jobportalback.model.dto.request.UserRegisterReq;
import by.giorgi.jobportalback.model.entity.Company;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.model.enums.Role;
import by.giorgi.jobportalback.repository.CompanyRepository;
import by.giorgi.jobportalback.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final CompanyRepository companyRepository;

    @Transactional
    public void registerCompany(CompanyRegisterReq companyRegisterReq) {
        Optional<Company> company = companyRepository.findByCompanyEmail(companyRegisterReq.getCompanyEmail());

        if(company.isPresent()) {
            throw new BadCredentialsException("Email already in use");
        }

        Company newCompany = Company.builder()
                .companyName(companyRegisterReq.getCompanyName())
                .companyEmail(companyRegisterReq.getCompanyEmail())
                .password(passwordEncoder.encode(companyRegisterReq.getPassword()))
//                .description(registerRequest.getDescription())
                .verified(false)
                .build();

        companyRepository.save(newCompany);
        sendVerificationEmail(newCompany.getCompanyEmail(), "COMPANY");
    }
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
        sendVerificationEmail(newUser.getEmail(),"USER");

    }
    public AuthResp loginCompany(UserLoginReq userLoginReq) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginReq.getEmail(),
                            userLoginReq.getPassword()
                    )
            );

            String token = jwtService.generateToken(authentication,"COMPANY");

            return new AuthResp(token);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password", ex);
        }
    }
    public AuthResp login(UserLoginReq userLoginReq) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginReq.getEmail(),
                            userLoginReq.getPassword()
                    )
            );

            String token = jwtService.generateToken(authentication,"USER");

            return new AuthResp(token);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password", ex);
        }
    }

    public void verifyEmail(String token,String userType) {
        String redisKey = "verification" + token;
        String email = redisTemplate.opsForValue().get(redisKey);
        if (email == null) {
            throw new IllegalArgumentException("invalid or expired token");
        }
        if(userType.equals("USER")) {
            verifyUserEmail(email, redisKey);
        }else {
            verifyCompanyEmail(email, redisKey);
        }
    }

    public Company getCompany(String userName){
        return companyRepository.findByCompanyEmail(userName).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
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
    private void verifyCompanyEmail(String email, String redisKey) {
        Company company = companyRepository.findByCompanyEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        company.setVerified(true);
        companyRepository.save(company);
        redisTemplate.delete(redisKey);

        Authentication authentication = new UsernamePasswordAuthenticationToken(company, null, company.getAuthorities());
        String jwtToken = jwtService.generateToken(authentication, "COMPANY");
        webSocketHandler.notifyEmailVerification(email, jwtToken);
    }

    private void verifyUserEmail(String email, String redisKey) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        user.setVerified(true);
        userRepository.save(user);
        redisTemplate.delete(redisKey);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String jwtToken = jwtService.generateToken(authentication, "USER");
        webSocketHandler.notifyEmailVerification(email, jwtToken);
    }
    private void sendVerificationEmail(String email,String userType) {
        String verificationToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("verification" + verificationToken,email,TOKEN_EXPIRATION_HOURS, TimeUnit.HOURS);
        try{
            mailService.sendVerificationMail(email, verificationToken,userType);
        }catch (Exception e){
            throw new RuntimeException("Failed to send verification email");
        }
    }
}
