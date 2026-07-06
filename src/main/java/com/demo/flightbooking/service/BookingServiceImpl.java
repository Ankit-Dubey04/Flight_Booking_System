package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.BookingRequest;
import com.demo.flightbooking.dto.BookingResponse;
import com.demo.flightbooking.enums.BookingStatus;
import com.demo.flightbooking.enums.TravelClass;
import com.demo.flightbooking.exception.ResourceNotFoundException;
import com.demo.flightbooking.exceptions.BookingNotFoundException;
import com.demo.flightbooking.exceptions.InsufficientSeatsException;
import com.demo.flightbooking.exceptions.UserNotFoundException;
import com.demo.flightbooking.models.Booking;
import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.models.User;
import com.demo.flightbooking.repository.AdminFlightRepository;
import com.demo.flightbooking.repository.BookingRepository;
import com.demo.flightbooking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AdminFlightRepository flightRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            AdminFlightRepository flightRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        User user = getCurrentUser();
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        int requestedSeats = request.getSeatsBooked();
        double pricePerSeat = getPricePerSeat(flight, request.getTravelClass());
        double discountPercentage = defaultDiscount(flight.getDiscountPercentage());
        double discountedPricePerSeat = pricePerSeat * flight.getDiscountMultiplier();

        reserveSeats(flight, request.getTravelClass(), requestedSeats);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setTicketNumber(generateTicketNumber());
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setTravelClass(request.getTravelClass());
        booking.setSeatsBooked(requestedSeats);
        booking.setPricePerSeat(discountedPricePerSeat);
        booking.setDiscountPercentage(discountPercentage);
        booking.setTotalPrice(discountedPricePerSeat * requestedSeats);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPassengerName(user.getName());
        booking.setPassengerEmail(user.getEmail());

        return toResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> getMyBookings() {
        User user = getCurrentUser();
        return bookingRepository.findByUserIdOrderByBookedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BookingResponse getMyBookingByTicket(String ticketNumber) {
        User user = getCurrentUser();
        return toResponse(getOwnedBooking(ticketNumber, user.getId()));
    }

    @Override
    @Transactional
    public BookingResponse cancelMyBooking(String ticketNumber) {
        User user = getCurrentUser();
        Booking booking = getOwnedBooking(ticketNumber, user.getId());

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return toResponse(booking);
        }

        Flight flight = booking.getFlight();
        restoreSeats(flight, booking.getTravelClass(), booking.getSeatsBooked());
        booking.setStatus(BookingStatus.CANCELLED);

        flightRepository.save(flight);
        return toResponse(bookingRepository.save(booking));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private void reserveSeats(Flight flight, TravelClass travelClass, int requestedSeats) {
        int availableSeats;

        switch (travelClass) {
            case ECONOMY -> {
                availableSeats = defaultSeats(flight.getEconomySeatsAvailable());
                if (availableSeats < requestedSeats) {
                    throw new InsufficientSeatsException("Not enough economy seats available");
                }
                flight.setEconomySeatsAvailable(availableSeats - requestedSeats);
            }
            case BUSINESS -> {
                availableSeats = defaultSeats(flight.getBusinessSeatsAvailable());
                if (availableSeats < requestedSeats) {
                    throw new InsufficientSeatsException("Not enough business seats available");
                }
                flight.setBusinessSeatsAvailable(availableSeats - requestedSeats);
            }
            case FIRST_CLASS -> {
                availableSeats = defaultSeats(flight.getFirstClassSeatsAvailable());
                if (availableSeats < requestedSeats) {
                    throw new InsufficientSeatsException("Not enough first class seats available");
                }
                flight.setFirstClassSeatsAvailable(availableSeats - requestedSeats);
            }
        }
    }

    private void restoreSeats(Flight flight, TravelClass travelClass, int seatsToRestore) {
        switch (travelClass) {
            case ECONOMY -> flight.setEconomySeatsAvailable(defaultSeats(flight.getEconomySeatsAvailable()) + seatsToRestore);
            case BUSINESS -> flight.setBusinessSeatsAvailable(defaultSeats(flight.getBusinessSeatsAvailable()) + seatsToRestore);
            case FIRST_CLASS -> flight.setFirstClassSeatsAvailable(defaultSeats(flight.getFirstClassSeatsAvailable()) + seatsToRestore);
        }
    }

    private double getPricePerSeat(Flight flight, TravelClass travelClass) {
        return switch (travelClass) {
            case ECONOMY -> defaultPrice(flight.getEconomyPrice(), flight.getPrice());
            case BUSINESS -> defaultPrice(flight.getBusinessPrice(), flight.getPrice());
            case FIRST_CLASS -> defaultPrice(flight.getFirstClassPrice(), flight.getPrice());
        };
    }

    private double defaultPrice(Double classPrice, Double fallbackPrice) {
        if (classPrice != null) {
            return classPrice;
        }
        if (fallbackPrice != null) {
            return fallbackPrice;
        }
        throw new IllegalStateException("Price is not configured for this flight");
    }

    private int defaultSeats(Integer seats) {
        return seats != null ? seats : 0;
    }

    private double defaultDiscount(Double discountPercentage) {
        return discountPercentage != null ? discountPercentage : 0.0;
    }

    private String generateTicketNumber() {
        return "TKT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    private Booking getOwnedBooking(String ticketNumber, Long userId) {
        Booking booking = bookingRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for ticket: " + ticketNumber));

        if (!booking.getUser().getId().equals(userId)) {
            throw new BookingNotFoundException("Booking not found for ticket: " + ticketNumber);
        }

        return booking;
    }

    private BookingResponse toResponse(Booking booking) {
        Flight flight = booking.getFlight();

        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setTicketNumber(booking.getTicketNumber());
        response.setFlightId(flight.getId());
        response.setFlightNumber(flight.getFlightNumber());
        response.setAirline(flight.getAirline());
        response.setSource(flight.getSource());
        response.setDestination(flight.getDestination());
        response.setDepartureTime(flight.getDepartureTime());
        response.setArrivalTime(flight.getArrivalTime());
        response.setPassengerName(booking.getPassengerName());
        response.setPassengerEmail(booking.getPassengerEmail());
        response.setTravelClass(booking.getTravelClass());
        response.setSeatsBooked(booking.getSeatsBooked());
        response.setPricePerSeat(booking.getPricePerSeat());
        response.setDiscountPercentage(booking.getDiscountPercentage());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus());
        response.setBookedAt(booking.getBookedAt());
        return response;
    }
}
