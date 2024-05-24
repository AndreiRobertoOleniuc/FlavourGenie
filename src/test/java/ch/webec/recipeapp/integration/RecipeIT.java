package ch.webec.recipeapp.integration;

import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = "/test-data.sql")
public class RecipeIT {

    private final RecipeService service;

    @Autowired
    public RecipeIT(RecipeRepository repository, FeedbackRepository feedbackRepository) {
        this.service = new RecipeService(null, null, null, repository, feedbackRepository);
    }

    @Test
    void testExpectedRecipeFields(){
        var recipes = service.getAllRecipes();
        // assert something
        //Base Fields
        assertEquals(1, recipes.size());
        assertEquals("Test Recipe", recipes.getFirst().getRecipeName());
        assertEquals("Test description", recipes.getFirst().getDescription());
        assertEquals("Test instruction", recipes.getFirst().getInstruction());
        assertEquals("30 minutes", recipes.getFirst().getCookingTime());
        assertEquals("image.jpg", recipes.getFirst().getRecipeImage());

        //User Fields
        assertEquals("test@test.com", recipes.getFirst().getUser().getEmail());
        assertEquals("Test", recipes.getFirst().getUser().getFirstName());
        assertEquals("User", recipes.getFirst().getUser().getLastName());
        assertEquals("picture.jpg", recipes.getFirst().getUser().getPicture());

        //Ingredient Fields
        assertEquals(2, recipes.getFirst().getIngredients().size());
        assertEquals("Sugar", recipes.getFirst().getIngredients().get(0).getName());
        assertEquals("Flour", recipes.getFirst().getIngredients().get(1).getName());

        //Category Fields
        assertEquals(2, recipes.getFirst().getCategories().size());
        assertEquals("Dessert", recipes.getFirst().getCategories().get(0).getCategory());
        assertEquals("Main Course", recipes.getFirst().getCategories().get(1).getCategory());
    }

    @Test
    void testDeleteRecipe(){
        var recipes = service.getAllRecipes();
        assertEquals(1, recipes.size());
        service.deleteRecipe(5);
        recipes = service.getAllRecipes();
        assertEquals(0, recipes.size());
    }
}
