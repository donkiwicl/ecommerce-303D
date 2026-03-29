package dev.rampmaster.ecommerce.cart.infrastructure.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class CartExpirationEventListener extends KeyExpirationEventMessageListener {

    private static final String CART_CACHE_KEY_PREFIX = "CART_USER:";

    public CartExpirationEventListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith(CART_CACHE_KEY_PREFIX)) {
            String userId = expiredKey.substring(CART_CACHE_KEY_PREFIX.length());

            System.out.println("⏰ EXPIRACIÓN: Carrito del usuario " + userId + " ha caducado.");
            System.out.println("💾 Guardando recordatorio en MySQL para el comprador...");

            // TODO: Implementar lógica de persistencia final o notificación al usuario
        }
    }
}
