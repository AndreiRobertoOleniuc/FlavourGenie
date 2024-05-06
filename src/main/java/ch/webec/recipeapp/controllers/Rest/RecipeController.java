package ch.webec.recipeapp.controllers.Rest;

import ch.webec.recipeapp.models.CreateRecipeRequest.RecipeRequest;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.services.RecipeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ch.webec.recipeapp.models.User;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/api/recipes")
    public Recipe createRecipe(@RequestBody RecipeRequest recipeRequest, Model model) {
        String[] ingredients = recipeRequest.ingredients().toArray(new String[0]);
        User user = (User) model.getAttribute("user");
        return recipeService.generateRecipe(ingredients, recipeRequest.generateImage(), user);
    }

    @GetMapping("/api/recipes")
    public List<Recipe> getRecipe() {
        return recipeService.getRecipe();
    }
}
