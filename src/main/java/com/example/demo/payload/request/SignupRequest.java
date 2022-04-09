package com.example.demo.payload.request;

import com.example.demo.annotations.PasswordMatches;
import com.example.demo.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "please, enter your username")
    private String username;
    @NotEmpty(message = "please, enter your firstname")
    private String firstname;
    @NotEmpty(message = "please, enter your lastname")
    private String lastname;
    @NotEmpty(message = "please, enter your password")
    @Size(min = 6)
    private String password;
    private String confirmPassword;
}
