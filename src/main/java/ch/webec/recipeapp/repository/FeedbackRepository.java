package ch.webec.recipeapp.repository;

import ch.webec.recipeapp.models.Feedback;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer>{
    Feedback findByUserAndRecipe(User user, Recipe recipe);

}
