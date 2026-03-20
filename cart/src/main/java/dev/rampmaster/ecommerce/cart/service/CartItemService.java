package dev.rampmaster.ecommerce.cart.service;

import dev.rampmaster.ecommerce.cart.model.CartItem;
import dev.rampmaster.ecommerce.cart.repository.CartItemRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository repository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY = "CART_USER:";

    public CartItemService(CartItemRepository repository, RedisTemplate<String, Object> redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public List<CartItem> findAll() {
        return repository.findAll();
    }

    public Optional<CartItem> findById(Long id) {
        return repository.findById(id);
    }

    public CartItem create(CartItem entity) {
        // Guardamos en MySQL primero para generar el ID real
        CartItem saved = repository.save(entity);

        // Sincronizamos con Redis (Cache de velocidad)
        updateRedis(saved);

        return saved;
    }

    // --- ESTE ES EL MÉTODO PARA EL PUTMAPPING ---
    public Optional<CartItem> update(Long id, CartItem entity) {
        return repository.findById(id).map(existingItem -> {
            // Actualizamos los datos del objeto existente
            existingItem.setQuantity(entity.getQuantity());
            existingItem.setProductId(entity.getProductId());

            // 1. Persistencia en MySQL
            CartItem updated = repository.save(existingItem);

            // 2. Actualización en Redis
            updateRedis(updated);

            return updated;
        });
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(item -> {
            // Borramos de MySQL
            repository.deleteById(id);
            // Borramos de Redis
            String key = REDIS_KEY + item.getUserId();
            redisTemplate.opsForHash().delete(key, item.getProductId().toString());
            return true;
        }).orElse(false);
    }

    // Método privado para no repetir código de Redis
    private void updateRedis(CartItem item) {
        String key = REDIS_KEY + item.getUserId();
        redisTemplate.opsForHash().put(key, item.getProductId().toString(), item.getQuantity());
        redisTemplate.expire(key, Duration.ofHours(1));
    }
}

