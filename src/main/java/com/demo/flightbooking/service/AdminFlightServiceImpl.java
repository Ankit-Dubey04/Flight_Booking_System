package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.FlightDto;
import com.demo.flightbooking.exception.ResourceNotFoundException;
import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.repository.AdminFlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminFlightServiceImpl implements AdminFlightService {
    private final AdminFlightRepository adminFlightRepository;

    public AdminFlightServiceImpl(AdminFlightRepository adminFlightRepository) {
        this.adminFlightRepository = adminFlightRepository;
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
        flight.setEconomyPrice(dto.getEconomyPrice());
        flight.setBusinessPrice(dto.getBusinessPrice());
        flight.setFirstClassPrice(dto.getFirstClassPrice());
        flight.setEconomySeatsAvailable(dto.getEconomySeatsAvailable());
        flight.setBusinessSeatsAvailable(dto.getBusinessSeatsAvailable());
        flight.setFirstClassSeatsAvailable(dto.getFirstClassSeatsAvailable());
        flight.setDiscountPercentage(dto.getDiscountPercentage());
        flight.setStatus(dto.getStatus());

        return adminFlightRepository.save(flight);
    }

    @Override
    public Flight updateFlight(Long id, FlightDto dto) {

        Flight flight = adminFlightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));

        flight.setFlightNumber(dto.getFlightNumber());
        flight.setAirline(dto.getAirline());
        flight.setSource(dto.getSource());
        flight.setDestination(dto.getDestination());
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setPrice(dto.getPrice());
        flight.setEconomyPrice(dto.getEconomyPrice());
        flight.setBusinessPrice(dto.getBusinessPrice());
        flight.setFirstClassPrice(dto.getFirstClassPrice());
        flight.setEconomySeatsAvailable(dto.getEconomySeatsAvailable());
        flight.setBusinessSeatsAvailable(dto.getBusinessSeatsAvailable());
        flight.setFirstClassSeatsAvailable(dto.getFirstClassSeatsAvailable());
        flight.setDiscountPercentage(dto.getDiscountPercentage());
        flight.setStatus(dto.getStatus());

        return adminFlightRepository.save(flight);
    }

    @Override
    public void deleteFlight(Long id) {

        Flight flight = adminFlightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));

        adminFlightRepository.delete(flight);
    }

    @Override
    public List<Flight> getAllFlights() {
        return adminFlightRepository.findAll();
    }

    @Override
    public List<Flight> searchFlights(String source, String destination, LocalDate departureDate) {
        LocalDateTime startOfDay = departureDate != null ? departureDate.atStartOfDay() : null;
        LocalDateTime endOfDay = departureDate != null ? departureDate.plusDays(1).atStartOfDay() : null;

        return adminFlightRepository.searchFlights(
                normalizeFilter(source),
                normalizeFilter(destination),
                startOfDay,
                endOfDay
        );
    }

    @Override
    public Flight getFlightById(Long id) {
        return adminFlightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));
    }

    private String normalizeFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
