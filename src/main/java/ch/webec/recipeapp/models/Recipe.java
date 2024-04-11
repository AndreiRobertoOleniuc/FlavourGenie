package ch.webec.recipeapp.models;

import java.util.List;

public record Recipe(String recipeName,
                     List<Ingredient> ingredients,
                     List<String> categories,
                     String recipeDifficulty,
                     String cookingTime,
                     String recipeImageDescription,
                     String instruction,
                     String recipeImage) {
    public record Ingredient(String name, String quantity, String unit) {
    }
}
