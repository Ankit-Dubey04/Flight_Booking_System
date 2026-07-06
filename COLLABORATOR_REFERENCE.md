# Flight Booking System Reference

This file is a handoff reference for collaborators and coding agents working in this repository.

## Project Summary

- Stack: Spring Boot 3.3.2, Spring Security, Spring Data JPA, JWT auth
- Language: Java 17 target
- Package root: `com.demo.flightbooking`
- Primary domain areas:
  - authentication and JWT login
  - admin flight management
  - user flight browsing
  - booking and ticket creation
  - one-way and round-trip booking
  - text ticket download
  - booking cancellation
  - class-wise pricing, seats, and discounts

## Important Conventions

- Keep all application code under `com.demo.flightbooking`
- Security is JWT-based and stateless
- Admin-only flight management lives under `/api/admin/...`
- Normal authenticated user read endpoints live under `/api/...`
- New registered users are created with role `USER`
- A default admin user is seeded on first run
- Flight seat inventory is tracked separately for:
  - `ECONOMY`
  - `BUSINESS`
  - `FIRST_CLASS`
- Booking price is based on selected class price, then reduced by the flight discount percentage
- Round-trip booking is represented by one booking row with an optional return flight

## Key Files

### App Entry

- [FlightbookingApplication.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/FlightbookingApplication.java:1)

### Security

- [SecurityConfig.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/security/SecurityConfig.java:1)
- [JwtService.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/security/JwtService.java:1)
- [JwtAuthenticationFilter.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/security/JwtAuthenticationFilter.java:1)
- [JwtAuthenticationEntryPoint.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/security/JwtAuthenticationEntryPoint.java:1)
- [CustomUserDetailsService.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/CustomUserDetailsService.java:1)

### Auth

- [AuthController.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/controllers/AuthController.java:1)
- [AuthService.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/AuthService.java:1)
- [User.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/models/User.java:1)
- [UserRepository.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/repository/UserRepository.java:1)

### Flights

- [Flight.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/models/Flight.java:1)
- [FlightDto.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/dto/FlightDto.java:1)
- [AdminFlightController.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/controllers/AdminFlightController.java:1)
- [UserFlightController.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/controllers/UserFlightController.java:1)
- [AdminFlightService.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/AdminFlightService.java:1)
- [AdminFlightServiceImpl.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/AdminFlightServiceImpl.java:1)
- [AdminFlightRepository.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/repository/AdminFlightRepository.java:1)

### Bookings and Tickets

- [Booking.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/models/Booking.java:1)
- [BookingRequest.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/dto/BookingRequest.java:1)
- [BookingResponse.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/dto/BookingResponse.java:1)
- [BookingController.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/controllers/BookingController.java:1)
- [BookingService.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/BookingService.java:1)
- [BookingServiceImpl.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/BookingServiceImpl.java:1)
- [BookingRepository.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/repository/BookingRepository.java:1)

### Seeding and Config

- [DataInitializer.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/config/DataInitializer.java:1)
- [application.properties](D:/java flight/Flight_Booking_System/src/main/resources/application.properties:1)
- [pom.xml](D:/java flight/Flight_Booking_System/pom.xml:1)
- [mvnw.cmd](D:/java flight/Flight_Booking_System/mvnw.cmd:1)

### Exceptions

- [GlobalExceptionController.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/exceptions/GlobalExceptionController.java:1)
- [ResourceNotFoundException.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/exception/ResourceNotFoundException.java:1)
- [UserNotFoundException.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/exceptions/UserNotFoundException.java:1)
- [DuplicateEmailException.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/exceptions/DuplicateEmailException.java:1)
- [BookingNotFoundException.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/exceptions/BookingNotFoundException.java:1)
- [InsufficientSeatsException.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/exceptions/InsufficientSeatsException.java:1)

## Current Data Model

### User

Main fields:
- `id`
- `name`
- `email`
- `password`
- `role`
- `createdAt`

Roles:
- `USER`
- `ADMIN`

### Flight

Main fields:
- `id`
- `flightNumber`
- `airline`
- `source`
- `destination`
- `departureTime`
- `arrivalTime`
- `price`
- `economyPrice`
- `businessPrice`
- `firstClassPrice`
- `economySeatsAvailable`
- `businessSeatsAvailable`
- `firstClassSeatsAvailable`
- `discountPercentage`
- `status`

Derived field:
- `getSeatsAvailable()` sums class-wise seat counts

### Booking

Main fields:
- `id`
- `ticketNumber`
- `user`
- `flight`
- `returnFlight`
- `travelClass`
- `seatsBooked`
- `pricePerSeat`
- `returnPricePerSeat`
- `discountPercentage`
- `returnDiscountPercentage`
- `outboundTotalPrice`
- `returnTotalPrice`
- `totalPrice`
- `status`
- `passengerName`
- `passengerEmail`
- `bookedAt`

