package dev.rampmaster.ecommerce.cart.service;

import dev.rampmaster.ecommerce.cart.repository.CartItemRepository;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class CartExpiration extends KeyExpirationEventMessageListener {

    private final CartItemRepository repository;

    public CartExpiration(RedisMessageListenerContainer listenerContainer, CartItemRepository repository) {
        super(listenerContainer);
        this.repository = repository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith("CART_USER:")) {
            String userId = expiredKey.split(":")[1];

            System.out.println("⏰ EXPIRACIÓN: Carrito del usuario " + userId + " ha caducado.");
            System.out.println("💾 Guardando recordatorio en MySQL para el comprador...");

            // Aquí la lógica de persistencia final si fuera necesaria
        }
    }
}