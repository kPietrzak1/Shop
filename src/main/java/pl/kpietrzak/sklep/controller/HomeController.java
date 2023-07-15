package pl.kpietrzak.sklep.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kpietrzak.sklep.Utils.SessionUtil;
import pl.kpietrzak.sklep.model.User;
import pl.kpietrzak.sklep.service.UserService;

@Controller
public class HomeController {

    private final UserService userService;
    private boolean logged;

    public HomeController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        boolean isUserLogged = SessionUtil.isUserLogged(request);

        model.addAttribute("header", isUserLogged ? "fragments/header_user :: header" : "fragments/header_guest :: header");
        model.addAttribute("footer", isUserLogged ? "fragments/footer_user :: footer" : "fragments/footer_guest :: footer");

        if (isUserLogged) {
            return "home_user";
        } else {
            return "home_guest";
        }
    }

    @GetMapping("/register")
    public String showRegister(){
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
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Nie znaleziono użytkownika");
            return "login";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Niepoprawne hasło");
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/product")
    public String showProduct() {
        return "product";
    }

    @GetMapping("/category")
    public String showCategory() {
        return "category";
    }

    @GetMapping("/cart")
    public String showCart() {
        return "cart";
    }

    public boolean isUserLogged() {
        return logged;
    }
}