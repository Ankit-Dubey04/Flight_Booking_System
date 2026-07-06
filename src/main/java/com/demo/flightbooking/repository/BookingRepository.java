package com.demo.flightbooking.repository;

import com.demo.flightbooking.enums.BookingStatus;
import com.demo.flightbooking.enums.TravelClass;
import com.demo.flightbooking.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookedAtDesc(Long userId);
    List<Booking> findByUserIdAndBookingForSelfFalseOrderByBookedAtDesc(Long userId);
    List<Booking> findByFlightIdAndTravelClassAndStatus(Long flightId, TravelClass travelClass, BookingStatus status);
    List<Booking> findByReturnFlightIdAndTravelClassAndStatus(Long returnFlightId, TravelClass travelClass, BookingStatus status);
    Optional<Booking> findByTicketNumber(String ticketNumber);
}
