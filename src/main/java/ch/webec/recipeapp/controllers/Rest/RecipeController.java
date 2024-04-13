package ch.webec.recipeapp.controllers.Rest;

import ch.webec.recipeapp.models.CreateRecipeRequest.RecipeRequest;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.services.RecipeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/api/recipes")
    public Recipe createRecipe(@RequestBody RecipeRequest recipeRequest) {
        String[] ingredients = recipeRequest.ingredients().toArray(new String[0]);
        return recipeService.generateRecipe(ingredients, recipeRequest.generateImage());
    }
}
