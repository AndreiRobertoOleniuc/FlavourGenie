package ch.webec.recipeapp.models.CreateRecipeRequest;

import java.util.List;

public record RecipeRequest(List<String> ingredients) {
}
