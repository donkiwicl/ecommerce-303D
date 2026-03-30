package dev.rampmaster.ecommerce.cart.application.dto;

import dev.rampmaster.ecommerce.cart.domain.model.CartItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CartItemResponse(
        Long id,
        Long userId,
        Long productId,
        Integer quantity,
        BigDecimal unitPrice,
        LocalDateTime createdAt
) {

    public static CartItemResponse fromEntity(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getUserId(),
                cartItem.getProductId(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                cartItem.getCreatedAt()
        );
    }
}
