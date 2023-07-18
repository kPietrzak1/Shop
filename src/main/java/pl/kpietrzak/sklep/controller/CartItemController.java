package pl.kpietrzak.sklep.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kpietrzak.sklep.model.CartItem;
import pl.kpietrzak.sklep.service.CartItemService;

import java.util.List;

@RestController
@RequestMapping("/api/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/create")
    public ResponseEntity<CartItem> createCartItem(@RequestBody CartItem cartItem){
        return ResponseEntity.ok(cartItemService.saveCartItem(cartItem));
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAllCartItems(){
        return ResponseEntity.ok(cartItemService.getAllCartItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id){
        CartItem cartItem = cartItemService.getCartItemById(id)
                .orElseThrow(() -> new RuntimeException("Error: CartItem is not found."));
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id){
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok("CartItem deleted successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItemQuantity(@PathVariable Long id, @RequestParam int quantity){
        return ResponseEntity.ok(cartItemService.updateCartItemQuantity(id, quantity));
    }
}

