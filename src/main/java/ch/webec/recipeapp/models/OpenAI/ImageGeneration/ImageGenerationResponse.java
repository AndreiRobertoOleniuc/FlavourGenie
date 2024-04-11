package ch.webec.recipeapp.models.OpenAI.ImageGeneration;

public record ImageGenerationResponse(int created, Image image) {
    public record Image(String revised_prompt, String url) {
    }
}
