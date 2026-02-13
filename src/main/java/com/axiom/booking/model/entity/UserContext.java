package com.axiom.booking.model.entity;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.VectorIndexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserContext {

    /**
     * User identifier (primary key for Redis OM)
     */
    @Indexed
    private String userId;

    /**
     * Summary of previous conversations (short text for LLM memory)
     */
    private String conversationSummary;

    /**
     * Emotional tone of the user, e.g., happy, neutral, frustrated
     */
    private String emotionalTone;

    /**
     * Vector representation of conversationSummary for semantic search
     * Dimension depends on the LLM embedding model you use
     */
    @VectorIndexed(dimension = 1536) // example: Gemini embedding size
    private float[] embeddingVector;

    /**
     * Last time this context was updated
     */
    private Instant lastUpdated = Instant.now();
}
