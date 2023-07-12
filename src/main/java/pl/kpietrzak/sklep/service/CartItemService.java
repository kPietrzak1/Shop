package pl.kpietrzak.sklep.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.kpietrzak.sklep.model.Cart;
import pl.kpietrzak.sklep.model.CartItem;
import pl.kpietrzak.sklep.model.Product;
import pl.kpietrzak.sklep.repository.CartItemRepository;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;

    public CartItemService(CartItemRepository cartItemRepository, CartService cartService, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    public List<CartItem> getCartItemsByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    public CartItem addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCartByUserId(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new ResourceNotFoundException("Cart item not found with id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) {
        return cartItemRepository.findById(cartItemId).map(cartItem -> {
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }).orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
    }
}

