package com.axiom.booking.util;

public class EmojiHelper {

    /**
     * Detects emotional tone from a given text
     * Simple heuristic: looks for positive, negative, neutral keywords
     */
    public static String detectTone(String text) {
        if (text == null || text.isEmpty()) {
            return "neutral";
        }

        String lower = text.toLowerCase();

        // Positive keywords
        String[] positive = {"thank", "happy", "great", "awesome", "good", "pleased"};
        // Negative keywords
        String[] negative = {"sorry", "unfortunately", "delay", "problem", "cancelled", "issue"};

        for (String word : positive) {
            if (lower.contains(word)) return "positive";
        }
        for (String word : negative) {
            if (lower.contains(word)) return "negative";
        }

        // Default neutral
        return "neutral";
    }

    /**
     * Appends emoji based on emotional tone
     */
    public static String addEmoji(String text, String tone) {
        switch (tone) {
            case "positive":
                return text + " 😊";
            case "negative":
                return text + " 😟";
            case "neutral":
            default:
                return text + " 🙂";
        }
    }
}
