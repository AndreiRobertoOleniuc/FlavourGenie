package ch.webec.recipeapp.models.OpenAI;

import java.util.List;

public record ChatRequest(String model, List<Message> messages) {
}
