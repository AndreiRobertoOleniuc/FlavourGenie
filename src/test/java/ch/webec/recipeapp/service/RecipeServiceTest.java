package ch.webec.recipeapp.service;

import ch.webec.recipeapp.models.*;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.ChatResponse;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.Message;
import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.services.RecipeService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {
    RecipeService service;
    User user;

    RecipeServiceTest() {
        List<Recipe> recipes = new ArrayList<>();
        //Generated a lot of recipes
        user = new User(
                "andrei@gmail.com",
                "Andrei",
                "B",
                "https://example.com/picture.jpg"
        );
        for (int i = 0; i < 10; i++) {
            Recipe recipe = getRecipe(i, user);
            recipes.add(recipe);
        }

        var feedbackStub = mock(FeedbackRepository.class);
        for (Recipe recipe : recipes) {
            when(feedbackStub.findAllByRecipe(recipe)).thenReturn(new ArrayList<>(List.of(
                    new Feedback(5, user, recipe),
                    new Feedback(5, user, recipe),
                    new Feedback(5, user, recipe)
            )));
        }

        var recipeStub = mock(RecipeRepository.class);
        when(recipeStub.findAll()).thenReturn(recipes);
        for (Recipe recipe : recipes) {
            when(recipeStub.findById(recipe.getId().intValue())).thenReturn(Optional.of(recipe));
            doAnswer(invocation -> {
                recipes.remove(recipe);
                return null;
            }).when(recipeStub).deleteById(recipe.getId().intValue());
            doAnswer(invocation -> {
                recipes.add(recipe);
                return recipe;
            }).when(recipeStub).save(recipe);
        }

        service = new RecipeService(null,null,null,recipeStub,feedbackStub);
    }

    private static Recipe getRecipe(int i, User user) {
        Recipe recipe = new Recipe(
                "Recipe" + i,
                List.of(new Ingredient("Ingredient one", "100", "g"),
                        new Ingredient("Ingredient two" , "500", "kg")),
                List.of(
                        new Category("Category one"),
                        new Category("Category two")
                ),
                "Easy",
                "Description",
                "30 minutes",
                "Image description",
                "Instruction",
                "https://example.com/image" + i + ".jpg",
                user
        );
        recipe.setRecipeId((long) i);
        return recipe;
    }

    @Test
    void testGetAllRecipes() {
        List<RecipeExtended> recipe = service.getAllRecipes();
        assertEquals(10, recipe.size());
    }


    @Test
    void testToRecipeFromJSON(){
        ChatResponse chatResponse = getChatResponse();
        Recipe recipe = service.toRecipe(chatResponse, "https://example.com/image.jpg", user);
        assertEquals("Beef Stir-Fry with Garlic Ginger Sauce", recipe.getRecipeName());
        assertEquals(5, recipe.getIngredients().size());
        assertEquals(2, recipe.getCategories().size());
        assertEquals("medium", recipe.getRecipeDifficulty());
        assertEquals("A delicious and flavorful beef stir-fry with a fragrant garlic ginger sauce, served over a bed of steamed rice.", recipe.getDescription());
        assertEquals("30 minutes", recipe.getCookingTime());
        assertEquals("Beef, Garlic, Ginger, Rice, Flour", recipe.getRecipeImageDescription());
        assertEquals("1. Marinate beef slices in a mixture of flour, garlic, and ginger.\n2. In a hot pan, stir-fry the marinated beef until cooked.\n3. Prepare a sauce using garlic, ginger, and honey.\n4. Add the sauce to the cooked beef and simmer for a few minutes.\n5. Serve the beef stir-fry over steamed rice. Enjoy!", recipe.getInstruction());
        assertEquals("https://example.com/image.jpg", recipe.getRecipeImage());
        assertEquals(user, recipe.getUser());
    }

    private static ChatResponse getChatResponse() {
        String json = "{\"recipeName\":\"Beef Stir-Fry with Garlic Ginger Sauce\",\"ingredients\":[{\"name\":\"Beef\",\"quantity\":\"250\",\"unit\":\"g\"},{\"name\":\"Garlic\",\"quantity\":\"2\",\"unit\":\"cloves\"},{\"name\":\"Ginger\",\"quantity\":\"1\",\"unit\":\"tbsp\"},{\"name\":\"Rice\",\"quantity\":\"1\",\"unit\":\"cup\"},{\"name\":\"Flour\",\"quantity\":\"2\",\"unit\":\"tbsp\"}],\"categories\":[\"Dinner\",\"Asian Cuisine\"],\"recipeDifficulty\":\"medium\",\"description\":\"A delicious and flavorful beef stir-fry with a fragrant garlic ginger sauce, served over a bed of steamed rice.\",\"cookingTime\":\"30 minutes\",\"recipeImageDescription\":\"Beef, Garlic, Ginger, Rice, Flour\",\"instruction\":\"1. Marinate beef slices in a mixture of flour, garlic, and ginger.\\n2. In a hot pan, stir-fry the marinated beef until cooked.\\n3. Prepare a sauce using garlic, ginger, and honey.\\n4. Add the sauce to the cooked beef and simmer for a few minutes.\\n5. Serve the beef stir-fry over steamed rice. Enjoy!\"}";
        ChatResponse.Usage usage = new ChatResponse.Usage(10,10,10);
        List<ChatResponse.Choice> choices = new ArrayList<>();
        choices.add(
                new ChatResponse.Choice(0, new Message(
                        "assistant",json
                ))
        );
        return new ChatResponse(choices,usage);
    }

    @Test
    void testFindRecipeById() {
        Recipe recipe = service.getRecipe(0);
        assertEquals("Recipe0", recipe.getRecipeName());
        assertEquals(2, recipe.getIngredients().size());
        assertEquals(2, recipe.getCategories().size());
        assertEquals("Easy", recipe.getRecipeDifficulty());
        assertEquals("Description", recipe.getDescription());
        assertEquals("30 minutes", recipe.getCookingTime());
        assertEquals("Image description", recipe.getRecipeImageDescription());
        assertEquals("Instruction", recipe.getInstruction());
        assertEquals("https://example.com/image0.jpg", recipe.getRecipeImage());
        assertEquals(user, recipe.getUser());
    }

    @Test
    void testDeleteRecipe() {
        Recipe recipe = service.getRecipe(0);
        service.deleteRecipe(0);
        assertFalse(service.getAllRecipes().contains(recipe));
    }

    @Test
    void testCreatedByRecipe() {
        Recipe recipe = service.getRecipe(0);
        assertEquals(user.getFirstName() + " " + user.getLastName() , service.getCreatedByForRecipe(recipe));
    }

}
