package pl.kpietrzak.sklep.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kpietrzak.sklep.model.Cart;
import pl.kpietrzak.sklep.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.saveCart(cart));
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Cart cart = cartService.getCartById(id)
                .orElseThrow(() -> new RuntimeException("Error: Cart is not found."));
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok("Cart deleted successfully!");
    }

    @PostMapping("/{cartId}/addItem/{itemId}")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, itemId));
    }

    @PostMapping("/{cartId}/removeItem/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartId, itemId));
    }

    @PostMapping("/{cartId}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.clearCart(cartId));
    }
}
