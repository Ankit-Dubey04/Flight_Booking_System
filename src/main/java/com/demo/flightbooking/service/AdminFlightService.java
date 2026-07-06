package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.FlightDto;
import com.demo.flightbooking.models.Flight;

import java.time.LocalDate;
import java.util.List;

public interface AdminFlightService {
    Flight createFlight(FlightDto flightDto);

    Flight updateFlight(Long id, FlightDto flightDto);

    void deleteFlight(Long id);

    List<Flight> getAllFlights();

    List<Flight> searchFlights(String source, String destination, LocalDate departureDate);

    Flight getFlightById(Long id);
}
