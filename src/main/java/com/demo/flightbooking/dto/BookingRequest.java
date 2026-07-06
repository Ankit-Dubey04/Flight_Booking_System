package com.demo.flightbooking.dto;

import com.demo.flightbooking.enums.TravelClass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingRequest {

    @NotNull(message = "Flight id is required")
    private Long flightId;

    @NotNull(message = "Travel class is required")
    private TravelClass travelClass;

    @NotNull(message = "Seats booked is required")
    @Min(value = 1, message = "At least one seat must be booked")
    private Integer seatsBooked;

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
}
