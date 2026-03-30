package dev.rampmaster.ecommerce.cart.infrastructure.seed;

import dev.rampmaster.ecommerce.cart.domain.model.CartItem;
import dev.rampmaster.ecommerce.cart.domain.repository.CartItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartDataSeeder implements CommandLineRunner {

    private final CartItemRepository cartItemRepository;

    public CartDataSeeder(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void run(String... args) {
        if (cartItemRepository.count() == 0) {
            System.out.println("🌱 Seeding cart items...");

            cartItemRepository.save(new CartItem(2L, 1L, 1, new BigDecimal("19.99")));
            cartItemRepository.save(new CartItem(2L, 3L, 2, new BigDecimal("23.99")));
            cartItemRepository.save(new CartItem(1L, 2L, 1, new BigDecimal("5.99")));

            System.out.println("✅ Cart seed data loaded successfully.");
        }
    }
}
