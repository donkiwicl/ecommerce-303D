package dev.rampmaster.ecommerce.cart.application.service;

import dev.rampmaster.ecommerce.cart.application.dto.CartItemResponse;
import dev.rampmaster.ecommerce.cart.application.dto.CreateCartItemRequest;
import dev.rampmaster.ecommerce.cart.application.dto.UpdateCartItemRequest;
import dev.rampmaster.ecommerce.cart.domain.exception.CartItemNotFoundException;
import dev.rampmaster.ecommerce.cart.domain.model.CartItem;
import dev.rampmaster.ecommerce.cart.domain.repository.CartItemRepository;
import dev.rampmaster.ecommerce.cart.infrastructure.cache.CartCacheManager;
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

    public CartItemResponse addItemToCart(CreateCartItemRequest request) {
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
