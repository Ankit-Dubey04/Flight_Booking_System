package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.FlightDto;
import com.demo.flightbooking.exception.ResourceNotFoundException;
import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.repository.AdminFlightRepository;
import org.springframework.stereotype.Service;

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
    public Flight getFlightById(Long id) {
        return adminFlightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));
    }
}
