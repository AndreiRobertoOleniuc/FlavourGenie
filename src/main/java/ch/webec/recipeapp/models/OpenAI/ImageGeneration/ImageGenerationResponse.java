package ch.webec.recipeapp.models.OpenAI.ImageGeneration;

import java.util.List;

public record ImageGenerationResponse(int created, List<Image> data) {
    public record Image(String revised_prompt, String url) {
    }
}
