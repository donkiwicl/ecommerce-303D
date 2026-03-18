package dev.rampmaster.ecommerce.users.controller;

import dev.rampmaster.ecommerce.users.model.UserAccount;
import dev.rampmaster.ecommerce.users.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService service;

    public UserAccountController(UserAccountService service) {
        this.service = service;
    }

    // Listar todos los usuarios
    @GetMapping
    public List<UserAccount> findAll() {
        return service.findAll();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // REGISTRO DE USUARIOS
    // Este método usará la lógica de tu Service para asignar rol "CUSTOMER"
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount entity) {
        try {
            UserAccount created = service.create(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            // Retorna el error si el usuario ya existe o falta la contraseña
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =========================================================
    // ESPACIO RESERVADO PARA LOGIN Y GENERACIÓN DE JWT
    // Aquí los compañeros deben implementar el @PostMapping("/login")
    // que valide credenciales y devuelva el Token.
    // =========================================================


    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> update(@PathVariable Long id, @RequestBody UserAccount entity) {
        return service.update(id, entity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}