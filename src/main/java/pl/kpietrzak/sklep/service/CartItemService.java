package pl.kpietrzak.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.kpietrzak.sklep.model.Cart;
import pl.kpietrzak.sklep.model.CartItem;
import pl.kpietrzak.sklep.model.Product;
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

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    public CartItem updateCartItemQuantity(Long id, int quantity){
        CartItem existingCartItem = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: CartItem is not found."));
        existingCartItem.setQuantity(quantity);
        return cartItemRepository.save(existingCartItem);
    }
}

