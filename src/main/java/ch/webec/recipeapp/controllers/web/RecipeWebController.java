package ch.webec.recipeapp.controllers.web;

import ch.webec.recipeapp.errors.InvalidParameterException;
import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.services.FeedbackService;
import ch.webec.recipeapp.services.RecipeService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class RecipeWebController {

    private final FeedbackService feedbackService;
    private final RecipeService recipeService;

    public RecipeWebController(RecipeService recipeService, FeedbackService feedbackService) {
        this.recipeService = recipeService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/recipe")
    public String recipe(Model model){
        var recipes = recipeService.getAllRecipes();
        model.addAttribute("recipes", recipes);
        return "recipe";
    }

    @GetMapping("/create")
    public String createRecipe(){
        return "create";
    }

    @PostMapping("/create")
    public RedirectView createRecipe(@RequestParam @NotBlank String ingredients, Model model){
        if(ingredients.isBlank()){
            throw new InvalidParameterException();
        }
        User user = (User) model.getAttribute("user");
        var recipe = recipeService.generateRecipe(ingredients, true,user);
        return new RedirectView("/recipe/" + recipe.getId());
    }

    @GetMapping("/recipe/{id}")
    public String recipe(Model model, @PathVariable int id){
        var recipe = recipeService.getRecipe(id);
        User user = (User) model.getAttribute("user");
        var currentUserFeedback = feedbackService.findFeedbackByUser(user, recipe);
        model.addAttribute("currentUserFeedback", currentUserFeedback);
        model.addAttribute("currentUser", user);
        model.addAttribute("creationUser", recipe.getUser());
        model.addAttribute("createdBy", recipeService.getCreatedByForRecipe(recipe));
        model.addAttribute("recipe", recipe);
        return "recipeDetail";
    }

    @PostMapping("/recipe/delete/{id}")
    public RedirectView deleteRecipe(@PathVariable int id){
        recipeService.deleteRecipe(id);
        return new RedirectView("/recipe");
    }

}
