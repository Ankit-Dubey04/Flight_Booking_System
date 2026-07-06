package com.demo.flightbooking.repository;

import com.demo.flightbooking.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookedAtDesc(Long userId);
    List<Booking> findByUserIdAndBookingForSelfFalseOrderByBookedAtDesc(Long userId);
    Optional<Booking> findByTicketNumber(String ticketNumber);
}
