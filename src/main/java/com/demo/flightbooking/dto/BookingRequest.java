package com.demo.flightbooking.dto;

import com.demo.flightbooking.enums.TravelClass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class BookingRequest {

    @NotNull(message = "Flight id is required")
    private Long flightId;

    private Long returnFlightId;

    @NotNull(message = "Travel class is required")
    private TravelClass travelClass;

    @NotNull(message = "Seats booked is required")
    @Min(value = 1, message = "At least one seat must be booked")
    private Integer seatsBooked;

    private List<@Size(max = 10, message = "Seat number must be at most 10 characters") String> selectedSeatNumbers;

    private List<@Size(max = 10, message = "Return seat number must be at most 10 characters") String> returnSelectedSeatNumbers;

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

    public Long getReturnFlightId() {
        return returnFlightId;
    }

    public void setReturnFlightId(Long returnFlightId) {
        this.returnFlightId = returnFlightId;
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

    public List<String> getSelectedSeatNumbers() {
        return selectedSeatNumbers;
    }

    public void setSelectedSeatNumbers(List<String> selectedSeatNumbers) {
        this.selectedSeatNumbers = selectedSeatNumbers;
    }

    public List<String> getReturnSelectedSeatNumbers() {
        return returnSelectedSeatNumbers;
    }

    public void setReturnSelectedSeatNumbers(List<String> returnSelectedSeatNumbers) {
        this.returnSelectedSeatNumbers = returnSelectedSeatNumbers;
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
