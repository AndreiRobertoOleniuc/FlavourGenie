package ch.webec.recipeapp.controllers.web;

import ch.webec.recipeapp.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import ch.webec.recipeapp.services.FeedbackService;
@Controller
public class FeedbackWebController {

    private final FeedbackService feedbackService;

    public FeedbackWebController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/feedback/{id}/{rating}")
    public RedirectView addFeedback(@PathVariable int id, @PathVariable int rating, Model model){
        User user = (User) model.getAttribute("user");
        feedbackService.addOrUpdateFeedback(id, rating,user);
        return new RedirectView("/recipe/" + id);
    }
}
