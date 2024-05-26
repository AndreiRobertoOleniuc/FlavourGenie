package ch.webec.recipeapp.services;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RecipeService recipeService;
    private final FeedbackService feedbackService;

    public UserService(UserRepository userRepository, RecipeService recipeService, FeedbackService feedbackService) {
        this.userRepository = userRepository;
        this.recipeService = recipeService;
        this.feedbackService = feedbackService;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUserFirstName(User user, String firstName) {
        user.setFirstName(firstName);
    }

    public void updateUserLastName(User user, String lastName) {
        user.setLastName(lastName);
    }

    public void deleteUser(User user) {
        recipeService.getRecipeByUser(user).forEach(recipe ->{
            recipe.setUser(null);
            recipeService.updateRecipe(recipe);
        });
        feedbackService.findAllFeedbackByRecipe(user)
                .forEach(feedback ->
                        feedbackService.deleteFeedback(feedback.getId().intValue())
                );
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
