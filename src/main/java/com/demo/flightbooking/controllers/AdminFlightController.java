package com.demo.flightbooking.controllers;

public class AdminFlightController {
    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<Flight> createFlight(
            @RequestBody FlightDto flightDto) {

        return ResponseEntity.ok(
                flightService.createFlight(flightDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(
            @PathVariable Long id,
            @RequestBody FlightDto flightDto) {

        return ResponseEntity.ok(
                flightService.updateFlight(id, flightDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(
            @PathVariable Long id) {

        flightService.deleteFlight(id);

        return ResponseEntity.noContent().build();
    }
}
