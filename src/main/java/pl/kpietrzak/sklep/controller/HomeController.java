package pl.kpietrzak.sklep.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kpietrzak.sklep.Utils.SessionUtil;
import pl.kpietrzak.sklep.exceptions.ProductNotFoundException;
import pl.kpietrzak.sklep.model.ApiError;
import pl.kpietrzak.sklep.model.Category;
import pl.kpietrzak.sklep.model.Product;
import pl.kpietrzak.sklep.model.User;
import pl.kpietrzak.sklep.service.CategoryService;
import pl.kpietrzak.sklep.service.ProductService;
import pl.kpietrzak.sklep.service.UserService;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final SessionUtil sessionUtil;

    public HomeController(UserService userService, CategoryService categoryService, SessionUtil sessionUtil, ProductService productService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.sessionUtil = sessionUtil;
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        boolean userLogged = isUserLogged(request, model);
        model.addAttribute("header", userLogged ? "fragments/header_user :: header" : "fragments/header_guest :: header");
        model.addAttribute("footer", userLogged ? "fragments/footer_user :: footer" : "fragments/footer_guest :: footer");

        if (userLogged) {
            return "home_user";
        } else {
            return "home_guest";
        }
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiError> registerUser(@RequestBody User user) {
        User existingUser = userService.findByUsername(user.getUsername());

        if (existingUser != null) {
            return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, "Username already taken."), HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpServletRequest request) {
        if (!userService.authenticate(username, password)) {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }

        request.getSession().setAttribute("isUserLogged", true);
        request.getSession().setAttribute("username", username);
        return "redirect:/";
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        request.getSession().invalidate();
        model.addAttribute("isUserLogged", false);
        return "/home_guest";
    }

    @GetMapping("/product")
    public String showProduct(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "product";
    }

    @GetMapping("/product/byCategory")
    public String getProductsByCategory(@RequestParam List<Long> categories, Model model) {
        List<Product> products = productService.findByCategoryIds(categories);
        model.addAttribute("products", products);
        return "fragments/product_list";
    }


    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            throw new ProductNotFoundException("Product not found.");
        }

        model.addAttribute("category", category);
        return "category";
    }

    @GetMapping("/cart")
    public String showCart() {
        return "cart";
    }

    static String getUser(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request, UserService userService) {
        User user = userService.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Nie znaleziono użytkownika");
            return "login";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Niepoprawne hasło");
            return "login";
        }

        request.getSession().setAttribute("isUserLogged", true);
        request.getSession().setAttribute("username", username);
        return "home_user";
    }

    @ModelAttribute("isUserLogged")
    public boolean isUserLogged(HttpServletRequest request, Model model) {
        boolean isUserLogged = sessionUtil.isUserLogged(request);
        model.addAttribute("isUserLogged", isUserLogged);
        model.addAttribute("username", request.getSession().getAttribute("username"));
        return isUserLogged;
    }
}