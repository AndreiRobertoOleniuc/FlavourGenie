package ch.webec.recipeapp.models.OpenAI.Completion;

import java.util.List;

public record ChatRequest(String model, List<Message> messages) {
}
