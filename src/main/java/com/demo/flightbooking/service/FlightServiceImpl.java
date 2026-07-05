package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.FlightDto;
import com.demo.flightbooking.exception.ResourceNotFoundException;
import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Flight createFlight(FlightDto dto) {

        Flight flight = new Flight();
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setAirline(dto.getAirline());
        flight.setSource(dto.getSource());
        flight.setDestination(dto.getDestination());
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setPrice(dto.getPrice());
        flight.setStatus(dto.getStatus());

        return flightRepository.save(flight);
    }

    @Override
    public Flight updateFlight(Long id, FlightDto dto) {

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));

        flight.setFlightNumber(dto.getFlightNumber());
        flight.setAirline(dto.getAirline());
        flight.setSource(dto.getSource());
        flight.setDestination(dto.getDestination());
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setPrice(dto.getPrice());
        flight.setStatus(dto.getStatus());

        return flightRepository.save(flight);
    }

    @Override
    public void deleteFlight(Long id) {

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));

        flightRepository.delete(flight);
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));
    }
}
