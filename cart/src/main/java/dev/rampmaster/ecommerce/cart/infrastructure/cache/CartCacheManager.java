package dev.rampmaster.ecommerce.cart.infrastructure.cache;

import dev.rampmaster.ecommerce.cart.domain.model.CartItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CartCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CART_CACHE_KEY_PREFIX = "CART_USER:";

    @Value("${cart.cache.ttl-hours:1}")
    private long cacheTimeToLiveHours;

    public CartCacheManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheCartItem(CartItem cartItem) {
        String cacheKey = buildCacheKey(cartItem.getUserId());
        String hashField = cartItem.getProductId().toString();

        redisTemplate.opsForHash().put(cacheKey, hashField, cartItem.getQuantity());
        redisTemplate.expire(cacheKey, Duration.ofHours(cacheTimeToLiveHours));
    }

    public void evictCartItem(Long userId, Long productId) {
        String cacheKey = buildCacheKey(userId);
        String hashField = productId.toString();

        redisTemplate.opsForHash().delete(cacheKey, hashField);
    }

    private String buildCacheKey(Long userId) {
        return CART_CACHE_KEY_PREFIX + userId;
    }
}
