package ch.webec.recipeapp.services;

import ch.webec.recipeapp.models.Feedback;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteFeedback(int feedbackId){
        Feedback feedback = feedbackRepo.findById(feedbackId).orElseThrow();
        feedbackRepo.delete(feedback);
    }

    public Feedback findFeedbackByUser(User user, Recipe recipe){
        return feedbackRepo.findByUserAndRecipe(user, recipe);
    }

    public List<Feedback> findAllFeedbackByRecipe(User user){
        return feedbackRepo.findAlByUser(user);
    }
}
