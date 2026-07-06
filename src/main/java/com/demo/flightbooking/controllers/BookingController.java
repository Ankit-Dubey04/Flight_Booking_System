package com.demo.flightbooking.controllers;

import com.demo.flightbooking.dto.BookingRequest;
import com.demo.flightbooking.dto.BookingResponse;
import com.demo.flightbooking.dto.PassengerProfileResponse;
import com.demo.flightbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/ticket/{ticketNumber}")
    public ResponseEntity<BookingResponse> getMyBookingByTicket(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(bookingService.getMyBookingByTicket(ticketNumber));
    }

    @GetMapping("/saved-passengers")
    public ResponseEntity<List<PassengerProfileResponse>> getSavedPassengers() {
        return ResponseEntity.ok(bookingService.getSavedPassengers());
    }

    @PatchMapping("/ticket/{ticketNumber}/cancel")
    public ResponseEntity<BookingResponse> cancelMyBooking(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(bookingService.cancelMyBooking(ticketNumber));
    }
}
