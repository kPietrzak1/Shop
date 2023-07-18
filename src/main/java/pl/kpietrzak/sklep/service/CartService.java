package pl.kpietrzak.sklep.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kpietrzak.sklep.model.Cart;
import pl.kpietrzak.sklep.model.CartItem;
import pl.kpietrzak.sklep.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
    }

    @Transactional
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Optional<Cart> getCartById(Long id) {
        return cartRepository.findById(id);
    }

    @Transactional
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Transactional
    public Cart addItemToCart(Long cartId, Long itemId) {
        Cart cart = getCartById(cartId).orElseThrow(() -> new RuntimeException("Cart is not found."));
        CartItem item = cartItemService.getCartItemById(itemId).orElseThrow(() -> new RuntimeException("CartItem is not found."));
        cart.getCartItems().add(item);
        item.setCart(cart);
        cartItemService.saveCartItem(item);
        return saveCart(cart);
    }

    @Transactional
    public Cart removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = getCartById(cartId).orElseThrow(() -> new RuntimeException("Error: Cart is not found."));
        CartItem item = cartItemService.getCartItemById(itemId).orElseThrow(() -> new RuntimeException("Error: CartItem is not found."));
        cart.getCartItems().remove(item);
        item.setCart(null);
        cartItemService.saveCartItem(item);
        return saveCart(cart);
    }

    public Cart clearCart(Long cartId) {
        Cart cart = getCartById(cartId).orElseThrow(() -> new RuntimeException("Error: Cart is not found."));
        List<CartItem> cartItems = cart.getCartItems();
        for(CartItem item : cartItems) {
            item.setCart(null);
            cartItemService.saveCartItem(item);
        }
        cartItems.clear();
        return saveCart(cart);
    }
}