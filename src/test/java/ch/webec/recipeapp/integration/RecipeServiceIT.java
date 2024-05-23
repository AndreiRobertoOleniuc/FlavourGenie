package ch.webec.recipeapp.integration;

import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
public class RecipeServiceIT {
    RecipeService service;

    @Autowired
    public RecipeServiceIT(RecipeRepository repository, FeedbackRepository feedbackRepository) {
        this.service = new RecipeService(null, null, null, repository, feedbackRepository);
    }

    @Test
    void getAllRecipes(){
        var recipes = service.getAllRecipes();
        // assert something
        //assertNotEquals(0, recipes.size());
    }
}
