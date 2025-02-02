package by.giorgi.jobportalback.controller;

import by.giorgi.jobportalback.model.dto.CvDto;
import by.giorgi.jobportalback.model.dto.UserDto;
import by.giorgi.jobportalback.model.dto.request.UserLoginReq;
import by.giorgi.jobportalback.model.dto.request.UserRegisterReq;
import by.giorgi.jobportalback.model.dto.response.AuthResp;
import by.giorgi.jobportalback.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterReq userRegisterReq) {
        authService.register(userRegisterReq);
        return ResponseEntity.ok("user registered");

    }
    @PostMapping("/login")
    public ResponseEntity<AuthResp> login(@Valid @RequestBody UserLoginReq userLoginReq) {
        AuthResp authResp = authService.login(userLoginReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResp);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = authService.getMe(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
    @GetMapping("/verify/{token}")
    public ResponseEntity<AuthResp> Verify(@PathVariable String token) {
        AuthResp authResp = authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResp);
    }
    @GetMapping("/test")
    public String test( @AuthenticationPrincipal UserDetails userDetails ) {
        return userDetails.getUsername();
    }

}