Enums:
- `BookingStatus`: `CONFIRMED`, `CANCELLED`
- `TravelClass`: `ECONOMY`, `BUSINESS`, `FIRST_CLASS`

## Current API Surface

### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`

### Admin Flight Management

- `GET /api/admin/flights`
- `GET /api/admin/flights/{id}`
- `POST /api/admin/flights`
- `PUT /api/admin/flights/{id}`
- `DELETE /api/admin/flights/{id}`

Requires `ADMIN` role.

### User Flight Browsing

- `GET /api/flights`
- `GET /api/flights/{id}`

Requires authentication.

### Booking and Tickets

- `POST /api/bookings`
- `GET /api/bookings/my`
- `GET /api/bookings/ticket/{ticketNumber}`
- `GET /api/bookings/ticket/{ticketNumber}/download`
- `PATCH /api/bookings/ticket/{ticketNumber}/cancel`

Requires authentication.

Round-trip request behavior:
- one-way booking uses `flightId`
- round-trip booking uses `flightId` plus `returnFlightId`
- `returnFlightId` must refer to a different flight whose route is the reverse of the outbound route
- the return flight must depart after the outbound flight arrives when both timestamps are present
- the ticket download endpoint returns a plain text `.txt` attachment for the authenticated user's owned ticket

## Security Behavior

- JWT auth is enabled for all routes except:
  - `/api/auth/register`
  - `/api/auth/login`
  - `/h2-console/**`
- Admin routes use method-level authorization with `@PreAuthorize("hasRole('ADMIN')")`
- Regular users can browse flights and manage their own bookings only

## Seed Behavior

`DataInitializer` currently does two things:

- ensures a default admin user exists:
  - email: `admin@flight.com`
  - password: `admin123`
  - role: `ADMIN`
- inserts sample flights only if the flight table is empty

Important note:
- the admin user check is idempotent
- the flight seeding is only on first empty flight table

## Booking Logic

Implemented in `BookingServiceImpl`.

Current behavior:
- booking uses authenticated user from `SecurityContextHolder`
- passenger name and email are copied from logged-in user
- seat inventory is reduced from the selected class on the outbound flight
- for round trips, seat inventory is also reduced from the selected class on the return flight
- booking price per seat is based on class price:
  - economy uses `economyPrice`, fallback `price`
  - business uses `businessPrice`, fallback `price`
  - first class uses `firstClassPrice`, fallback `price`
- outbound discount is applied from `flight.discountPercentage`
- return discount is applied from `returnFlight.discountPercentage` when `returnFlightId` is provided
- total price is outbound leg total plus return leg total when present
- cancellation restores seats to the original class on both outbound and return flights
- cancelled bookings remain stored and are marked `CANCELLED`
- ticket download is generated in `BookingServiceImpl` as plain text and served by `BookingController`

## Known Assumptions

- one booking currently represents one user-owned ticket record, including both legs for a round trip
- no separate passenger list exists yet
- no payment integration exists
- no booking history filter exists beyond "my bookings"
- no admin booking management exists yet
- flight status is a plain string, not an enum
- discount is flight-wide, not per-class
- downloaded tickets are plain text, not PDF

## Good Extension Points

If adding features, these are the safest places to extend:

- booking rules and pricing:
  - [BookingServiceImpl.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/BookingServiceImpl.java:1)
- admin flight creation/update payload:
  - [FlightDto.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/dto/FlightDto.java:1)
  - [AdminFlightServiceImpl.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/AdminFlightServiceImpl.java:1)
- new user-facing booking endpoints:
  - [BookingController.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/controllers/BookingController.java:1)
- auth or user-role behavior:
  - [AuthService.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/service/AuthService.java:1)
  - [SecurityConfig.java](D:/java flight/Flight_Booking_System/src/main/java/com/demo/flightbooking/security/SecurityConfig.java:1)

## High-Value Next Features

These should be straightforward next additions:

- booking cancellation policy and refund amount
- per-class discounts instead of flight-wide discount
- multi-passenger booking
- seat selection and seat numbers
- payment status and payment transaction records
- admin booking list and booking search
- user profile and booking history filters
- flight search by route/date/status
- flight status enum instead of free-form string
- ticket PDF generation or richer printable ticket formatting

## Notes For Another Codex Agent

If you are continuing work in this repo:

- do not reintroduce mixed package names like `com.flightbooking`
- preserve the existing JWT flow unless intentionally refactoring auth
- use the current service layer instead of duplicating business logic inside controllers
- when adding booking features, update seat restoration and price calculations together
- prefer modifying `GlobalExceptionController` when adding business exceptions
- if you change startup seed behavior, keep it idempotent
- if testing fails locally at first, Maven may need network access to resolve dependencies before `.\mvnw.cmd test -q` can run

## Suggested Prompt To Give Another Codex

You can paste this along with the repository:

`Read COLLABORATOR_REFERENCE.md first, then inspect the referenced files before making changes. Preserve the current JWT auth flow, package structure under com.demo.flightbooking, admin/user route split, and booking seat accounting rules.`
