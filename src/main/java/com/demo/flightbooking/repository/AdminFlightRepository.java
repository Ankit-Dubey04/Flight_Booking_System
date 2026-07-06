package com.demo.flightbooking.repository;

import com.demo.flightbooking.models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminFlightRepository extends JpaRepository<Flight, Long> {
//    Optional<Flight> findByFlightNumber(String flightNumber);
    @Query("""
            select f from Flight f
            where (:source is null or lower(f.source) = lower(:source))
              and (:destination is null or lower(f.destination) = lower(:destination))
              and (:startOfDay is null or f.departureTime >= :startOfDay)
              and (:endOfDay is null or f.departureTime < :endOfDay)
            order by f.departureTime asc
            """)
    List<Flight> searchFlights(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
