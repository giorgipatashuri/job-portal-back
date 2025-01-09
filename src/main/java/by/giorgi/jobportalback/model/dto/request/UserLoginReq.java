package by.giorgi.jobportalback.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginReq {
    @NotBlank(message = "username is required.")
    private String username;
    @NotBlank(message = "Password is required.")
    private String password;
}
