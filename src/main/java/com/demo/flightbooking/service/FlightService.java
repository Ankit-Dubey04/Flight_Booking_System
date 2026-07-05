package com.demo.flightbooking.service;

public class FlightService {
    Flight createFlight(FlightDto flightDto);

    Flight updateFlight(Long id, FlightDto flightDto);

    void deleteFlight(Long id);

    List<Flight> getAllFlights();

    Flight getFlightById(Long id);
}
