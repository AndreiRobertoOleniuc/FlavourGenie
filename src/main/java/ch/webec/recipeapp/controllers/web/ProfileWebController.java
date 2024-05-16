package ch.webec.recipeapp.controllers.web;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.services.UserService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ProfileWebController {

    private final UserService userService;

    public ProfileWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@RequestParam @NotBlank String firstname, @RequestParam @NotBlank String lastname, Model model) {
        User user = (User) model.getAttribute("user");
        assert user != null;
        userService.updateUserFirstName(user, firstname);
        userService.updateUserLastName(user, lastname);
        userService.saveUser(user);
        return "profile";
    }

    @PostMapping("/profile-delete")
    public RedirectView deleteProfile(Model model) {
        User user = (User) model.getAttribute("user");
        assert user != null;
        userService.deleteUser(user);
        return new RedirectView("/logout");
    }
}
