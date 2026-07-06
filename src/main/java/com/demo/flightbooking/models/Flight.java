package com.demo.flightbooking.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String flightNumber;

    private String airline;

    private String source;

    private String destination;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private Double price;

    private Double economyPrice;

    private Double businessPrice;

    private Double firstClassPrice;

    private Integer economySeatsAvailable;

    private Integer businessSeatsAvailable;

    private Integer firstClassSeatsAvailable;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(Double economyPrice) {
        this.economyPrice = economyPrice;
    }

    public Double getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(Double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public Double getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(Double firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public Integer getEconomySeatsAvailable() {
        return economySeatsAvailable;
    }

    public void setEconomySeatsAvailable(Integer economySeatsAvailable) {
        this.economySeatsAvailable = economySeatsAvailable;
    }

    public Integer getBusinessSeatsAvailable() {
        return businessSeatsAvailable;
    }

    public void setBusinessSeatsAvailable(Integer businessSeatsAvailable) {
        this.businessSeatsAvailable = businessSeatsAvailable;
    }

    public Integer getFirstClassSeatsAvailable() {
        return firstClassSeatsAvailable;
    }

    public void setFirstClassSeatsAvailable(Integer firstClassSeatsAvailable) {
        this.firstClassSeatsAvailable = firstClassSeatsAvailable;
    }

    @Transient
    public Integer getSeatsAvailable() {
        int economy = economySeatsAvailable != null ? economySeatsAvailable : 0;
        int business = businessSeatsAvailable != null ? businessSeatsAvailable : 0;
        int firstClass = firstClassSeatsAvailable != null ? firstClassSeatsAvailable : 0;
        return economy + business + firstClass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
