package com.demo.flightbooking.models;

import com.demo.flightbooking.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // =====================================================================
    // Spring Security's UserDetails contract - the security layer authenticates
    // against "username", so here that identifier is the user's email.
    // =====================================================================

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // =====================================================================
    // LOMBOK @Data + @Builder + @NoArgsConstructor + @AllArgsConstructor
    // replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public User() {}
    //
    // public User(Long id, String name, String email, String password, Role role, LocalDateTime createdAt) {
    //     this.id = id;
    //     this.name = name;
    //     this.email = email;
    //     this.password = password;
    //     this.role = role;
    //     this.createdAt = createdAt;
    // }
    //
    // public Long getId() { return id; }
    // public void setId(Long id) { this.id = id; }
    // public String getName() { return name; }
    // public void setName(String name) { this.name = name; }
    // public String getEmail() { return email; }
    // public void setEmail(String email) { this.email = email; }
    // public String getPassword() { return password; }
    // public void setPassword(String password) { this.password = password; }
    // public Role getRole() { return role; }
    // public void setRole(Role role) { this.role = role; }
    // public LocalDateTime getCreatedAt() { return createdAt; }
    // public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    //
    // public static UserBuilder builder() { return new UserBuilder(); }
    // public static class UserBuilder {
    //     private Long id;
    //     private String name;
    //     private String email;
    //     private String password;
    //     private Role role;
    //     private LocalDateTime createdAt;
    //     public UserBuilder id(Long id) { this.id = id; return this; }
    //     public UserBuilder name(String name) { this.name = name; return this; }
    //     public UserBuilder email(String email) { this.email = email; return this; }
    //     public UserBuilder password(String password) { this.password = password; return this; }
    //     public UserBuilder role(Role role) { this.role = role; return this; }
    //     public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    //     public User build() { return new User(id, name, email, password, role, createdAt); }
    // }
}
