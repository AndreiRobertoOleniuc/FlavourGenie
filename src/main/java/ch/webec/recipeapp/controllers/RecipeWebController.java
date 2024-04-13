package ch.webec.recipeapp.controllers;

import ch.webec.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecipeWebController {

    private RecipeService recipeService;

    public RecipeWebController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe")
    public String recipe() {
        return "recipe";
    }
}
