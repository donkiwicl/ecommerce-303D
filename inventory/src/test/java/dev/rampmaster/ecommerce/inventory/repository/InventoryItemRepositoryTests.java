package dev.rampmaster.ecommerce.inventory.repository;

import dev.rampmaster.ecommerce.inventory.model.InventoryItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryItemRepositoryTests {

    @Test
    void saveShouldGenerateUniqueIdsUnderConcurrentLoad() throws Exception {
        InventoryItemRepository repository = new InventoryItemRepository();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<Callable<InventoryItem>> tasks = new ArrayList<>();

        for (long productId = 10; productId < 60; productId++) {
            long currentProductId = productId;
            tasks.add(() -> repository.save(new InventoryItem(null, currentProductId, 10, "WH-TEST-01")));
        }

        List<Future<InventoryItem>> results = executorService.invokeAll(tasks);
        executorService.shutdown();

        Set<Long> generatedIds = results.stream()
                .map(result -> {
                    try {
                        return result.get().getId();
                    } catch (Exception exception) {
                        throw new IllegalStateException(exception);
                    }
                })
                .collect(Collectors.toSet());

        assertEquals(tasks.size(), generatedIds.size());
    }
}