package com.demo.flightbooking.models;

import com.demo.flightbooking.enums.BookingStatus;
import com.demo.flightbooking.enums.TravelClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String ticketNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_flight_id")
    private Flight returnFlight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TravelClass travelClass;

    @Column(nullable = false)
    private Integer seatsBooked;

    @Column(nullable = false)
    private Double pricePerSeat;

    private Double returnPricePerSeat;

    @Column(nullable = false)
    private Double discountPercentage;

    private Double returnDiscountPercentage;

    private Double outboundTotalPrice;

    private Double returnTotalPrice;

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false, length = 100)
    private String passengerName;

    @Column(nullable = false, length = 100)
    private String passengerEmail;

    @Column(nullable = false)
    private Boolean bookingForSelf = true;

    @Column(name = "booked_at", nullable = false, updatable = false)
    private LocalDateTime bookedAt;

    @PrePersist
    protected void onCreate() {
        this.bookedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(Flight returnFlight) {
        this.returnFlight = returnFlight;
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

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}
