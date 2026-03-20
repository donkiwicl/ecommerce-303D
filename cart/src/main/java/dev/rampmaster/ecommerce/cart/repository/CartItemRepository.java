package dev.rampmaster.ecommerce.cart.repository;

import dev.rampmaster.ecommerce.cart.model.CartItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CartItemRepository {

    private final Map<Long, CartItem> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public CartItemRepository() {
        save(new CartItem(null, 2L, 1L, 1, 19.99));
        save(new CartItem(null, 2L, 3L, 2, 23.99));
        save(new CartItem(null, 1L, 2L, 1, 5.99));
    }

    public List<CartItem> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<CartItem> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public CartItem save(CartItem entity) {
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
}

