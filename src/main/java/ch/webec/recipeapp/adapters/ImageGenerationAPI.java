package ch.webec.recipeapp.adapters;

import ch.webec.recipeapp.models.OpenAI.ImageGeneration.ImageGenerationRequest;
import ch.webec.recipeapp.models.OpenAI.ImageGeneration.ImageGenerationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ImageGenerationAPI {

    private final WebClient webClient;

    public ImageGenerationAPI() {
        String openAIKey = System.getenv("OPENAI_API_KEY");
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + openAIKey)
                .build();
    }

    public ImageGenerationResponse generateImage(ImageGenerationRequest request){
        return this.webClient.post().uri("/images/generations")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(ImageGenerationResponse.class)
                        .block();
    }

}
