package dev.rampmaster.ecommerce.cart.interfaces.rest;

import dev.rampmaster.ecommerce.cart.application.dto.CartItemResponse;
import dev.rampmaster.ecommerce.cart.application.dto.CreateCartItemRequest;
import dev.rampmaster.ecommerce.cart.application.dto.UpdateCartItemRequest;
import dev.rampmaster.ecommerce.cart.application.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public List<CartItemResponse> findAllCartItems() {
        return cartItemService.findAllCartItems();
    }

    @GetMapping("/{cartItemId}")
    public CartItemResponse findCartItemById(@PathVariable Long cartItemId) {
        return cartItemService.findCartItemById(cartItemId);
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addItemToCart(@RequestBody CreateCartItemRequest request) {
        CartItemResponse createdCartItem = cartItemService.addItemToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCartItem);
    }

    @PutMapping("/{cartItemId}")
    public CartItemResponse updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody UpdateCartItemRequest request) {
        return cartItemService.updateCartItemQuantity(cartItemId, request);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartItemId) {
        cartItemService.removeItemFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
