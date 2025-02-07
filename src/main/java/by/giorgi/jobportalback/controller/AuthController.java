package by.giorgi.jobportalback.controller;

import by.giorgi.jobportalback.model.dto.UserDto;
import by.giorgi.jobportalback.model.dto.request.CompanyRegisterReq;
import by.giorgi.jobportalback.model.dto.request.UserLoginReq;
import by.giorgi.jobportalback.model.dto.request.UserRegisterReq;
import by.giorgi.jobportalback.model.dto.response.AuthResp;
import by.giorgi.jobportalback.model.entity.Company;
import by.giorgi.jobportalback.repository.CompanyRepository;
import by.giorgi.jobportalback.service.AuthService;
import by.giorgi.jobportalback.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterReq userRegisterReq) {
        authService.register(userRegisterReq);
        return ResponseEntity.ok("user registered");
    }
    @PostMapping("/company/login")
    public ResponseEntity<AuthResp> companyLogin(@Valid @RequestBody UserLoginReq userLoginReq) {
        AuthResp authResp = authService.loginCompany(userLoginReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResp);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResp> login(@Valid @RequestBody UserLoginReq userLoginReq) {
        AuthResp authResp = authService.login(userLoginReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResp);
    }
    @GetMapping("/user/me")
    public ResponseEntity<UserDto> me2(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = authService.getMe(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
    @GetMapping("/company/me")
    public ResponseEntity<Company> me(@AuthenticationPrincipal UserDetails userDetails) {
        Company user = authService.getCompany(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
    @PostMapping("/company/register")
    public ResponseEntity<String> registerCompany(@Valid @RequestBody CompanyRegisterReq request) {
        authService.registerCompany(request);
        return ResponseEntity.ok("user registered");
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> Verify(@PathVariable String token,@RequestParam("userType") String userType, HttpServletResponse response) throws IOException {
        authService.verifyEmail(token,userType);
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("""
                <html>
                    <head><title>მეილი წარმატებით ვერიფიცირებულია</title></head>
                    <body>
                        <h2>მეილი წარმატებით ვერიფიცირებულია!</h2>
                        <p>შეგიძლია დახურო ეს გვერდი.</p>
                        <script>
                            setTimeout(() => window.close(),1);
                        </script>
                    </body>
                </html>
            """);
        writer.flush();

        return ResponseEntity.ok().build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(authResp);
    }
    @GetMapping("/test")
    public String test( @AuthenticationPrincipal UserDetails userDetails ) {
        return userDetails.getUsername();
    }

}
