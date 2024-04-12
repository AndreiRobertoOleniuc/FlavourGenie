package ch.webec.recipeapp.repository;

import ch.webec.recipeapp.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
