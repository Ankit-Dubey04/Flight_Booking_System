package com.demo.flightbooking.dto;

import java.time.LocalDateTime;

public class PassengerProfileResponse {
    private String passengerName;
    private String passengerEmail;
    private LocalDateTime lastUsedAt;

    public PassengerProfileResponse(String passengerName, String passengerEmail, LocalDateTime lastUsedAt) {
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.lastUsedAt = lastUsedAt;
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

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }
}
