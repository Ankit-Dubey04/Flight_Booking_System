package com.demo.flightbooking.repository;

public class FlightRepository extends JpaRepository<Flight,Long>{
    Optional<Flight> findByFlightNumber(String flightNumber);
}
