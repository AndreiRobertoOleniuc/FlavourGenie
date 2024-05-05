package ch.webec.recipeapp.controllers;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

//TODO: Implement email and password login
@Controller
public class RegistrationWebController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegistrationWebController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

//    @GetMapping("/register")
//    public String register() {
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public RedirectView register(@RequestParam("email") String email,
//                           @RequestParam("password") String password,
//                           @RequestParam("firstName") String firstName,
//                           @RequestParam("lastName") String lastName) {
//        User user = new User(email,firstName,lastName,"",bCryptPasswordEncoder.encode(password));
//        userService.saveUser(user);
//        return  new RedirectView("/login");
//    }
}
