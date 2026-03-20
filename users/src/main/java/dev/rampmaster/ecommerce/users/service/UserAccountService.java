package dev.rampmaster.ecommerce.users.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import dev.rampmaster.ecommerce.users.model.UserAccount;
import dev.rampmaster.ecommerce.users.model.Role; 
import dev.rampmaster.ecommerce.users.repository.UserAccountRepository;

@Service
public class UserAccountService {

    private final UserAccountRepository repository;

    public UserAccountService(UserAccountRepository repository) {
        this.repository = repository;
    }

    // --- Lógica de Permisos (RBAC) ---
    
    public boolean canDeleteUser(Role requesterRole) {
        // Solo el ADMIN tiene permisos de eliminación
        return Role.ADMIN.equals(requesterRole);
    }

    public boolean canViewAll(Role requesterRole) {
        // ADMIN y SUPPORT pueden visualizar la lista completa de cuentas
        return Role.ADMIN.equals(requesterRole) || Role.SUPPORT.equals(requesterRole);
    }

    // --- Métodos CRUD ---

    public List<UserAccount> findAll() {
        return repository.findAll();
    }

    public Optional<UserAccount> findById(Long id) {
        return repository.findById(id);
    }

    public UserAccount create(UserAccount entity) {
        // Si no se especifica rol, se asigna COSTUMER por defecto
        if (entity.getRole() == null) {
            entity.setRole(Role.COSTUMER);
        }
        return repository.save(entity);
    }

    public Optional<UserAccount> update(Long id, UserAccount entity) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        entity.setId(id);
        return Optional.of(repository.save(entity));
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public Optional<UserAccount> findByEmail(String email) {
        return repository.findByEmail(email);
    }
    
}