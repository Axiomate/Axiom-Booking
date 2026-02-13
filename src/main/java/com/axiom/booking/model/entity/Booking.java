package com.axiom.booking.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId; // links booking to a user

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight; // associated flight

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column
    private String seatNumber; // optional, can be assigned dynamically

    @Column(nullable = false)
    private Instant bookedAt = Instant.now();

    @Column
    private Instant updatedAt = Instant.now();

    // Enum for booking status
    public enum BookingStatus {
        CONFIRMED,
        CANCELLED,
        RESCHEDULED
    }
}
