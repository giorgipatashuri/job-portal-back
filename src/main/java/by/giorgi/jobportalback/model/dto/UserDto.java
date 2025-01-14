package by.giorgi.jobportalback.model.dto;

import by.giorgi.jobportalback.model.enums.Role;
import jakarta.persistence.*;

public class UserDto {

    private Long id;


    private String name;


    private String lastname;


    private String username;


    private String email;


    private Role role = Role.USER;

}
