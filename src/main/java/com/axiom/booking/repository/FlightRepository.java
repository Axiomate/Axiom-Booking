package com.axiom.booking.repository;

import com.axiom.booking.model.entity.Flight;
import com.axiom.booking.model.entity.Flight.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    /**
     * Find a flight by its unique flight number
     */
    Flight findByFlightNumber(String flightNumber);

    /**
     * Find all flights departing from a specific airport
     */
    List<Flight> findByDepartureAirport(String departureAirport);

    /**
     * Find all flights arriving at a specific airport
     */
    List<Flight> findByArrivalAirport(String arrivalAirport);

    /**
     * Find all flights with a specific status
     */
    List<Flight> findByStatus(FlightStatus status);

    /**
     * Find flights between dates
     */
    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
}
