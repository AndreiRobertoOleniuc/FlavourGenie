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
@Sql(scripts = "/test-data.sql") // SQL script to populate the database
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
        assertEquals(0, recipes.size());
    }
}
