package com.axiom.booking.service;

import com.axiom.booking.model.entity.Booking;
import com.axiom.booking.model.entity.Booking.BookingStatus;
import com.axiom.booking.model.entity.Flight;
import com.axiom.booking.model.entity.Flight.FlightStatus;
import com.axiom.booking.repository.BookingRepository;
import com.axiom.booking.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final WeatherService weatherService; // Service to check weather

    /**
     * Book a flight for a user
     */
    public String bookFlight(String userId, String flightNumber, String seatNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber);

        if (flight == null) {
            return "Flight not found 😟";
        }

        if (flight.getStatus() == FlightStatus.CANCELLED) {
            return "Cannot book, flight is cancelled 😟";
        }

        // Check weather conditions
        String weather = weatherService.getWeatherForFlight(flight);
        if ("Storm".equalsIgnoreCase(weather) || "Severe".equalsIgnoreCase(weather)) {
            return "Cannot book flight due to severe weather ⚠️";
        }

        // Check if user already has a booking
        Booking existing = bookingRepository.findByUserIdAndFlight(userId, flight);
        if (existing != null) {
            return "You already have a booking for this flight 🙂";
        }

        // Create booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setFlight(flight);
        booking.setSeatNumber(seatNumber);
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);

        return "Flight booked successfully! 😊 Seat: " + seatNumber;
    }

    /**
     * Cancel a booking
     */
    public String cancelBooking(String userId, String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber);
        if (flight == null) return "Flight not found 😟";

        Booking booking = bookingRepository.findByUserIdAndFlight(userId, flight);
        if (booking == null) return "No booking found to cancel 😟";

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(java.time.Instant.now());
        bookingRepository.save(booking);

        return "Booking cancelled successfully 🙂";
    }

    /**
     * Reschedule a booking to a new flight
     */
    public String rescheduleBooking(String userId, String oldFlightNumber, String newFlightNumber, String seatNumber) {
        Flight oldFlight = flightRepository.findByFlightNumber(oldFlightNumber);
        Flight newFlight = flightRepository.findByFlightNumber(newFlightNumber);

        if (oldFlight == null || newFlight == null) return "Flight not found 😟";

        Booking booking = bookingRepository.findByUserIdAndFlight(userId, oldFlight);
        if (booking == null) return "No existing booking found to reschedule 😟";

        // Check new flight status and weather
        if (newFlight.getStatus() == FlightStatus.CANCELLED) return "Cannot reschedule to cancelled flight 😟";
        String weather = weatherService.getWeatherForFlight(newFlight);
        if ("Storm".equalsIgnoreCase(weather) || "Severe".equalsIgnoreCase(weather)) {
            return "Cannot reschedule due to severe weather ⚠️";
        }

        // Update booking
        booking.setFlight(newFlight);
        booking.setSeatNumber(seatNumber);
        booking.setStatus(BookingStatus.RESCHEDULED);
        booking.setUpdatedAt(java.time.Instant.now());

        bookingRepository.save(booking);

        return "Booking rescheduled successfully! 😊 New Seat: " + seatNumber;
    }

    /**
     * Get all bookings for a user
     */
    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }
}
