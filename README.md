# Flight Booking System

A Spring Boot REST API with a React/Vite frontend for a real flight-booking workflow.

## React Pages

- Home: branded SkyPort landing/search screen.
- Flights: route/date search with flight cards, fares, seats, discounts, and booking links.
- Booking: cabin, seat, passenger, one-way/return-flight booking form with fare summary.
- My Trips: confirmed bookings, ticket download, and cancellation.
- Saved Passengers: passengers saved from previous bookings for other people.
- Admin Flights: create, edit, and delete flight schedules, fares, seats, and statuses.
- Login/Register: JWT authentication screens.

## API Connection

The React app calls the Spring Boot API from `frontend/src/main.jsx`.

- `API_BASE_URL` reads `VITE_API_BASE_URL`, defaulting to `http://localhost:8080`.
- `apiRequest()` wraps `fetch`, sends JSON, and attaches `Authorization: Bearer <token>` when the user is logged in.
- Login stores the JWT plus user profile/role in `localStorage`.
- Protected pages use that token for `/api/bookings/**` and `/api/admin/flights/**`.
- Public flight browsing uses `GET /api/flights` and `GET /api/flights/search`.
- Spring Security enables CORS for `http://localhost:5173` in `SecurityConfig`.

## Default Admin

```text
admin@flight.com
admin123
```

## Run Together

Make sure MySQL is running and `src/main/resources/application.properties` matches your database credentials.

One command from the project root:

```powershell
.\run-dev.ps1
```

Then open:

```text
http://localhost:5173
```

## Run Manually

Terminal 1:

```powershell
.\mvnw.cmd spring-boot:run
```

Terminal 2:

```powershell
cd frontend
npm install
npm run dev
```

Backend: `http://localhost:8080`

Frontend: `http://localhost:5173`

If your backend uses another port, create `frontend/.env`:

```text
VITE_API_BASE_URL=http://localhost:8080
```
