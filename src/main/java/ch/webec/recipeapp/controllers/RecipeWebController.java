package ch.webec.recipeapp.controllers;

import ch.webec.recipeapp.services.RecipeService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/create")
    public String createRecipe(){
        return "create";
    }

    @PostMapping("/create")
    public String createRecipe(@RequestParam @NotBlank String[] ingredients){
        recipeService.generateRecipe(ingredients, true);
        return "recipe";
    }

}
