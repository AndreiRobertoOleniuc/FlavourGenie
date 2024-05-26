package ch.webec.recipeapp.integration;

import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.repository.UserRepository;
import ch.webec.recipeapp.services.FeedbackService;
import ch.webec.recipeapp.services.RecipeService;
import ch.webec.recipeapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = "/test-data.sql")
public class UserIT {
    //Test deleting a User and how it effects the Recipes
    private final UserService userService;
    private final RecipeService recipeService;
    private final FeedbackService feedbackService;

    @Autowired
    public UserIT(UserRepository userRepository, RecipeRepository recipeRepository, FeedbackRepository feedbackRepository) {
        recipeService = new RecipeService(null, null, null, recipeRepository, feedbackRepository);
        feedbackService = new FeedbackService(recipeRepository, feedbackRepository);
        this.userService = new UserService(userRepository, recipeService, feedbackService);
    }

    @Test
    void testDeleteUser() {
        // assert something
        var user = userService.findUserByUsername("test@test.com");
        userService.deleteUser(user);
        var recipes = recipeService.getAllRecipes();
        //Check that no recipe has the user anymore
        recipes.forEach(recipe -> {
            assert recipe.getUser() == null || (!recipe.getUser().equals(user));
        });
        //Check that no feedback has the user anymore
        var feedbacks = feedbackService.findAllFeedbackByRecipe(user);
       assertEquals(0, feedbacks.size());
    }
}
