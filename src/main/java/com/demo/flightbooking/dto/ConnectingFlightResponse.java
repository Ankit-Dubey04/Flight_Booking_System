package com.demo.flightbooking.dto;

import com.demo.flightbooking.models.Flight;

public class ConnectingFlightResponse {
    private Flight firstFlight;
    private Flight secondFlight;
    private String connectionCity;
    private Long layoverMinutes;
    private Long totalTravelMinutes;
    private Double totalEconomyPrice;
    private Double totalBusinessPrice;
    private Double totalFirstClassPrice;

    public ConnectingFlightResponse(
            Flight firstFlight,
            Flight secondFlight,
            String connectionCity,
            Long layoverMinutes,
            Long totalTravelMinutes,
            Double totalEconomyPrice,
            Double totalBusinessPrice,
            Double totalFirstClassPrice
    ) {
        this.firstFlight = firstFlight;
        this.secondFlight = secondFlight;
        this.connectionCity = connectionCity;
        this.layoverMinutes = layoverMinutes;
        this.totalTravelMinutes = totalTravelMinutes;
        this.totalEconomyPrice = totalEconomyPrice;
        this.totalBusinessPrice = totalBusinessPrice;
        this.totalFirstClassPrice = totalFirstClassPrice;
    }

    public Flight getFirstFlight() {
        return firstFlight;
    }

    public void setFirstFlight(Flight firstFlight) {
        this.firstFlight = firstFlight;
    }

    public Flight getSecondFlight() {
        return secondFlight;
    }

    public void setSecondFlight(Flight secondFlight) {
        this.secondFlight = secondFlight;
    }

    public String getConnectionCity() {
        return connectionCity;
    }

    public void setConnectionCity(String connectionCity) {
        this.connectionCity = connectionCity;
    }

    public Long getLayoverMinutes() {
        return layoverMinutes;
    }

    public void setLayoverMinutes(Long layoverMinutes) {
        this.layoverMinutes = layoverMinutes;
    }

    public Long getTotalTravelMinutes() {
        return totalTravelMinutes;
    }

    public void setTotalTravelMinutes(Long totalTravelMinutes) {
        this.totalTravelMinutes = totalTravelMinutes;
    }

    public Double getTotalEconomyPrice() {
        return totalEconomyPrice;
    }

    public void setTotalEconomyPrice(Double totalEconomyPrice) {
        this.totalEconomyPrice = totalEconomyPrice;
    }

    public Double getTotalBusinessPrice() {
        return totalBusinessPrice;
    }

    public void setTotalBusinessPrice(Double totalBusinessPrice) {
        this.totalBusinessPrice = totalBusinessPrice;
    }

    public Double getTotalFirstClassPrice() {
        return totalFirstClassPrice;
    }

    public void setTotalFirstClassPrice(Double totalFirstClassPrice) {
        this.totalFirstClassPrice = totalFirstClassPrice;
    }
}
