package com.axiom.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for storing a summary of a user's previous conversation
 * Used by LLM to maintain context across sessions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummaryDTO {

    private String userId;          // ID of the user
    private String summaryText;     // Condensed summary of previous chats
    private String emotionalTone;   // Example: "happy", "neutral", "frustrated"
    private Instant lastUpdated;    // Timestamp of last conversation
}
