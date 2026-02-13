package com.axiom.booking.service;

import com.axiom.booking.model.entity.ConversationSummary;
import com.axiom.booking.repository.ConversationSummaryRepository;
import com.axiom.booking.util.EmojiHelper;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LLMService {

    private final ConversationSummaryRepository summaryRepository;

    // LangChain4j models
    private final GoogleAiGeminiChatModel geminiChatModel;
    private final EmbeddingModel embeddingModel;

    /**
     * Process a user message with context memory, emotional awareness, and vector embedding
     */
    public String processUserMessage(String userId, String message) {

        // 1️⃣ Load or create previous conversation summary
        ConversationSummary summary = summaryRepository.findById(userId)
                .orElseGet(() -> {
                    ConversationSummary cs = new ConversationSummary();
                    cs.setUserId(userId);
                    cs.setSummaryText("");
                    cs.setEmotionalTone("neutral");
                    cs.setLastUpdated(Instant.now());
                    return cs;
                });

        // 2️⃣ Build prompt for Gemini with previous summary
        String prompt = buildPrompt(message, summary);

        // 3️⃣ Call Gemini 2.5 Flash model via LangChain4j
        ChatResponse response = geminiChatModel.chat(
                ChatRequest.builder()
                        .messages(UserMessage.from(prompt))
                        .build()
        );
        String aiResponse = response.aiMessage().text();

        // 4️⃣ Detect emotion and add emojis
        String emotionalTone = EmojiHelper.detectTone(aiResponse);
        String responseWithEmoji = EmojiHelper.addEmoji(aiResponse, emotionalTone);

        // 5️⃣ Update conversation summary
        String updatedSummary = summarizeContext(summary.getSummaryText(), message, aiResponse);
        summary.setSummaryText(updatedSummary);
        summary.setEmotionalTone(emotionalTone);
        summary.setLastUpdated(Instant.now());

        // 6️⃣ Generate vector embedding for semantic memory
        Embedding embedding = embeddingModel.embed(updatedSummary).content();
        summary.setEmbeddingVector(embedding.vector());

        // 7️⃣ Save updated summary back to Redis
        summaryRepository.save(summary);

        return responseWithEmoji;
    }

    /**
     * Builds a prompt with previous summary and current user message
     */
    private String buildPrompt(String userMessage, ConversationSummary summary) {
        String prevSummary = (summary.getSummaryText() == null) ? "" : summary.getSummaryText();
        return """
               You are an airline virtual assistant 😊.
               Previous conversation summary: %s
               User: %s
               Reply positively, empathetically, and include friendly emojis when appropriate:
               """.formatted(prevSummary, userMessage);
    }

    /**
     * Simple context summarization method
     */
    private String summarizeContext(String previousSummary, String userMsg, String aiMsg) {
        String interaction = "\nUser: " + userMsg + "\nAI: " + aiMsg;
        String updated = previousSummary + interaction;

        // Truncate to keep memory manageable
        if (updated.length() > 2000) {
            return updated.substring(updated.length() - 2000);
        }
        return updated;
    }
}
