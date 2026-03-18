package dev.rampmaster.ecommerce.users.service;

import dev.rampmaster.ecommerce.users.model.UserAccount;
import dev.rampmaster.ecommerce.users.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository repository;

    public UserAccountService(UserAccountRepository repository) {
        this.repository = repository;
    }

    public List<UserAccount> findAll() {
        return repository.findAll();
    }

    public Optional<UserAccount> findById(Long id) {
        return repository.findById(id);
    }

    // --- REGISTRO (ESTO SÍ FUNCIONA) ---
    public UserAccount create(UserAccount entity) {
        if (entity.getPassword() == null || entity.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Error: La contraseña es obligatoria.");
        }

        if (repository.existsByUsername(entity.getUsername())) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso.");
        }

        entity.setId(null); 
        entity.setRole("CUSTOMER"); 
        entity.setActive(true);     

        return repository.save(entity);
    }

    /* =========================================================
       COMENTADO PARA EVITAR ERROR DE COMPILACIÓN
       Tus compañeros deben habilitar esto cuando implementen 
       el método findByUsername en el Repository.
       =========================================================
       
    public Optional<UserAccount> login(String username, String password) {
        return repository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password)); 
    }
    */

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
}