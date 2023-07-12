package pl.kpietrzak.sklep.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.kpietrzak.sklep.model.Cart;
import pl.kpietrzak.sklep.model.CartItem;
import pl.kpietrzak.sklep.model.Product;
import pl.kpietrzak.sklep.model.User;
import pl.kpietrzak.sklep.repository.CartItemRepository;
import pl.kpietrzak.sklep.repository.CartRepository;
import pl.kpietrzak.sklep.repository.UserRepository;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartRepository cartRepository, CartItemRepository userItemRepository, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = userItemRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));
    }

    public Cart createCart(Long userId) {
        User user = userService.getUserById(userId);
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public Cart addProductToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productService.getProductById(productId);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return getCartByUserId(userId);
    }

    public void removeProductFromCart(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new ResourceNotFoundException("Cart item not found with id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> cartItems = cart.getCartItems();
        cartItems.clear();
        cartRepository.save(cart);
    }
}
