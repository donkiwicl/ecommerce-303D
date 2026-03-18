package dev.rampmaster.ecommerce.users.model;

public class UserAccount {

    private Long id;
    private String username;
    private String email;
    private String password; // <-- Nuevo campo para el Login
    private String role;
    private boolean active;

    public UserAccount() {
    }

    public UserAccount(Long id, String username, String email, String password, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password; // <-- Agregado al constructor
        this.role = role;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Nuevo Getter
    public String getPassword() {
        return password;
    }

    // Nuevo Setter
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}