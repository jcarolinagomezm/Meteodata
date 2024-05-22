package com.service.hydrometrics.models.DTO.user;

import com.service.hydrometrics.models.DB.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserDTO implements Serializable {
    @NotEmpty
    private final String username;
    @NotEmpty
    private final String firstName;
    @NotEmpty
    private final String lastName;
    @Email
    private final String email;
    @NotEmpty
    private final String role;
    private final boolean enabled;

    public UserDTO(User user) {
        this.role = user.getRole().toString();
        this.email = user.getEmail();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
    }
}