# Flight Booking System

A Spring Boot REST API for searching flights, managing flight inventory, and booking tickets with JWT-based authentication.

## Features

- User registration and login with JWT authentication
- Role-based access control for admin flight management
- Public flight search for authenticated users
- Direct and connecting flight search
- One-way and round-trip booking support
- Seat selection by travel class
- Ticket download as a text file
- Booking cancellation
- Saved passenger profile lookup
- Initial seed data for admin login and sample flights

## Tech Stack

- Java 17
- Spring Boot 3.3.2
- Spring Web
- Spring Security
- Spring Data JPA
- MySQL
- H2 runtime dependency
- JWT using `jjwt`
- Maven
- Lombok

## Project Structure

```text
src/main/java/com/demo/flightbooking
|-- config          # Seed data configuration
|-- controllers     # REST controllers
|-- dto             # Request and response DTOs
|-- enums           # Role, booking status, travel class
|-- exceptions      # Custom exceptions and global handling
|-- models          # JPA entities
|-- repository      # Spring Data repositories
|-- security        # JWT and Spring Security configuration
`-- service         # Business logic
```

## Prerequisites

- JDK 17 or later
- MySQL server
- Maven, or use the included Maven wrapper

## Database Setup

Create a MySQL database:

```sql
CREATE DATABASE flight_db;
```

Update `src/main/resources/application.properties` with your local database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flight_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

The application uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Tables are created or updated automatically when the application starts.

## Run the Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

## Seed Data

On first startup, the application creates:

- One default admin user
- Nine sample flights

Default admin login:

```text
Email: admin@flight.com
Password: admin123
```

## Authentication

Register or log in to receive a JWT token. Send the token with protected requests:

```http
Authorization: Bearer <jwt-token>
```

## API Endpoints

### Auth

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |

### User Flights

All endpoints below require authentication.

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/flights` | Get all flights |
| GET | `/api/flights/{id}` | Get flight by ID |
| GET | `/api/flights/search?source=Delhi&destination=Mumbai&departureDate=2025-08-10` | Search direct flights |
| GET | `/api/flights/connecting?source=Delhi&destination=Bangalore&departureDate=2025-08-10` | Search connecting flights |

### Bookings

All endpoints below require authentication.

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/bookings` | Create a booking |
| GET | `/api/bookings/my` | Get current user's bookings |
| GET | `/api/bookings/ticket/{ticketNumber}` | Get booking by ticket number |
| GET | `/api/bookings/ticket/{ticketNumber}/download` | Download ticket as text file |
| PATCH | `/api/bookings/ticket/{ticketNumber}/cancel` | Cancel booking |
| GET | `/api/bookings/saved-passengers` | Get saved passenger profiles |

### Admin Flights

All endpoints below require an admin token.

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/admin/flights` | Get all flights |
| GET | `/api/admin/flights/{id}` | Get flight by ID |
| POST | `/api/admin/flights` | Create a flight |
| PUT | `/api/admin/flights/{id}` | Update a flight |
| DELETE | `/api/admin/flights/{id}` | Delete a flight |

## Example Requests

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Demo User",
    "email": "demo@example.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@flight.com",
    "password": "admin123"
  }'
```

### Create a Booking

```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "flightId": 1,
    "travelClass": "ECONOMY",
    "seatsBooked": 2,
    "selectedSeatNumbers": ["12A", "12B"],
    "bookingForSelf": true
  }'
```

### Create a Flight as Admin

```bash
curl -X POST http://localhost:8080/api/admin/flights \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin-jwt-token>" \
  -d '{
    "flightNumber": "AI-777",
    "airline": "Air India",
    "source": "Delhi",
    "destination": "Mumbai",
    "departureTime": "2025-09-01T10:00:00",
    "arrivalTime": "2025-09-01T12:15:00",
    "price": 5000.0,
    "economyPrice": 5000.0,
    "businessPrice": 9000.0,
    "firstClassPrice": 15000.0,
    "economySeatsAvailable": 120,
    "businessSeatsAvailable": 20,
    "firstClassSeatsAvailable": 8,
    "discountPercentage": 10.0,
    "status": "SCHEDULED"
  }'
```

## Travel Classes

Valid booking values:

```text
ECONOMY
BUSINESS
FIRST_CLASS
```

## Testing

Run tests with:

```powershell
.\mvnw.cmd test
```

On macOS or Linux:

```bash
./mvnw test
```

## Build

Create a packaged application:

```powershell
.\mvnw.cmd clean package
```

The generated JAR will be available in the `target` directory.
