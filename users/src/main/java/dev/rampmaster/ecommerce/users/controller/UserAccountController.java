package dev.rampmaster.ecommerce.users.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.rampmaster.ecommerce.users.model.UserAccount;
import dev.rampmaster.ecommerce.users.model.LoginRequest;
import dev.rampmaster.ecommerce.users.model.Role;
import dev.rampmaster.ecommerce.users.service.UserAccountService;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService service;

    public UserAccountController(UserAccountService service) {
        this.service = service;
    }

    // Obtener todos los usuarios (Requiere ADMIN o SUPPORT)
    @GetMapping("/all")
    public ResponseEntity<?> findAll(@RequestHeader(value = "X-User-Role", defaultValue = "COSTUMER") Role role) {
        if (!service.canViewAll(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: Se requiere rol ADMIN o SUPPORT.");
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserAccount> create(@RequestBody UserAccount entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> update(@PathVariable Long id, @RequestBody UserAccount entity) {
        return service.update(id, entity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Borrar usuario (Solo ADMINISTRADOR)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, 
                                    @RequestHeader(value = "X-User-Role", defaultValue = "COSTUMER") Role role) {
        if (!service.canDeleteUser(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: Solo el ADMINISTRADOR tiene permisos para eliminar registros.");
        }

        if (!service.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) { // Cambiado aquí
    return service.findByEmail(loginRequest.getEmail())
            .filter(user -> user.getPassword() != null && 
                    user.getPassword().equals(loginRequest.getPassword()))
            .map(user -> ResponseEntity.ok("Login exitoso. Bienvenido " + user.getUsername() + 
                                           ". Tu rol es: " + user.getRole()))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas"));
    }
}