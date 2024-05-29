package ch.webec.recipeapp.models;

import java.util.List;

public class RecipeExtended extends Recipe {
    private String createdBy;
    private int aggregatedFeedback;

    public RecipeExtended(String recipeName, List<Ingredient> ingredients, List<Category> categories, String recipeDifficulty, String description, String cookingTime, String recipeImageDescription, String instruction, String recipeImage, User user, String createdBy, int aggregatedFeedback) {
        super(recipeName, ingredients, categories, recipeDifficulty, description, cookingTime, recipeImageDescription, instruction, recipeImage, user);
        this.createdBy = createdBy;
        this.aggregatedFeedback = aggregatedFeedback;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public int getAggregatedFeedback() {
        return aggregatedFeedback;
    }

}
