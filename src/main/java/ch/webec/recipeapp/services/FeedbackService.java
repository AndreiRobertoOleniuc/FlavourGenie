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

    public FeedbackService(RecipeRepository recipeRepo, FeedbackRepository feedbackRepo) {
        this.recipeRepo = recipeRepo;
        this.feedbackRepo = feedbackRepo;
    }

    public void addOrUpdateFeedback(int recipeId, int rating, User user){
        Recipe recipe = recipeRepo.findById(recipeId).orElseThrow();
        Feedback feedback = findFeedbackByUser(user, recipe);
        if (feedback != null) {
            feedback.setRating(rating);
        } else {
            feedback = new Feedback(rating, user, recipe);
        }
        feedbackRepo.save(feedback);
    }

    public Feedback findFeedbackByUser(User user, Recipe recipe){
        return feedbackRepo.findByUserAndRecipe(user, recipe);
    }
}
