package com.axiom.booking.controller;

import com.axiom.booking.model.dto.ChatMessageDTO;
import com.axiom.booking.service.FlightService;
import com.axiom.booking.service.LLMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking/chat")
@RequiredArgsConstructor
public class ChatController {

    private final LLMService llmService;
    private final FlightService flightService;

    /**
     * Endpoint for user chat messages
     * Example JSON: { "userId": "123", "message": "Book flight FL123 seat 12A" }
     */
    @PostMapping
    public ResponseEntity<ChatMessageDTO> chat(@RequestBody ChatMessageDTO chatMessageDTO) {
        String userId = chatMessageDTO.getUserId();
        String message = chatMessageDTO.getMessage();

        if (userId == null || userId.isBlank() || message == null || message.isBlank()) {
            chatMessageDTO.setResponse("Invalid input 😟");
            return ResponseEntity.badRequest().body(chatMessageDTO);
        }

        String response;

        // Basic intent detection (can later be enhanced with NLP/LLM)
        String lowerMsg = message.toLowerCase();
        if (lowerMsg.contains("book")) {
            response = handleBooking(userId, message);
        } else if (lowerMsg.contains("cancel")) {
            response = handleCancellation(userId, message);
        } else if (lowerMsg.contains("reschedule")) {
            response = handleReschedule(userId, message);
        } else {
            // Non-flight message → send to LLMService for emotional-aware response
            response = llmService.processUserMessage(userId, message);
        }

        chatMessageDTO.setResponse(response);
        return ResponseEntity.ok(chatMessageDTO);
    }

    // ---------------- Flight handling methods ----------------

    private String handleBooking(String userId, String message) {
        // Example parsing: "Book flight FL123 seat 12A"
        try {
            String[] parts = message.split(" ");
            String flightNumber = parts.length > 2 ? parts[2] : null;
            String seatNumber = parts.length > 4 ? parts[4] : "Auto";
            return flightService.bookFlight(userId, flightNumber, seatNumber);
        } catch (Exception e) {
            return "Sorry 😟, could not parse booking request.";
        }
    }

    private String handleCancellation(String userId, String message) {
        // Example parsing: "Cancel flight FL123"
        try {
            String[] parts = message.split(" ");
            String flightNumber = parts.length > 2 ? parts[2] : null;
            return flightService.cancelBooking(userId, flightNumber);
        } catch (Exception e) {
            return "Sorry 😟, could not parse cancellation request.";
        }
    }

    private String handleReschedule(String userId, String message) {
        // Example parsing: "Reschedule from FL123 to FL456 seat 12B"
        try {
            String[] parts = message.split(" ");
            String oldFlight = parts.length > 2 ? parts[2] : null;
            String newFlight = parts.length > 4 ? parts[4] : null;
            String seatNumber = parts.length > 6 ? parts[6] : "Auto";
            return flightService.rescheduleBooking(userId, oldFlight, newFlight, seatNumber);
        } catch (Exception e) {
            return "Sorry 😟, could not parse reschedule request.";
        }
    }
}
