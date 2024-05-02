package ch.webec.recipeapp.controllers;

import ch.webec.recipeapp.services.RecipeService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

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
    public String createRecipe(@RequestParam @NotBlank String ingredients){
        String[] ingredientArray = ingredients.split(","); // Split the comma-separated string
        System.out.println(Arrays.toString(ingredientArray));
        recipeService.generateRecipe(ingredientArray, true);
        return "recipe";
    }

}
