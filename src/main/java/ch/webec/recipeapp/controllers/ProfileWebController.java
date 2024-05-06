package ch.webec.recipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileWebController {

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }

}
