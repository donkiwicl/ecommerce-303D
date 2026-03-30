package dev.rampmaster.ecommerce.cart.application.service;

import dev.rampmaster.ecommerce.cart.application.dto.CartItemResponse;
import dev.rampmaster.ecommerce.cart.application.dto.CreateCartItemRequest;
import dev.rampmaster.ecommerce.cart.application.dto.UpdateCartItemRequest;
import dev.rampmaster.ecommerce.cart.domain.exception.CartItemNotFoundException;
import dev.rampmaster.ecommerce.cart.domain.model.CartItem;
import dev.rampmaster.ecommerce.cart.domain.repository.CartItemRepository;
import dev.rampmaster.ecommerce.cart.infrastructure.cache.CartCacheManager;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartCacheManager cartCacheManager;

    public CartItemService(CartItemRepository cartItemRepository, CartCacheManager cartCacheManager) {
        this.cartItemRepository = cartItemRepository;
        this.cartCacheManager = cartCacheManager;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findAllCartItems() {
        return cartItemRepository.findAll()
                .stream()
                .map(CartItemResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CartItemResponse findCartItemById(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        return CartItemResponse.fromEntity(cartItem);
    }

    @CircuitBreaker(name = "inventoryCB", fallbackMethod = "fallbackAddItemToCart")
    public CartItemResponse addItemToCart(CreateCartItemRequest request) {
        // Simulamos un fallo intencional aleatorio para que se abra el circuito y puedas ver actuar a Resilience4j
        if (Math.random() > 0.5) {
            throw new RuntimeException("Simulando fallo en la conexion con microservicio de Inventory");
        }

        CartItem cartItem = new CartItem(
                request.userId(),
                request.productId(),
                request.quantity(),
                request.unitPrice()
        );

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        cartCacheManager.cacheCartItem(savedCartItem);

        return CartItemResponse.fromEntity(savedCartItem);
    }

    // Metodo fallback
    public CartItemResponse fallbackAddItemToCart(CreateCartItemRequest request, Throwable t) {
        System.out.println("⚠️ ERROR: Fallo simulado interceptado por el Circuit Breaker. Motivo: " + t.getMessage());
        System.out.println("Aplicando Fallback: guardando el item localmente asumiendo que hay stock.");
        
        CartItem cartItem = new CartItem(
                request.userId(),
                request.productId(),
                request.quantity(),
                request.unitPrice()
        );

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return CartItemResponse.fromEntity(savedCartItem);
    }

    public CartItemResponse updateCartItemQuantity(Long cartItemId, UpdateCartItemRequest request) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        existingCartItem.setQuantity(request.quantity());

        CartItem updatedCartItem = cartItemRepository.save(existingCartItem);
        cartCacheManager.cacheCartItem(updatedCartItem);

        return CartItemResponse.fromEntity(updatedCartItem);
    }

    public void removeItemFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        cartItemRepository.deleteById(cartItemId);
        cartCacheManager.evictCartItem(cartItem.getUserId(), cartItem.getProductId());
    }
}
