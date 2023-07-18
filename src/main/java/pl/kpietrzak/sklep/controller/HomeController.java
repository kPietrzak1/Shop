package pl.kpietrzak.sklep.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kpietrzak.sklep.Utils.SessionUtil;
import pl.kpietrzak.sklep.model.Category;
import pl.kpietrzak.sklep.model.User;
import pl.kpietrzak.sklep.service.CategoryService;
import pl.kpietrzak.sklep.service.UserService;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final CategoryService categoryService;

    public HomeController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
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
    public String registerUser(@RequestBody User user) {
        User existingUser = userService.findByUsername(user.getUsername());

        if (existingUser != null) {
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/login";
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
        request.getSession().setAttribute("username", username);
        return getUser(username, password, model, request, userService);
    }

    @GetMapping("/product")
    public String showProduct(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "product";
    }


    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
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
        boolean isUserLogged = SessionUtil.isUserLogged(request);
        model.addAttribute("isUserLogged", isUserLogged);
        model.addAttribute("username", request.getSession().getAttribute("username"));
        return isUserLogged;
    }
}