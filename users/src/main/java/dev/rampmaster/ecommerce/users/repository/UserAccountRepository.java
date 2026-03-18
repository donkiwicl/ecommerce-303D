package dev.rampmaster.ecommerce.users.repository;

import dev.rampmaster.ecommerce.users.model.UserAccount;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserAccountRepository {

    private final Map<Long, UserAccount> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public UserAccountRepository() {
        // Actualicé los datos iniciales para incluir una contraseña por defecto ("1234")
        save(new UserAccount(null, "admin", "admin@ecommerce.dev", "1234", "ADMIN", true));
        save(new UserAccount(null, "buyer01", "buyer01@ecommerce.dev", "1234", "CUSTOMER", true));
        save(new UserAccount(null, "support01", "support@ecommerce.dev", "1234", "SUPPORT", true));
    }


    public List<UserAccount> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<UserAccount> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public UserAccount save(UserAccount entity) {
        if (entity.getId() == null) {
            entity.setId(sequence.incrementAndGet());
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    /**
     * TODO: Implementar busqueda en BD
     **/
    public boolean existsByUsername(String username) {
        return true;
    }
}