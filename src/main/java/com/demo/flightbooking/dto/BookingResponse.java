package com.demo.flightbooking.dto;

import com.demo.flightbooking.enums.BookingStatus;
import com.demo.flightbooking.enums.TravelClass;

import java.time.LocalDateTime;

public class BookingResponse {
    private Long bookingId;
    private String ticketNumber;
    private Long flightId;
    private Long returnFlightId;
    private String flightNumber;
    private String returnFlightNumber;
    private String airline;
    private String returnAirline;
    private String source;
    private String destination;
    private String returnSource;
    private String returnDestination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime returnDepartureTime;
    private LocalDateTime returnArrivalTime;
    private String passengerName;
    private String passengerEmail;
    private Boolean bookingForSelf;
    private Boolean roundTrip;
    private TravelClass travelClass;
    private Integer seatsBooked;
    private Double pricePerSeat;
    private Double returnPricePerSeat;
    private Double discountPercentage;
    private Double returnDiscountPercentage;
    private Double outboundTotalPrice;
    private Double returnTotalPrice;
    private Double totalPrice;
    private BookingStatus status;
    private LocalDateTime bookedAt;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getReturnFlightId() {
        return returnFlightId;
    }

    public void setReturnFlightId(Long returnFlightId) {
        this.returnFlightId = returnFlightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getReturnFlightNumber() {
        return returnFlightNumber;
    }

    public void setReturnFlightNumber(String returnFlightNumber) {
        this.returnFlightNumber = returnFlightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getReturnAirline() {
        return returnAirline;
    }

    public void setReturnAirline(String returnAirline) {
        this.returnAirline = returnAirline;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReturnSource() {
        return returnSource;
    }

    public void setReturnSource(String returnSource) {
        this.returnSource = returnSource;
    }

    public String getReturnDestination() {
        return returnDestination;
    }

    public void setReturnDestination(String returnDestination) {
        this.returnDestination = returnDestination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getReturnDepartureTime() {
        return returnDepartureTime;
    }

    public void setReturnDepartureTime(LocalDateTime returnDepartureTime) {
        this.returnDepartureTime = returnDepartureTime;
    }

    public LocalDateTime getReturnArrivalTime() {
        return returnArrivalTime;
    }

    public void setReturnArrivalTime(LocalDateTime returnArrivalTime) {
        this.returnArrivalTime = returnArrivalTime;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public Boolean getBookingForSelf() {
        return bookingForSelf;
    }

    public void setBookingForSelf(Boolean bookingForSelf) {
        this.bookingForSelf = bookingForSelf;
    }

    public Boolean getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(Boolean roundTrip) {
        this.roundTrip = roundTrip;
    }

    public TravelClass getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(TravelClass travelClass) {
        this.travelClass = travelClass;
    }

    public Integer getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public Double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(Double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public Double getReturnPricePerSeat() {
        return returnPricePerSeat;
    }

    public void setReturnPricePerSeat(Double returnPricePerSeat) {
        this.returnPricePerSeat = returnPricePerSeat;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getReturnDiscountPercentage() {
        return returnDiscountPercentage;
    }

    public void setReturnDiscountPercentage(Double returnDiscountPercentage) {
        this.returnDiscountPercentage = returnDiscountPercentage;
    }

    public Double getOutboundTotalPrice() {
        return outboundTotalPrice;
    }

    public void setOutboundTotalPrice(Double outboundTotalPrice) {
        this.outboundTotalPrice = outboundTotalPrice;
    }

    public Double getReturnTotalPrice() {
        return returnTotalPrice;
    }

    public void setReturnTotalPrice(Double returnTotalPrice) {
        this.returnTotalPrice = returnTotalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}
