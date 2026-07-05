package com.demo.flightbooking.config;

import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.repository.AdminFlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initFlights(AdminFlightRepository flightRepository) {
        return args -> {
            if (flightRepository.count() != 0) {
                System.out.println("DataInitializer: Flights table already has data, skipping seed.");
                return;
            }

            Flight f1 = new Flight();
            f1.setFlightNumber("AI-101");
            f1.setAirline("Air India");
            f1.setSource("Delhi");
            f1.setDestination("Mumbai");
            f1.setDepartureTime(LocalDateTime.of(2025, 8, 10, 6, 0));
            f1.setArrivalTime(LocalDateTime.of(2025, 8, 10, 8, 15));
            f1.setPrice(4500.00);
            f1.setStatus("SCHEDULED");

            Flight f2 = new Flight();
            f2.setFlightNumber("6E-202");
            f2.setAirline("IndiGo");
            f2.setSource("Mumbai");
            f2.setDestination("Bangalore");
            f2.setDepartureTime(LocalDateTime.of(2025, 8, 10, 9, 30));
            f2.setArrivalTime(LocalDateTime.of(2025, 8, 10, 11, 0));
            f2.setPrice(3200.00);
            f2.setStatus("SCHEDULED");

            Flight f3 = new Flight();
            f3.setFlightNumber("SG-303");
            f3.setAirline("SpiceJet");
            f3.setSource("Bangalore");
            f3.setDestination("Chennai");
            f3.setDepartureTime(LocalDateTime.of(2025, 8, 11, 7, 45));
            f3.setArrivalTime(LocalDateTime.of(2025, 8, 11, 9, 0));
            f3.setPrice(2800.00);
            f3.setStatus("SCHEDULED");

            Flight f4 = new Flight();
            f4.setFlightNumber("UK-404");
            f4.setAirline("Vistara");
            f4.setSource("Delhi");
            f4.setDestination("Kolkata");
            f4.setDepartureTime(LocalDateTime.of(2025, 8, 12, 14, 0));
            f4.setArrivalTime(LocalDateTime.of(2025, 8, 12, 16, 30));
            f4.setPrice(5100.00);
            f4.setStatus("SCHEDULED");

            Flight f5 = new Flight();
            f5.setFlightNumber("G8-505");
            f5.setAirline("GoFirst");
            f5.setSource("Hyderabad");
            f5.setDestination("Delhi");
            f5.setDepartureTime(LocalDateTime.of(2025, 8, 13, 11, 15));
            f5.setArrivalTime(LocalDateTime.of(2025, 8, 13, 13, 30));
            f5.setPrice(3900.00);
            f5.setStatus("SCHEDULED");

            Flight f6 = new Flight();
            f6.setFlightNumber("EK-601");
            f6.setAirline("Emirates");
            f6.setSource("Mumbai");
            f6.setDestination("Dubai");
            f6.setDepartureTime(LocalDateTime.of(2025, 8, 15, 2, 30));
            f6.setArrivalTime(LocalDateTime.of(2025, 8, 15, 5, 0));
            f6.setPrice(18500.00);
            f6.setStatus("SCHEDULED");

            Flight f7 = new Flight();
            f7.setFlightNumber("QR-702");
            f7.setAirline("Qatar Airways");
            f7.setSource("Delhi");
            f7.setDestination("London");
            f7.setDepartureTime(LocalDateTime.of(2025, 8, 16, 22, 0));
            f7.setArrivalTime(LocalDateTime.of(2025, 8, 17, 6, 30));
            f7.setPrice(42000.00);
            f7.setStatus("SCHEDULED");

            Flight f8 = new Flight();
            f8.setFlightNumber("AI-888");
            f8.setAirline("Air India");
            f8.setSource("Chennai");
            f8.setDestination("Delhi");
            f8.setDepartureTime(LocalDateTime.of(2025, 8, 10, 18, 0));
            f8.setArrivalTime(LocalDateTime.of(2025, 8, 10, 20, 30));
            f8.setPrice(4000.00);
            f8.setStatus("CANCELLED");

            Flight f9 = new Flight();
            f9.setFlightNumber("6E-999");
            f9.setAirline("IndiGo");
            f9.setSource("Kolkata");
            f9.setDestination("Mumbai");
            f9.setDepartureTime(LocalDateTime.of(2025, 8, 11, 15, 0));
            f9.setArrivalTime(LocalDateTime.of(2025, 8, 11, 17, 45));
            f9.setPrice(3600.00);
            f9.setStatus("DELAYED");

            flightRepository.save(f1);
            flightRepository.save(f2);
            flightRepository.save(f3);
            flightRepository.save(f4);
            flightRepository.save(f5);
            flightRepository.save(f6);
            flightRepository.save(f7);
            flightRepository.save(f8);
            flightRepository.save(f9);

            System.out.println("DataInitializer: 9 sample flights loaded into the database.");
        };
    }
}
