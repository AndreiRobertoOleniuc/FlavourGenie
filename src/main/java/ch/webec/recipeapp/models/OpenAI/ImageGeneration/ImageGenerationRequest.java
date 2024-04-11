package ch.webec.recipeapp.models.OpenAI.ImageGeneration;

public record ImageGenerationRequest(String model, String prompt, String size) {
}
