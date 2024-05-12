package ch.webec.recipeapp.services;

import ch.webec.recipeapp.models.Feedback;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final RecipeRepository recipeRepo;
    private final FeedbackRepository feedbackRepo;
    private final RecipeService recipeService;

    public FeedbackService(RecipeRepository recipeRepo, FeedbackRepository feedbackRepo, RecipeService recipeService) {
        this.recipeRepo = recipeRepo;
        this.feedbackRepo = feedbackRepo;
        this.recipeService = recipeService;
    }

    public void addOrUpdateFeedback(int recipeId, int rating, User user){
        Recipe recipe = recipeRepo.findById(recipeId).orElseThrow();
        Feedback feedback = recipeService.findFeedbackByUser(user, recipe);
        if (feedback != null) {
            feedback.setRating(rating);
        } else {
            feedback = new Feedback(rating, user, recipe);
        }
        feedbackRepo.save(feedback);
    }
}
