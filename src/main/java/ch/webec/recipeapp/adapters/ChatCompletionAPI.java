package ch.webec.recipeapp.adapters;

import ch.webec.recipeapp.models.OpenAI.ChatCompletion.ChatRequest;
import ch.webec.recipeapp.models.OpenAI.ChatCompletion.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ChatCompletionAPI {

    private final WebClient webClient;

    public ChatCompletionAPI() {
        String openAIKey = System.getenv("OPENAI_API_KEY");
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + openAIKey)
                .build();
    }

    public ChatResponse generateRecipe(ChatRequest chatRequest){
        return this.webClient.post().uri("/chat/completions")
                        .bodyValue(chatRequest)
                        .retrieve()
                        .bodyToMono(ChatResponse.class)
                        .block();
    }

}
