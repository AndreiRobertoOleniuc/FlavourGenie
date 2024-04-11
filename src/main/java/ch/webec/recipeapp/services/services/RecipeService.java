package ch.webec.recipeapp.services.services;

import ch.webec.recipeapp.adapters.ChatCompletionAPI;
import ch.webec.recipeapp.config.RecipePromptsConfig;
import ch.webec.recipeapp.models.OpenAI.ChatRequest;
import ch.webec.recipeapp.models.OpenAI.ChatResponse;
import ch.webec.recipeapp.models.OpenAI.Message;
import ch.webec.recipeapp.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final ChatCompletionAPI chatCompletionAPI;

    public RecipeService(ChatCompletionAPI chatCompletionAPI) {
        this.chatCompletionAPI = chatCompletionAPI;
    }

    public ChatResponse generateRecipe(String[] ingredients){
        Message systemMessage = RecipePromptsConfig.getMessage();
        String result = Arrays.stream(ingredients)
                .collect(Collectors.joining(",", "[", "]"));
        Message userMessage = new Message("user", "I have the following ingredients at Home, what could I cook with these: " + result);

        List<Message> messages = Arrays.asList(systemMessage, userMessage);
        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", messages);
        var recipeResponse = this.chatCompletionAPI.generateRecipe(chatRequest);
        LoggerUtil.logInfo("Used Ingredients: {}", Arrays.toString(ingredients));
        LoggerUtil.logInfo("Used Tokens: Completion Token: {} , Prompt Token: {} , Total Token: {} ,",
                recipeResponse.usage().completion_tokens(),
                recipeResponse.usage().prompt_tokens(),
                recipeResponse.usage().total_tokens())
        ;
        return recipeResponse;
    }

}
