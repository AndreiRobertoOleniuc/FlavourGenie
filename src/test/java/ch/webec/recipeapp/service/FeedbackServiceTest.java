package ch.webec.recipeapp.service;

import ch.webec.recipeapp.models.*;
import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.services.FeedbackService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FeedbackServiceTest {

    FeedbackService service;
    User user;
    Recipe recipe;
    List<Feedback> feedbacks;

    FeedbackServiceTest() {
        user = new User(
                "andrei@gmail.com",
                "Andrei",
                "Oleniuc",
                "https://example.com/picture.jpg");

        recipe = new Recipe(
                "Recipe",
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
                "https://example.com/image.jpg",
                user
        );

        Feedback feedback = new Feedback(1, user, recipe);
        feedbacks = new ArrayList<>(List.of(feedback));

        recipe.setRecipeId(1L);
        var recipeStub = mock(RecipeRepository.class);
        var feedbackStub = mock(FeedbackRepository.class);
        when(recipeStub.findById(recipe.getId().intValue())).thenReturn(Optional.ofNullable(recipe));
        when(feedbackStub.findByUserAndRecipe(user, recipe)).thenReturn(feedback);
        doAnswer(invocation -> {
            Feedback feedbackTemp = invocation.getArgument(0);
            if(feedbacks.stream().anyMatch(f -> f.getRecipe().equals(feedbackTemp.getRecipe()) && f.getUser().equals(feedbackTemp.getUser()))){
                var filteredFeedback = feedbacks.stream().filter(f -> f.getRecipe().equals(feedbackTemp.getRecipe()) && f.getUser().equals(feedbackTemp.getUser())).findFirst();
                assert filteredFeedback.orElse(null) != null;
                filteredFeedback.orElse(null).setRating(feedbackTemp.getRating());
            }else{
                feedbacks.add(feedbackTemp);
            }
            return feedbackTemp;
        }).when(feedbackStub).save(any(Feedback.class));
        service = new FeedbackService(recipeStub, feedbackStub);
    }

    @Test
    void testAddOrUpdateFeedback() {
        service.addOrUpdateFeedback(1, 5, user);
        assertEquals(5, feedbacks.getFirst().getRating());
    }

    @Test
    void updateMultipleFeedbacks(){
        User user2 = new User(
                "2",
                "2",
                "2",
                "2");
        service.addOrUpdateFeedback(1, 3, user2);
        service.addOrUpdateFeedback(1, 2, user);
        assertEquals(3, feedbacks.get(1).getRating());
        assertEquals(2, feedbacks.get(0).getRating());
    }
}
