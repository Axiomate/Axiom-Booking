package com.axiom.booking.repository;

import com.axiom.booking.model.entity.Booking;
import com.axiom.booking.model.entity.Flight;
import com.axiom.booking.model.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find all bookings for a specific user
     */
    List<Booking> findByUserId(String userId);

    /**
     * Find all bookings for a specific flight
     */
    List<Booking> findByFlight(Flight flight);

    /**
     * Find all bookings by status
     */
    List<Booking> findByStatus(BookingStatus status);

    /**
     * Find booking by user and flight
     * Useful for checking if user already booked a flight
     */
    Booking findByUserIdAndFlight(String userId, Flight flight);
}
