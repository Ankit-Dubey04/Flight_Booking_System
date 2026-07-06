package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.BookingRequest;
import com.demo.flightbooking.dto.BookingResponse;
import com.demo.flightbooking.dto.PassengerProfileResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    List<BookingResponse> getMyBookings();
    BookingResponse getMyBookingByTicket(String ticketNumber);
    List<PassengerProfileResponse> getSavedPassengers();
    String downloadMyTicket(String ticketNumber);
    BookingResponse cancelMyBooking(String ticketNumber);
}
