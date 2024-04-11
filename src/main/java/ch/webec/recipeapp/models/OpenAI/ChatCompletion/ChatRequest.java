package ch.webec.recipeapp.models.OpenAI.ChatCompletion;

import java.util.List;

public record ChatRequest(String model, List<Message> messages) {
}
