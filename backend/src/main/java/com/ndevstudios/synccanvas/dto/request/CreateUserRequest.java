package com.ndevstudios.synccanvas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateUserRequest {
    @NotBlank
    @Email
    @Size(max = 225)
    private String email;

    @NotBlank
    @Size(min = 8,max = 50)
    private String password;

    @NotBlank
    @Size(min = 3,max = 100)
    private String displayName;
}
