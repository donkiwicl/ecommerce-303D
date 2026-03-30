package dev.rampmaster.ecommerce.cart.application.dto;

import java.math.BigDecimal;

public record CreateCartItemRequest(
        Long userId,
        Long productId,
        Integer quantity,
        BigDecimal unitPrice
) {
}
