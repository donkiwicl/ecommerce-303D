package dev.rampmaster.ecommerce.users.model;

public class UserAccount {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role; // Cambiado de String a Role
    private boolean active;

    public UserAccount() {
    }

    public UserAccount(Long id, String username, String email, String password, Role role, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() { // Ahora devuelve un Role
        return role;
    }

    public void setRole(Role role) { // Ahora recibe un Role
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}