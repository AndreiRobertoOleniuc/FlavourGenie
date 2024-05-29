package ch.webec.recipeapp.models.OpenAI.ChatCompletion;

import java.util.List;

public record ChatResponse(List<Choice> choices, Usage usage) {
    public record Choice(int index, Message message) {
    }

    public record Usage(int prompt_tokens, int completion_tokens, int total_tokens) {
    }
}
