package ch.webec.recipeapp.repository;

import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findAllByUser(User user);
}
