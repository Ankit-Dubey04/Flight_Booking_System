package com.demo.flightbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // Generates: getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Generates: no-args constructor
@AllArgsConstructor     // Generates: all-args constructor
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    // =====================================================================
    // LOMBOK replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public RegisterRequest() {}
    //
    // public RegisterRequest(String name, String email, String password) {
    //     this.name = name;
    //     this.email = email;
    //     this.password = password;
    // }
    //
    // public String getName() { return name; }
    // public void setName(String name) { this.name = name; }
    // public String getEmail() { return email; }
    // public void setEmail(String email) { this.email = email; }
    // public String getPassword() { return password; }
    // public void setPassword(String password) { this.password = password; }
}
