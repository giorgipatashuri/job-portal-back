package by.giorgi.jobportalback.controller;

import by.giorgi.jobportalback.model.dto.request.UserLoginReq;
import by.giorgi.jobportalback.model.dto.request.UserRegisterReq;
import by.giorgi.jobportalback.model.dto.response.AuthResp;
import by.giorgi.jobportalback.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public String test() { return "test" ;}


    @PostMapping("/register")
    public ResponseEntity<AuthResp> register(@Valid @RequestBody UserRegisterReq userRegisterReq) {
        return ResponseEntity.ok(authService.register(userRegisterReq));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResp> login(@Valid @RequestBody UserLoginReq userLoginReq) {
        return ResponseEntity.ok(authService.login(userLoginReq));
    }
}
