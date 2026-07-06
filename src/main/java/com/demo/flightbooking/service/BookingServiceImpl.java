package com.demo.flightbooking.service;

import com.demo.flightbooking.dto.BookingRequest;
import com.demo.flightbooking.dto.BookingResponse;
import com.demo.flightbooking.dto.PassengerProfileResponse;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        Flight returnFlight = resolveReturnFlight(request, flight);

        int requestedSeats = request.getSeatsBooked();
        double pricePerSeat = getPricePerSeat(flight, request.getTravelClass());
        double discountPercentage = defaultDiscount(flight.getDiscountPercentage());
        double discountedPricePerSeat = pricePerSeat * flight.getDiscountMultiplier();
        double outboundTotalPrice = discountedPricePerSeat * requestedSeats;
        Double returnPricePerSeat = null;
        Double returnDiscountPercentage = null;
        Double returnTotalPrice = null;

        if (returnFlight != null) {
            returnPricePerSeat = getPricePerSeat(returnFlight, request.getTravelClass()) * returnFlight.getDiscountMultiplier();
            returnDiscountPercentage = defaultDiscount(returnFlight.getDiscountPercentage());
            returnTotalPrice = returnPricePerSeat * requestedSeats;
        }

        reserveSeats(flight, request.getTravelClass(), requestedSeats);
        if (returnFlight != null) {
            reserveSeats(returnFlight, request.getTravelClass(), requestedSeats);
        }
        flightRepository.save(flight);
        if (returnFlight != null) {
            flightRepository.save(returnFlight);
        }

        Booking booking = new Booking();
        booking.setTicketNumber(generateTicketNumber());
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setReturnFlight(returnFlight);
        booking.setTravelClass(request.getTravelClass());
        booking.setSeatsBooked(requestedSeats);
        booking.setPricePerSeat(discountedPricePerSeat);
        booking.setReturnPricePerSeat(returnPricePerSeat);
        booking.setDiscountPercentage(discountPercentage);
        booking.setReturnDiscountPercentage(returnDiscountPercentage);
        booking.setOutboundTotalPrice(outboundTotalPrice);
        booking.setReturnTotalPrice(returnTotalPrice);
        booking.setTotalPrice(outboundTotalPrice + defaultPrice(returnTotalPrice));
        booking.setStatus(BookingStatus.CONFIRMED);
        boolean bookingForSelf = request.getBookingForSelf() == null || request.getBookingForSelf();
        booking.setBookingForSelf(bookingForSelf);
        booking.setPassengerName(resolvePassengerName(request, user, bookingForSelf));
        booking.setPassengerEmail(resolvePassengerEmail(request, user, bookingForSelf));

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
    public List<PassengerProfileResponse> getSavedPassengers() {
        User user = getCurrentUser();
        Map<String, PassengerProfileResponse> savedPassengers = new LinkedHashMap<>();

        bookingRepository.findByUserIdAndBookingForSelfFalseOrderByBookedAtDesc(user.getId())
                .forEach(booking -> {
                    String key = passengerKey(booking.getPassengerName(), booking.getPassengerEmail());
                    savedPassengers.putIfAbsent(key, new PassengerProfileResponse(
                            booking.getPassengerName(),
                            booking.getPassengerEmail(),
                            booking.getBookedAt()
                    ));
                });

        return List.copyOf(savedPassengers.values());
    }

    @Override
    @Transactional
    public String downloadMyTicket(String ticketNumber) {
        User user = getCurrentUser();
        return buildTicketText(getOwnedBooking(ticketNumber, user.getId()));
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
        if (booking.getReturnFlight() != null) {
            restoreSeats(booking.getReturnFlight(), booking.getTravelClass(), booking.getSeatsBooked());
        }
        booking.setStatus(BookingStatus.CANCELLED);

        flightRepository.save(flight);
        if (booking.getReturnFlight() != null) {
            flightRepository.save(booking.getReturnFlight());
        }
        return toResponse(bookingRepository.save(booking));
    }

    private Flight resolveReturnFlight(BookingRequest request, Flight outboundFlight) {
        if (request.getReturnFlightId() == null) {
            return null;
        }

        if (request.getReturnFlightId().equals(outboundFlight.getId())) {
            throw new IllegalArgumentException("Return flight must be different from outbound flight");
        }

        Flight returnFlight = flightRepository.findById(request.getReturnFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Return flight not found"));

        if (!equalsIgnoreCase(returnFlight.getSource(), outboundFlight.getDestination())
                || !equalsIgnoreCase(returnFlight.getDestination(), outboundFlight.getSource())) {
            throw new IllegalArgumentException("Return flight route must match the reverse of outbound flight route");
        }

        if (returnFlight.getDepartureTime() != null
                && outboundFlight.getArrivalTime() != null
                && returnFlight.getDepartureTime().isBefore(outboundFlight.getArrivalTime())) {
            throw new IllegalArgumentException("Return flight must depart after outbound flight arrives");
        }

        return returnFlight;
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

    private double defaultPrice(Double price) {
        return price != null ? price : 0.0;
    }

    private int defaultSeats(Integer seats) {
        return seats != null ? seats : 0;
    }

    private double defaultDiscount(Double discountPercentage) {
        return discountPercentage != null ? discountPercentage : 0.0;
    }

    private String resolvePassengerName(BookingRequest request, User user, boolean bookingForSelf) {
        if (bookingForSelf) {
            return user.getName();
        }

        if (request.getPassengerName() == null || request.getPassengerName().isBlank()) {
            throw new IllegalArgumentException("Passenger name is required when booking for someone else");
        }

        return request.getPassengerName().trim();
    }

    private String resolvePassengerEmail(BookingRequest request, User user, boolean bookingForSelf) {
        if (bookingForSelf) {
            return user.getEmail();
        }

        if (request.getPassengerEmail() == null || request.getPassengerEmail().isBlank()) {
            throw new IllegalArgumentException("Passenger email is required when booking for someone else");
        }

        return request.getPassengerEmail().trim();
    }

    private String passengerKey(String passengerName, String passengerEmail) {
        return passengerName.trim().toLowerCase() + "|" + passengerEmail.trim().toLowerCase();
    }

    private String generateTicketNumber() {
        return "TKT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    private boolean equalsIgnoreCase(String first, String second) {
        return first != null && second != null && first.equalsIgnoreCase(second);
    }

    private String buildTicketText(Booking booking) {
        Flight outboundFlight = booking.getFlight();
        Flight returnFlight = booking.getReturnFlight();

        StringBuilder ticket = new StringBuilder();
        ticket.append("FLIGHT TICKET\n");
        ticket.append("==============================\n");
        ticket.append("Ticket Number: ").append(booking.getTicketNumber()).append('\n');
        ticket.append("Booking Status: ").append(booking.getStatus()).append('\n');
        ticket.append("Trip Type: ").append(returnFlight == null ? "One Way" : "Round Trip").append('\n');
        ticket.append("Passenger: ").append(booking.getPassengerName()).append('\n');
        ticket.append("Passenger Email: ").append(booking.getPassengerEmail()).append('\n');
        ticket.append("Travel Class: ").append(booking.getTravelClass()).append('\n');
        ticket.append("Seats Booked: ").append(booking.getSeatsBooked()).append('\n');
        ticket.append('\n');
        appendFlightDetails(ticket, "Outbound Flight", outboundFlight, booking.getPricePerSeat(), booking.getDiscountPercentage(), getOutboundTotalPrice(booking));
        if (returnFlight != null) {
            ticket.append('\n');
            appendFlightDetails(ticket, "Return Flight", returnFlight, booking.getReturnPricePerSeat(), booking.getReturnDiscountPercentage(), booking.getReturnTotalPrice());
        }
        ticket.append('\n');
        ticket.append("Total Price: ").append(booking.getTotalPrice()).append('\n');
        ticket.append("Booked At: ").append(booking.getBookedAt()).append('\n');
        return ticket.toString();
    }

    private void appendFlightDetails(
            StringBuilder ticket,
            String title,
            Flight flight,
            Double pricePerSeat,
            Double discountPercentage,
            Double legTotal
    ) {
        ticket.append(title).append('\n');
        ticket.append("------------------------------\n");
        ticket.append("Flight Number: ").append(flight.getFlightNumber()).append('\n');
        ticket.append("Airline: ").append(flight.getAirline()).append('\n');
        ticket.append("Route: ").append(flight.getSource()).append(" to ").append(flight.getDestination()).append('\n');
        ticket.append("Departure: ").append(flight.getDepartureTime()).append('\n');
        ticket.append("Arrival: ").append(flight.getArrivalTime()).append('\n');
        ticket.append("Price Per Seat: ").append(pricePerSeat).append('\n');
        ticket.append("Discount Percentage: ").append(discountPercentage).append('\n');
        ticket.append("Leg Total: ").append(legTotal).append('\n');
    }

    private Double getOutboundTotalPrice(Booking booking) {
        if (booking.getOutboundTotalPrice() != null) {
            return booking.getOutboundTotalPrice();
        }

        return booking.getPricePerSeat() * booking.getSeatsBooked();
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
        Flight returnFlight = booking.getReturnFlight();

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
        if (returnFlight != null) {
            response.setReturnFlightId(returnFlight.getId());
            response.setReturnFlightNumber(returnFlight.getFlightNumber());
            response.setReturnAirline(returnFlight.getAirline());
            response.setReturnSource(returnFlight.getSource());
            response.setReturnDestination(returnFlight.getDestination());
            response.setReturnDepartureTime(returnFlight.getDepartureTime());
            response.setReturnArrivalTime(returnFlight.getArrivalTime());
        }
        response.setPassengerName(booking.getPassengerName());
        response.setPassengerEmail(booking.getPassengerEmail());
        response.setBookingForSelf(booking.getBookingForSelf());
        response.setRoundTrip(returnFlight != null);
        response.setTravelClass(booking.getTravelClass());
        response.setSeatsBooked(booking.getSeatsBooked());
        response.setPricePerSeat(booking.getPricePerSeat());
        response.setReturnPricePerSeat(booking.getReturnPricePerSeat());
        response.setDiscountPercentage(booking.getDiscountPercentage());
        response.setReturnDiscountPercentage(booking.getReturnDiscountPercentage());
        response.setOutboundTotalPrice(getOutboundTotalPrice(booking));
        response.setReturnTotalPrice(booking.getReturnTotalPrice());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus());
        response.setBookedAt(booking.getBookedAt());
        return response;
    }
}
