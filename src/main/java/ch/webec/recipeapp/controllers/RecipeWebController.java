package ch.webec.recipeapp.controllers;

import ch.webec.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecipeWebController {

    private RecipeService recipeService;

    public RecipeWebController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe")
    public String recipe(Model model){
        var recipes = recipeService.getAllRecipes();
        model.addAttribute("recipes", recipes);
        return "recipe";
    }
}
