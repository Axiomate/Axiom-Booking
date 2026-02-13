package com.axiom.booking.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String departureAirport;

    @Column(nullable = false)
    private String arrivalAirport;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus status = FlightStatus.SCHEDULED;

    @Column
    private String weatherCondition; // e.g., "Clear", "Storm", "Rainy"

    // Enum for flight status
    public enum FlightStatus {
        SCHEDULED,
        DELAYED,
        CANCELLED,
        BOARDING,
        COMPLETED
    }
}
