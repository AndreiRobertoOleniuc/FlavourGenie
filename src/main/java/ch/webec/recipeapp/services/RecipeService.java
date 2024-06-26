package ch.webec.recipeapp.services;

import ch.webec.recipeapp.adapters.ChatCompletionAPI;
import ch.webec.recipeapp.adapters.GCPCloudStorageAPI;
import ch.webec.recipeapp.adapters.ImageGenerationAPI;
import ch.webec.recipeapp.config.RecipePromptsConfig;
import ch.webec.recipeapp.errors.ResourceNotFound;
import ch.webec.recipeapp.models.Feedback;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.ChatRequest;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.ChatResponse;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.Message;
import ch.webec.recipeapp.models.OpenAI.ImageGeneration.ImageGenerationRequest;
import ch.webec.recipeapp.models.OpenAI.ImageGeneration.ImageGenerationResponse;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.models.RecipeExtended;
import ch.webec.recipeapp.models.User;
import ch.webec.recipeapp.repository.FeedbackRepository;
import ch.webec.recipeapp.repository.RecipeRepository;
import ch.webec.recipeapp.utils.LoggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final ChatCompletionAPI chatCompletionAPI;
    private final ImageGenerationAPI imageGenerationAPI;
    private final GCPCloudStorageAPI gcpCloudStorageAPI;

    private final RecipeRepository recipeRepo;
    private final FeedbackRepository feedbackRepo;

    public RecipeService(ChatCompletionAPI chatCompletionAPI, ImageGenerationAPI imageGenerationAPI, GCPCloudStorageAPI gcpCloudStorageAPI, RecipeRepository recipeRepo, FeedbackRepository feedbackRepo) {
        this.chatCompletionAPI = chatCompletionAPI;
        this.imageGenerationAPI = imageGenerationAPI;
        this.gcpCloudStorageAPI = gcpCloudStorageAPI;
        this.recipeRepo = recipeRepo;
        this.feedbackRepo = feedbackRepo;
    }

    public List<RecipeExtended> getAllRecipes() {
        var recipes = recipeRepo.findAll();
        var feedbacks = feedbackRepo.findAll();
        return recipes.stream().map(recipe -> {
            var feedbackList = feedbacks.stream().filter(f -> Objects.equals(f.getRecipe().getId(), recipe.getId())).toList();
            int totalRating = feedbackList.stream().mapToInt(Feedback::getRating).sum();
            int averageRating = feedbackList.isEmpty() ? 0 : Math.round((float) totalRating / feedbackList.size());
            String username = recipe.getUser() != null ? recipe.getUser().getUsername() : "[deleted]";
            return getRecipeExtended(recipe, username, averageRating);
        }).collect(Collectors.toList());
    }

    private static RecipeExtended getRecipeExtended(Recipe recipe, String username, int averageRating) {
        var recipeExtended = new RecipeExtended(
                recipe.getRecipeName(),
                recipe.getIngredients(),
                recipe.getCategories(),
                recipe.getRecipeDifficulty(),
                recipe.getDescription(),
                recipe.getCookingTime(),
                recipe.getRecipeImageDescription(),
                recipe.getInstruction(),
                recipe.getRecipeImage(),
                recipe.getUser(),
                username,
                averageRating
        );
        recipeExtended.setRecipeId(recipe.getId());
        return recipeExtended;
    }

    public Recipe generateRecipe(String ingredients, boolean generateImage, User user) {
        String[] ingredientArray = ingredients.split(","); // Split the comma-separated string
        ChatResponse chatResponse = generateRecipeText(ingredientArray);
        if (generateImage) {
            var parsedChatResponse = toRecipe(chatResponse, null, user);
            String imageUrlOpenAI = generateImage(parsedChatResponse.getRecipeImageDescription());
            String imageUrl = gcpCloudStorageAPI.uploadImage(imageUrlOpenAI, parsedChatResponse.getRecipeName());
            Recipe recipe = toRecipe(chatResponse, imageUrl, user);
            recipeRepo.save(recipe);
            return recipe;
        } else {
            Recipe recipe = toRecipe(chatResponse, null, user);
            recipeRepo.save(recipe);
            return recipe;
        }
    }

    public ChatResponse generateRecipeText(String[] ingredients) {
        Message systemMessage = RecipePromptsConfig.getMessage();
        String result = Arrays.stream(ingredients)
                .collect(Collectors.joining(",", "[", "]"));
        Message userMessage = new Message("user", "I have the following ingredients at home, what could I cook with these: " + result);

        List<Message> messages = Arrays.asList(systemMessage, userMessage);
        String chatModel = "gpt-3.5-turbo";
        ChatRequest chatRequest = new ChatRequest(chatModel, messages);
        var recipeResponse = this.chatCompletionAPI.generateRecipe(chatRequest);
        LoggerUtil.logInfo("Used Ingredient: {}", Arrays.toString(ingredients));
        LoggerUtil.logInfo("Used Tokens: Completion Token: {} , Prompt Token: {} , Total Token: {} ,",
                recipeResponse.usage().completion_tokens(),
                recipeResponse.usage().prompt_tokens(),
                recipeResponse.usage().total_tokens())
        ;
        return recipeResponse;
    }

    public String generateImage(String prompt) {
        String imageSize = "1024x1024";
        String imagesModel = "dall-e-3";
        ImageGenerationResponse imageResponse = imageGenerationAPI.generateImage(new ImageGenerationRequest(imagesModel, RecipePromptsConfig.getImagePrompt() + prompt, imageSize));
        LoggerUtil.logInfo("Generated Image: ", prompt);
        return imageResponse.data().getFirst().url();
    }

    public Recipe toRecipe(ChatResponse chatResponse, String imageUrl, User user) {
        String jsonContent = chatResponse.choices().getFirst().message().content();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Recipe recipe = objectMapper.readValue(jsonContent, Recipe.class);
            return new Recipe(
                    recipe.getRecipeName(),
                    recipe.getIngredients(),
                    recipe.getCategories(),
                    recipe.getRecipeDifficulty(),
                    recipe.getDescription(),
                    recipe.getCookingTime(),
                    recipe.getRecipeImageDescription(),
                    recipe.getInstruction(),
                    imageUrl,
                    user
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to Recipe", e);
        }
    }

    public List<Recipe> getRecipeByUser(User user) {
        return recipeRepo.findAllByUser(user);
    }

    public void updateRecipe(Recipe recipe) {
        recipeRepo.save(recipe);
    }

    public List<Recipe> getRecipe() {
        return recipeRepo.findAll();
    }

    public Recipe getRecipe(int id) {
        return recipeRepo.findById(id).orElseThrow(() -> {
            LoggerUtil.logError("Recipe not found with id: " + id);
            return new ResourceNotFound();
        });
    }

    public void deleteFeedbacksByRecipe(Recipe recipe) {
        var feedbacks = feedbackRepo.findAllByRecipe(recipe);
        feedbackRepo.deleteAll(feedbacks);
    }

    public void deleteRecipe(int id) {
        var recipe = recipeRepo.findById(id).orElseThrow();
        deleteFeedbacksByRecipe(recipe);
        recipeRepo.save(recipe);
        recipeRepo.deleteById(id);
    }

    public String getCreatedByForRecipe(Recipe recipe) {
        if (recipe.getUser() == null) {
            return "[deleted]";
        }
        var lastname = recipe.getUser().getLastName() == null ? "" : recipe.getUser().getLastName();
        var firstname = recipe.getUser().getFirstName() == null ? "" : recipe.getUser().getFirstName();
        return firstname + " " + lastname;
    }
}