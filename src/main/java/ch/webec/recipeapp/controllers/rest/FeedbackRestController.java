package ch.webec.recipeapp.controllers.rest;

import ch.webec.recipeapp.errors.InvalidParameterException;
import ch.webec.recipeapp.models.Feedback;
import ch.webec.recipeapp.models.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ch.webec.recipeapp.services.FeedbackService;
@RestController
public class FeedbackRestController {
    private final FeedbackService feedbackService;

    public FeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/feedback/{id}/{rating}")
    public Feedback addFeedback(@PathVariable int id, @PathVariable int rating, Model model){
        if(rating < 1 || rating > 5){
            throw new InvalidParameterException();
        }
        User user = (User) model.getAttribute("user");
        return feedbackService.addOrUpdateFeedback(id, rating,user);
    }
}
