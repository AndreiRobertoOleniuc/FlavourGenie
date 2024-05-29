package ch.webec.recipeapp.integration;

import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.services.FeedbackService;
import ch.webec.recipeapp.services.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = "/test-data.sql")
public class FeedbackIT {
    //Test changing a Feedback and how it is presented when then the Recipe gets Requested again
    private final FeedbackService serviceFeedback;
    private final RecipeService serviceRecipe;

    @Autowired
    public FeedbackIT(RecipeRepository recipeRepo, FeedbackRepository feedbackRepo) {
        this.serviceFeedback = new FeedbackService(recipeRepo, feedbackRepo);
        this.serviceRecipe = new RecipeService(null, null, null, recipeRepo, feedbackRepo);
    }

    @Test
    void testChangeFeedback() {
        // assert something
        var recipe = serviceRecipe.getRecipe(6);
        var feedback = serviceFeedback.findFeedbackByUser(recipe.getUser(), recipe);
        assertEquals(5, feedback.getRating());
        serviceFeedback.addOrUpdateFeedback(6, 3, recipe.getUser());
        var feedbackUpdated = serviceFeedback.findFeedbackByUser(recipe.getUser(), recipe);
        assertEquals(3, feedbackUpdated.getRating());
    }
}
