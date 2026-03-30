package dev.rampmaster.ecommerce.cart.service;

import dev.rampmaster.ecommerce.cart.model.CartItem;
import dev.rampmaster.ecommerce.cart.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    // private final InventoryClient inventoryClient;

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

    @CircuitBreaker(name = "inventoryCB", fallbackMethod = "fallbackCreate")
    public CartItem create(CartItem entity) {
        // boolean hasStock = inventoryClient.checkStock(entity.getProductId(), entity.getQuantity());

        CartItem saved = repository.save(entity);

        updateRedis(saved);

        return saved;
    }

    public CartItem fallbackCreate(CartItem entity, Throwable t) {
        System.out.println("⚠️ ERROR: El microservicio de Inventario no responde. Aplicando Fallback.");
        return repository.save(entity);
    }

    public Optional<CartItem> update(Long id, CartItem entity) {
        return repository.findById(id).map(existingItem -> {

            existingItem.setQuantity(entity.getQuantity());
            existingItem.setProductId(entity.getProductId());

            CartItem updated = repository.save(existingItem);

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

    private void updateRedis(CartItem item) {
        String key = REDIS_KEY + item.getUserId();
        redisTemplate.opsForHash().put(key, item.getProductId().toString(), item.getQuantity());
        redisTemplate.expire(key, Duration.ofHours(1));
    }
}
