package com.axiom.booking.model.entity;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.VectorIndexed;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Document // Redis OM Spring annotation for entity
public class ConversationSummary {

    @Indexed
    private String userId;  // Primary key (unique per user)
    
    private String summaryText; // Condensed conversation summary

   
    private String emotionalTone; // Example: "happy", "neutral", "frustrated"

   
    private Instant lastUpdated; // Timestamp of last update

    @VectorIndexed
    private float[] embeddingVector; // Optional: vector for semantic search
}
