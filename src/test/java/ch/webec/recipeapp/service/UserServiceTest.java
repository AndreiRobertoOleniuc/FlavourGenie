package ch.webec.recipeapp.service;

import ch.webec.recipeapp.models.Feedback;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.repository.UserRepository;
import ch.webec.recipeapp.services.FeedbackService;
import ch.webec.recipeapp.services.RecipeService;
import ch.webec.recipeapp.services.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.invocation.InvocationOnMock;

public class UserServiceTest {
    UserService service;
    RecipeService recipeService;
    FeedbackService feedbackService;
    List<Recipe> savedRecipes = new ArrayList<>();
    List<Feedback> feedbacks = new ArrayList<>();

    UserServiceTest() {
        List<User> users = new ArrayList<>();
        //Generated a lot of users
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setUsername("user" + i + "@example.com");
            user.setFirstName("User" + i);
            user.setLastName("LastName" + i);
            user.setPicture("https://example.com/picture" + i + ".jpg");
            users.add(user);
        }
        //Generated a lot of recipes
        for (int i = 0; i < 100; i++) {
            Recipe recipe = new Recipe(
                    "Recipe" + i,
                    List.of(),
                    List.of(),
                    "RecipeDifficulty" + i,
                    "Description" + i,
                    "CookingTime" + i,
                    "RecipeImageDescription" + i,
                    "Instruction" + i,
                   "placeholder",
                    users.get(i)
            );
            recipe.setId((long) i);
            savedRecipes.add(recipe);
        }

        //Generated a lot of feedbacks
        for (int i = 0; i < 100; i++) {
            Feedback feedback = new Feedback(i, users.get(i), savedRecipes.get(i));
            feedback.setId((long) i);
            feedbacks.add(feedback);
        }

        var stubUser = mock(UserRepository.class);
        var stubRecipe = mock(RecipeRepository.class);
        var stubFeedback = mock(FeedbackRepository.class);

        when(stubRecipe.findAll()).thenReturn(savedRecipes);
        when(stubUser.findByUsername("User")).thenReturn(null);

        when(stubFeedback.findAll()).thenReturn(feedbacks);
        when(stubFeedback.findAlByUser(any(User.class))).thenAnswer((InvocationOnMock invocation) -> {
            User user = (User) invocation.getArguments()[0];
            return feedbacks.stream().filter(f -> f.getUser().equals(user)).toList();
        });

        when(stubFeedback.findById(any(Integer.class))).thenAnswer((InvocationOnMock invocation) -> {
            Integer id = (Integer) invocation.getArguments()[0];
            return feedbacks.stream().filter(f -> f.getId().intValue() == id).findFirst();
        });

        doAnswer((InvocationOnMock invocation) -> {
            Feedback feedback = (Feedback) invocation.getArguments()[0];
            feedbacks.remove(feedback);
            return null;
        }).when(stubFeedback).delete(any(Feedback.class));

        for(Recipe recipe : savedRecipes){
            when(stubRecipe.findAllByUser(recipe.getUser())).thenReturn(savedRecipes);
            when(stubRecipe.findById(recipe.getId().intValue())).thenReturn(java.util.Optional.of(recipe));
        }

        when(stubRecipe.save(any(Recipe.class))).thenAnswer((InvocationOnMock invocation) -> {
            Recipe recipe = (Recipe) invocation.getArguments()[0];
            var savedRecipe = savedRecipes.stream().filter(r -> r.getId().equals(recipe.getId())).findFirst().orElse(null);
            int indexSavedRecipe = savedRecipes.indexOf(savedRecipe);
            if(savedRecipe != null){
                savedRecipes.set(indexSavedRecipe, recipe);
            }else{
                savedRecipes.add(recipe);
            }
            return recipe;
        });

        for (User user : users) {
            when(stubUser.findByUsername(user.getUsername())).thenReturn(user);
        }

        feedbackService = new FeedbackService(stubRecipe, stubFeedback);
        recipeService = new RecipeService(null,null,null, stubRecipe, null);
        service = new UserService(stubUser,recipeService,feedbackService);
    }

    @Test
    void testNotFoundUser() {
        User user = service.findUserByUsername("User");
        assertNull(user);
    }

    @Test
    void testFoundUser() {
        User user = service.findUserByUsername("user1@example.com");
        assertNotNull(user);
    }

    @Test
    void testUserProperty() {
        User user = service.findUserByUsername("user1@example.com");
        assertNotNull(user);
        assertEquals("User1", user.getFirstName());
        assertEquals("LastName1", user.getLastName());
        assertEquals("https://example.com/picture1.jpg", user.getPicture());
    }

    @Test
    void testUserEquals() {
        User user1 = service.findUserByUsername("user1@example.com");
        User user2 = service.findUserByUsername("user10@example.com");
        assertNotEquals(user1, user2);
    }

    @Test
    void removeUserFromRecipeWhenDeletingUser() {
        User user1 = service.findUserByUsername("user1@example.com");
        assertNotNull(user1);
        service.deleteUser(user1);
        for (Recipe recipe : savedRecipes) {
            assertNull(recipe.getUser());
        }
        for (Feedback feedback : feedbacks) {
            assertNotEquals(user1, feedback.getUser());
        }
    }
}
