package by.giorgi.jobportalback.controller;

import by.giorgi.jobportalback.model.dto.CvDto;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.service.CvService;
import by.giorgi.jobportalback.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cv")
@AllArgsConstructor
public class CvController {
    private final CvService cvService;
    private final UserService userService;

    @PostMapping
//    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CvDto> createCv(@Valid @RequestBody CvDto cvDto, @AuthenticationPrincipal UserDetails userDetails) {

//        User user = userService.getUserByUsername(userDetails.getUsername());
        User user = userService.getUserById(1L);
        System.out.println(user.getRole());
        CvDto createdCv = cvService.createCv(cvDto,user.getId());
        return new ResponseEntity<>(createdCv, HttpStatus.CREATED);
    }
}
