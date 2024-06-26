package ch.webec.recipeapp.config;

import ch.webec.recipeapp.models.OpenAI.ChatCompletion.Message;

public class RecipePromptsConfig {
    public static Message getMessage() {
        String systemPrompt = "You are a nutrition expert, " +
                "which helps create meals from " +
                "a given list of ingredients. For each meal you create, a list of ingredients will be provided. " +
                "Each meal needs to have a description of how to prepare it. " +
                "Make sure that each meal only uses the ingredients provided. " +
                "Create only one meal per list of ingredients. " +
                "Please present your findings in a structured JSON format, using the template: ";
        String expectedJSON = "{" +
                "\\\"recipeName\\\": string, " +
                "\\\"ingredients\\\":[" +
                "{" +
                "\\\"name\\\": string, // ingredient name," +
                "\\\"quantity\\\": string, // quantity of the ingredient" +
                "\\\"unit\\\": string // unit of the ingredient" +
                " }" +
                " ]," +
                "\\\"categories\\\": [list of categories this meal belongs to]," +
                "\\\"recipeDifficulty\\\": string, // easy, medium, hard" +
                "\\\"description\\\": string, // a short description of the recipe" +
                "\\\"cookingTime\\\": string," +
                "\\\"recipeImageDescription\\\": string, // Only use the ingredients that are mentioned" +
                "\\\"instruction\\\": string" +
                "}." +
                "Remove ALL whitespaces from the JSON in the response. The JSON should be minified.";
        return new Message("system", systemPrompt + expectedJSON);
    }

    public static String getImagePrompt() {
        return "You will get a description of a recipe then generate an image from it: \n: ";
    }

}
