package com.demo.flightbooking.controllers;

import com.demo.flightbooking.dto.FlightDto;
import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.service.AdminFlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flights")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFlightController {
    private final AdminFlightService flightService;

    public AdminFlightController(AdminFlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

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
