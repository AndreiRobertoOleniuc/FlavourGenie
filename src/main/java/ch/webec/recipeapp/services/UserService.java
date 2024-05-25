package ch.webec.recipeapp.services;

import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static java.util.Collections.emptyList;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Add PasswordEncoder here
    private final RecipeService recipeService;
    private final FeedbackService feedbackService;

    public UserService(UserRepository userRepository, RecipeService recipeService, FeedbackService feedbackService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.recipeService = recipeService;
        this.feedbackService = feedbackService;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
