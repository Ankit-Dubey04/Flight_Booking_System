package com.demo.flightbooking.dto;

import com.demo.flightbooking.enums.TravelClass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookingRequest {

    @NotNull(message = "Flight id is required")
    private Long flightId;

    @NotNull(message = "Travel class is required")
    private TravelClass travelClass;

    @NotNull(message = "Seats booked is required")
    @Min(value = 1, message = "At least one seat must be booked")
    private Integer seatsBooked;

    private Boolean bookingForSelf = true;

    @Size(max = 100, message = "Passenger name must be at most 100 characters")
    private String passengerName;

    @Email(message = "Passenger email must be valid")
    @Size(max = 100, message = "Passenger email must be at most 100 characters")
    private String passengerEmail;

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
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

    public Boolean getBookingForSelf() {
        return bookingForSelf;
    }

    public void setBookingForSelf(Boolean bookingForSelf) {
        this.bookingForSelf = bookingForSelf;
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
}
