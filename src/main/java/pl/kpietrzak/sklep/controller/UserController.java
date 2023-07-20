package pl.kpietrzak.sklep.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kpietrzak.sklep.model.Cart;
import pl.kpietrzak.sklep.model.User;
import pl.kpietrzak.sklep.service.CartService;
import pl.kpietrzak.sklep.service.UserService;

import java.util.List;

import static pl.kpietrzak.sklep.controller.HomeController.getUser;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public UserController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        Cart cart = new Cart();
        cart = cartService.saveCart(cart);
        user.setCart(cart);
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, Model model) {
        return getUser(username, password, model, request, userService);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
