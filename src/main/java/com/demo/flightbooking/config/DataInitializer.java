package com.demo.flightbooking.config;

import com.demo.flightbooking.enums.Role;
import com.demo.flightbooking.models.Flight;
import com.demo.flightbooking.models.User;
import com.demo.flightbooking.repository.AdminFlightRepository;
import com.demo.flightbooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initFlights(
            AdminFlightRepository flightRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepository.existsByEmail("admin@flight.com")) {
                User admin = User.builder()
                        .name("Admin")
                        .email("admin@flight.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(admin);
                System.out.println("DataInitializer: Default admin user created.");
            }

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
            f1.setEconomyPrice(4500.00);
            f1.setBusinessPrice(7800.00);
            f1.setFirstClassPrice(12000.00);
            f1.setEconomySeatsAvailable(120);
            f1.setBusinessSeatsAvailable(24);
            f1.setFirstClassSeatsAvailable(8);
            f1.setStatus("SCHEDULED");

            Flight f2 = new Flight();
            f2.setFlightNumber("6E-202");
            f2.setAirline("IndiGo");
            f2.setSource("Mumbai");
            f2.setDestination("Bangalore");
            f2.setDepartureTime(LocalDateTime.of(2025, 8, 10, 9, 30));
            f2.setArrivalTime(LocalDateTime.of(2025, 8, 10, 11, 0));
            f2.setPrice(3200.00);
            f2.setEconomyPrice(3200.00);
            f2.setBusinessPrice(6200.00);
            f2.setFirstClassPrice(9500.00);
            f2.setEconomySeatsAvailable(132);
            f2.setBusinessSeatsAvailable(18);
            f2.setFirstClassSeatsAvailable(6);
            f2.setStatus("SCHEDULED");

            Flight f3 = new Flight();
            f3.setFlightNumber("SG-303");
            f3.setAirline("SpiceJet");
            f3.setSource("Bangalore");
            f3.setDestination("Chennai");
            f3.setDepartureTime(LocalDateTime.of(2025, 8, 11, 7, 45));
            f3.setArrivalTime(LocalDateTime.of(2025, 8, 11, 9, 0));
            f3.setPrice(2800.00);
            f3.setEconomyPrice(2800.00);
            f3.setBusinessPrice(5400.00);
            f3.setFirstClassPrice(8200.00);
            f3.setEconomySeatsAvailable(110);
            f3.setBusinessSeatsAvailable(16);
            f3.setFirstClassSeatsAvailable(4);
            f3.setStatus("SCHEDULED");

            Flight f4 = new Flight();
            f4.setFlightNumber("UK-404");
            f4.setAirline("Vistara");
            f4.setSource("Delhi");
            f4.setDestination("Kolkata");
            f4.setDepartureTime(LocalDateTime.of(2025, 8, 12, 14, 0));
            f4.setArrivalTime(LocalDateTime.of(2025, 8, 12, 16, 30));
            f4.setPrice(5100.00);
            f4.setEconomyPrice(5100.00);
            f4.setBusinessPrice(8800.00);
            f4.setFirstClassPrice(13600.00);
            f4.setEconomySeatsAvailable(126);
            f4.setBusinessSeatsAvailable(22);
            f4.setFirstClassSeatsAvailable(8);
            f4.setStatus("SCHEDULED");

            Flight f5 = new Flight();
            f5.setFlightNumber("G8-505");
            f5.setAirline("GoFirst");
            f5.setSource("Hyderabad");
            f5.setDestination("Delhi");
            f5.setDepartureTime(LocalDateTime.of(2025, 8, 13, 11, 15));
            f5.setArrivalTime(LocalDateTime.of(2025, 8, 13, 13, 30));
            f5.setPrice(3900.00);
            f5.setEconomyPrice(3900.00);
            f5.setBusinessPrice(6900.00);
            f5.setFirstClassPrice(10400.00);
            f5.setEconomySeatsAvailable(118);
            f5.setBusinessSeatsAvailable(20);
            f5.setFirstClassSeatsAvailable(6);
            f5.setStatus("SCHEDULED");

            Flight f6 = new Flight();
            f6.setFlightNumber("EK-601");
            f6.setAirline("Emirates");
            f6.setSource("Mumbai");
            f6.setDestination("Dubai");
            f6.setDepartureTime(LocalDateTime.of(2025, 8, 15, 2, 30));
            f6.setArrivalTime(LocalDateTime.of(2025, 8, 15, 5, 0));
            f6.setPrice(18500.00);
            f6.setEconomyPrice(18500.00);
            f6.setBusinessPrice(32500.00);
            f6.setFirstClassPrice(52500.00);
            f6.setEconomySeatsAvailable(180);
            f6.setBusinessSeatsAvailable(32);
            f6.setFirstClassSeatsAvailable(12);
            f6.setStatus("SCHEDULED");

            Flight f7 = new Flight();
            f7.setFlightNumber("QR-702");
            f7.setAirline("Qatar Airways");
            f7.setSource("Delhi");
            f7.setDestination("London");
            f7.setDepartureTime(LocalDateTime.of(2025, 8, 16, 22, 0));
            f7.setArrivalTime(LocalDateTime.of(2025, 8, 17, 6, 30));
            f7.setPrice(42000.00);
            f7.setEconomyPrice(42000.00);
            f7.setBusinessPrice(79000.00);
            f7.setFirstClassPrice(122000.00);
            f7.setEconomySeatsAvailable(210);
            f7.setBusinessSeatsAvailable(40);
            f7.setFirstClassSeatsAvailable(10);
            f7.setStatus("SCHEDULED");

            Flight f8 = new Flight();
            f8.setFlightNumber("AI-888");
            f8.setAirline("Air India");
            f8.setSource("Chennai");
            f8.setDestination("Delhi");
            f8.setDepartureTime(LocalDateTime.of(2025, 8, 10, 18, 0));
            f8.setArrivalTime(LocalDateTime.of(2025, 8, 10, 20, 30));
            f8.setPrice(4000.00);
            f8.setEconomyPrice(4000.00);
            f8.setBusinessPrice(7100.00);
            f8.setFirstClassPrice(10900.00);
            f8.setEconomySeatsAvailable(0);
            f8.setBusinessSeatsAvailable(0);
            f8.setFirstClassSeatsAvailable(0);
            f8.setStatus("CANCELLED");

            Flight f9 = new Flight();
            f9.setFlightNumber("6E-999");
            f9.setAirline("IndiGo");
            f9.setSource("Kolkata");
            f9.setDestination("Mumbai");
            f9.setDepartureTime(LocalDateTime.of(2025, 8, 11, 15, 0));
            f9.setArrivalTime(LocalDateTime.of(2025, 8, 11, 17, 45));
            f9.setPrice(3600.00);
            f9.setEconomyPrice(3600.00);
            f9.setBusinessPrice(6500.00);
            f9.setFirstClassPrice(9800.00);
            f9.setEconomySeatsAvailable(98);
            f9.setBusinessSeatsAvailable(14);
            f9.setFirstClassSeatsAvailable(4);
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
