package com.axiom.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chat messages
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    private String userId;    // ID of the user sending the message
    private String message;   // User's message text
    private String response;  // Response from LLM / FlightService (optional)
}
