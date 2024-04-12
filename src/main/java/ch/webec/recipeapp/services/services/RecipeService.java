package ch.webec.recipeapp.services.services;

import ch.webec.recipeapp.adapters.ChatCompletionAPI;
import ch.webec.recipeapp.adapters.GCPCloudStorageAPI;
import ch.webec.recipeapp.adapters.ImageGenerationAPI;
import ch.webec.recipeapp.config.RecipePromptsConfig;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.ChatRequest;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.ChatResponse;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.Message;
import ch.webec.recipeapp.models.OpenAI.ImageGeneration.ImageGenerationRequest;
import ch.webec.recipeapp.models.OpenAI.ImageGeneration.ImageGenerationResponse;
import ch.webec.recipeapp.models.Recipe;
import ch.webec.recipeapp.utils.LoggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final ChatCompletionAPI chatCompletionAPI;
    private final ImageGenerationAPI imageGenerationAPI;
    private final GCPCloudStorageAPI gcpCloudStorageAPI;

    private final String chatModel = "gpt-3.5-turbo";
    private final String imagesModel = "dall-e-3";
    private final String imageSize = "1024x1024";

    public RecipeService(ChatCompletionAPI chatCompletionAPI, ImageGenerationAPI imageGenerationAPI, GCPCloudStorageAPI gcpCloudStorageAPI) {
        this.chatCompletionAPI = chatCompletionAPI;
        this.imageGenerationAPI = imageGenerationAPI;
        this.gcpCloudStorageAPI = gcpCloudStorageAPI;
    }

    public Recipe generateRecipe(String[] ingredients){
        ChatResponse chatResponse = generateRecipeText(ingredients);
        var parsedChatResponse = toRecipe(chatResponse, null);
        //String imageUrlOpenAI = generateImage(parsedChatResponse.recipeImageDescription());
        //String imageUrl = gcpCloudStorageAPI.uploadImage(imageUrlOpenAI);
        return toRecipe(chatResponse, null);
    }

    public ChatResponse generateRecipeText(String[] ingredients){
        Message systemMessage = RecipePromptsConfig.getMessage();
        String result = Arrays.stream(ingredients)
                .collect(Collectors.joining(",", "[", "]"));
        Message userMessage = new Message("user", "I have the following ingredients at home, what could I cook with these: " + result);

        List<Message> messages = Arrays.asList(systemMessage, userMessage);
        ChatRequest chatRequest = new ChatRequest(chatModel, messages);
        var recipeResponse = this.chatCompletionAPI.generateRecipe(chatRequest);
        LoggerUtil.logInfo("Used Ingredients: {}", Arrays.toString(ingredients));
        LoggerUtil.logInfo("Used Tokens: Completion Token: {} , Prompt Token: {} , Total Token: {} ,",
                recipeResponse.usage().completion_tokens(),
                recipeResponse.usage().prompt_tokens(),
                recipeResponse.usage().total_tokens())
        ;
        return recipeResponse;
    }

    public String generateImage(String prompt){
        ImageGenerationResponse imageResponse = imageGenerationAPI.generateImage(new ImageGenerationRequest(imagesModel,  RecipePromptsConfig.getImagePrompt() + prompt, imageSize));
        LoggerUtil.logInfo("Generated Image: ", prompt);
        return imageResponse.data().getFirst().url();
    }

    public Recipe toRecipe(ChatResponse chatResponse, String imageUrl) {
        String jsonContent = chatResponse.choices().getFirst().message().content();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Recipe recipe = objectMapper.readValue(jsonContent, Recipe.class);
            return new Recipe(
                    recipe.recipeName(),
                    recipe.ingredients(),
                    recipe.categories(),
                    recipe.recipeDifficulty(),
                    recipe.description(),
                    recipe.cookingTime(),
                    recipe.recipeImageDescription(),
                    recipe.instruction(),
                    imageUrl
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to Recipe", e);
        }
    }
}
