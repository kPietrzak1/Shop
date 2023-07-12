package pl.kpietrzak.sklep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kpietrzak.sklep.DTO.CartItemDTO;
import pl.kpietrzak.sklep.model.Cart;
import pl.kpietrzak.sklep.model.CartItem;
import pl.kpietrzak.sklep.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCartByUserId(cartId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Cart> createCart(@PathVariable Long userId) {
        return new ResponseEntity<>(cartService.createCart(userId), HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @RequestBody CartItemDTO newCartItem) {
        return new ResponseEntity<>(cartService.addProductToCart(cartId, newCartItem.getProductId(), newCartItem.getQuantity()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long cartItemId) {
        cartService.removeProductFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
