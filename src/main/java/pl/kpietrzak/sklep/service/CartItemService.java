package pl.kpietrzak.sklep.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kpietrzak.sklep.model.CartItem;
import pl.kpietrzak.sklep.repository.CartItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Transactional
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long id, int quantity){
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        CartItem existingCartItem = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("CartItem is not found."));
        existingCartItem.setQuantity(quantity);
        return cartItemRepository.save(existingCartItem);
    }
}

