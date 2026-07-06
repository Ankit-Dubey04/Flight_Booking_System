package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.ConnectingFlightResponse;
import com.demo.flightbooking.dto.FlightDto;
import com.demo.flightbooking.exception.ResourceNotFoundException;
import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.repository.AdminFlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Comparator;
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
    public List<ConnectingFlightResponse> searchConnectingFlights(String source, String destination, LocalDate departureDate) {
        String normalizedSource = normalizeFilter(source);
        String normalizedDestination = normalizeFilter(destination);

        if (normalizedSource == null || normalizedDestination == null) {
            throw new IllegalArgumentException("Source and destination are required to search connecting flights");
        }

        List<Flight> firstLegs = searchFlights(normalizedSource, null, departureDate);
        List<Flight> secondLegs = searchFlights(null, normalizedDestination, null);

        return firstLegs.stream()
                .flatMap(firstLeg -> secondLegs.stream()
                        .filter(secondLeg -> isValidConnection(firstLeg, secondLeg, normalizedDestination))
                        .map(secondLeg -> toConnectingFlightResponse(firstLeg, secondLeg)))
                .sorted(Comparator.comparing(ConnectingFlightResponse::getTotalTravelMinutes)
                        .thenComparing(response -> response.getFirstFlight().getDepartureTime()))
                .toList();
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

    private boolean isValidConnection(Flight firstLeg, Flight secondLeg, String finalDestination) {
        if (firstLeg.getId().equals(secondLeg.getId())) {
            return false;
        }

        if (!equalsIgnoreCase(firstLeg.getDestination(), secondLeg.getSource())
                || !equalsIgnoreCase(secondLeg.getDestination(), finalDestination)
                || equalsIgnoreCase(firstLeg.getSource(), secondLeg.getDestination())) {
            return false;
        }

        if (firstLeg.getDepartureTime() == null
                || firstLeg.getArrivalTime() == null
                || secondLeg.getDepartureTime() == null
                || secondLeg.getArrivalTime() == null) {
            return false;
        }

        long layoverMinutes = Duration.between(firstLeg.getArrivalTime(), secondLeg.getDepartureTime()).toMinutes();
        return layoverMinutes >= 30 && layoverMinutes <= 720;
    }

    private ConnectingFlightResponse toConnectingFlightResponse(Flight firstLeg, Flight secondLeg) {
        long layoverMinutes = Duration.between(firstLeg.getArrivalTime(), secondLeg.getDepartureTime()).toMinutes();
        long totalTravelMinutes = Duration.between(firstLeg.getDepartureTime(), secondLeg.getArrivalTime()).toMinutes();

        return new ConnectingFlightResponse(
                firstLeg,
                secondLeg,
                firstLeg.getDestination(),
                layoverMinutes,
                totalTravelMinutes,
                discountedTotal(firstLeg.getEconomyPrice(), firstLeg.getPrice(), firstLeg.getDiscountMultiplier(),
                        secondLeg.getEconomyPrice(), secondLeg.getPrice(), secondLeg.getDiscountMultiplier()),
                discountedTotal(firstLeg.getBusinessPrice(), firstLeg.getPrice(), firstLeg.getDiscountMultiplier(),
                        secondLeg.getBusinessPrice(), secondLeg.getPrice(), secondLeg.getDiscountMultiplier()),
                discountedTotal(firstLeg.getFirstClassPrice(), firstLeg.getPrice(), firstLeg.getDiscountMultiplier(),
                        secondLeg.getFirstClassPrice(), secondLeg.getPrice(), secondLeg.getDiscountMultiplier())
        );
    }

    private Double discountedTotal(
            Double firstClassPrice,
            Double firstFallbackPrice,
            Double firstDiscountMultiplier,
            Double secondClassPrice,
            Double secondFallbackPrice,
            Double secondDiscountMultiplier
    ) {
        return defaultPrice(firstClassPrice, firstFallbackPrice) * firstDiscountMultiplier
                + defaultPrice(secondClassPrice, secondFallbackPrice) * secondDiscountMultiplier;
    }

    private Double defaultPrice(Double classPrice, Double fallbackPrice) {
        if (classPrice != null) {
            return classPrice;
        }
        return fallbackPrice != null ? fallbackPrice : 0.0;
    }

    private boolean equalsIgnoreCase(String first, String second) {
        return first != null && second != null && first.equalsIgnoreCase(second);
    }
}
