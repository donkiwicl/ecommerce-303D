package dev.rampmaster.ecommerce.inventory.repository;

import dev.rampmaster.ecommerce.inventory.model.InventoryItem;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InventoryItemRepository {

    private final Map<Long, InventoryItem> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public InventoryItemRepository() {
        save(new InventoryItem(null, 1L, 25, "WH-BOG-01"));
        save(new InventoryItem(null, 2L, 80, "WH-MDE-01"));
        save(new InventoryItem(null, 3L, 120, "WH-CLO-01"));
    }

    public List<InventoryItem> findAll() {
        return storage.values().stream()
                .map(this::copy)
                .toList();
    }

    public Optional<InventoryItem> findById(Long id) {
        return Optional.ofNullable(storage.get(id))
                .map(this::copy);
    }

    public InventoryItem save(InventoryItem entity) {
        InventoryItem entityToStore = copy(entity);

        if (entityToStore.getId() == null) {
            entityToStore.setId(sequence.incrementAndGet());
        }
        storage.put(entityToStore.getId(), entityToStore);
        return copy(entityToStore);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    private InventoryItem copy(InventoryItem source) {
        return new InventoryItem(
                source.getId(),
                source.getProductId(),
                source.getQuantity(),
                source.getWarehouseCode()
        );
    }
}

