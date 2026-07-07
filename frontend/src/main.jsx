import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import { Link, Navigate, NavLink, Route, BrowserRouter as Router, Routes, useLocation, useNavigate, useParams } from "react-router-dom";
import {
  ArrowRight,
  BadgeIndianRupee,
  BriefcaseBusiness,
  CheckCircle2,
  CircleUserRound,
  Download,
  LogOut,
  Plane,
  Plus,
  Search,
  ShieldCheck,
  Ticket,
  Trash2,
  Users,
  XCircle
} from "lucide-react";
import "./styles.css";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";
const travelClasses = ["ECONOMY", "BUSINESS", "FIRST_CLASS"];
const blankFlight = {
  flightNumber: "",
  airline: "",
  source: "",
  destination: "",
  departureTime: "",
  arrivalTime: "",
  price: "",
  economyPrice: "",
  businessPrice: "",
  firstClassPrice: "",
  economySeatsAvailable: "",
  businessSeatsAvailable: "",
  firstClassSeatsAvailable: "",
  discountPercentage: "",
  status: "SCHEDULED"
};

const AuthContext = createContext(null);

function App() {
  const [token, setToken] = useState(() => localStorage.getItem("flight_token") || "");
  const [email, setEmail] = useState(() => localStorage.getItem("flight_email") || "");
  const [name, setName] = useState(() => localStorage.getItem("flight_name") || "");
  const [role, setRole] = useState(() => localStorage.getItem("flight_role") || "");

  const auth = useMemo(() => ({
    token,
    email,
    name,
    role,
    isAuthenticated: Boolean(token),
    isAdmin: role === "ADMIN" || email.toLowerCase() === "admin@flight.com",
    login(nextToken, nextEmail, nextName = "", nextRole = "") {
      localStorage.setItem("flight_token", nextToken);
      localStorage.setItem("flight_email", nextEmail);
      localStorage.setItem("flight_name", nextName);
      localStorage.setItem("flight_role", nextRole);
      setToken(nextToken);
      setEmail(nextEmail);
      setName(nextName);
      setRole(nextRole);
    },
    logout() {
      localStorage.removeItem("flight_token");
      localStorage.removeItem("flight_email");
      localStorage.removeItem("flight_name");
      localStorage.removeItem("flight_role");
      setToken("");
      setEmail("");
      setName("");
      setRole("");
    }
  }), [token, email, name, role]);

  return (
    <AuthContext.Provider value={auth}>
      <Router>
        <Shell>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/flights" element={<FlightsPage />} />
            <Route path="/book/:flightId" element={<Protected><BookingPage /></Protected>} />
            <Route path="/bookings" element={<Protected><MyBookingsPage /></Protected>} />
            <Route path="/passengers" element={<Protected><SavedPassengersPage /></Protected>} />
            <Route path="/admin" element={<Protected adminOnly><AdminFlightsPage /></Protected>} />
            <Route path="/login" element={<AuthPage mode="login" />} />
            <Route path="/register" element={<AuthPage mode="register" />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Shell>
      </Router>
    </AuthContext.Provider>
  );
}

function useAuth() {
  return useContext(AuthContext);
}

async function apiRequest(path, options = {}, token = "") {
  const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
  if (token) headers.Authorization = `Bearer ${token}`;
  const response = await fetch(`${API_BASE_URL}${path}`, { ...options, headers });
  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Request failed with status ${response.status}`);
  }
  if (response.status === 204) return null;
  const contentType = response.headers.get("content-type") || "";
  return contentType.includes("application/json") ? response.json() : response.text();
}

function Shell({ children }) {
  const auth = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const navItems = [
    ["/flights", "Flights"],
    ["/bookings", "Trips"],
    ["/passengers", "Passengers"]
  ];
  if (auth.isAdmin) navItems.push(["/admin", "Admin"]);

  return (
    <div className="app-shell">
      <header className="topbar">
        <Link className="brand" to="/">
          <span className="brand-mark"><Plane size={20} /></span>
          <span>SkyPort</span>
        </Link>
        <nav className="nav-links">
          {navItems.map(([to, label]) => <NavLink key={to} to={to}>{label}</NavLink>)}
        </nav>
        <div className="auth-actions">
          {auth.isAuthenticated ? (
            <>
              <span className="user-chip"><CircleUserRound size={16} />{auth.name || auth.email}</span>
              <button className="icon-btn" title="Logout" onClick={() => { auth.logout(); navigate("/"); }}>
                <LogOut size={18} />
              </button>
            </>
          ) : (
            <>
              <Link className="ghost-btn" to="/login" state={{ from: location }}>Login</Link>
              <Link className="primary-btn" to="/register">Create account</Link>
            </>
          )}
        </div>
      </header>
      <main>{children}</main>
    </div>
  );
}

function Protected({ children, adminOnly = false }) {
  const auth = useAuth();
  const location = useLocation();
  if (!auth.isAuthenticated) return <Navigate to="/login" state={{ from: location }} replace />;
  if (adminOnly && !auth.isAdmin) return <Navigate to="/flights" replace />;
  return children;
}

function HomePage() {
  return (
    <>
      <section className="hero">
        <div className="hero-copy">
          <p className="eyebrow">Book smarter across every cabin</p>
          <h1>SkyPort</h1>
          <p>Search routes, compare fares, reserve seats, download tickets, and manage trips from one clean dashboard.</p>
          <div className="hero-actions">
            <Link className="primary-btn big" to="/flights"><Search size={18} />Search flights</Link>
            <Link className="ghost-btn big" to="/bookings"><Ticket size={18} />My trips</Link>
          </div>
        </div>
        <FlightSearchPanel compact />
      </section>
      <section className="metrics-strip">
        <Stat icon={<Plane />} label="Live routes" value="Domestic and international" />
        <Stat icon={<ShieldCheck />} label="Secure booking" value="JWT protected trips" />
        <Stat icon={<BadgeIndianRupee />} label="Smart fares" value="Class prices and discounts" />
      </section>
    </>
  );
}

function FlightsPage() {
  const location = useLocation();
  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadFlights(query = {}) {
    setLoading(true);
    setError("");
    try {
      const params = new URLSearchParams(Object.entries(query).filter(([, value]) => value));
      const path = params.toString() ? `/api/flights/search?${params}` : "/api/flights";
      setFlights(await apiRequest(path));
    } catch (err) {
      setError(cleanError(err));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    loadFlights({
      source: params.get("source") || "",
      destination: params.get("destination") || "",
      departureDate: params.get("departureDate") || ""
    });
  }, [location.search]);

  return (
    <section className="page-grid">
      <aside className="filter-panel">
        <FlightSearchPanel onSearch={loadFlights} />
      </aside>
      <div className="content-stack">
        <PageTitle icon={<Plane />} title="Available flights" subtitle="Compare departure times, fares, seats, and discounts." />
        {error && <Alert tone="error" message={error} />}
        {loading ? <div className="loading">Loading flights...</div> : (
          <div className="flight-list">
            {flights.length === 0 ? <EmptyState title="No flights found" text="Try changing the route or date." /> : flights.map(flight => <FlightCard key={flight.id} flight={flight} />)}
          </div>
        )}
      </div>
    </section>
  );
}

function FlightSearchPanel({ onSearch, compact = false }) {
  const navigate = useNavigate();
  const location = useLocation();
  const initialQuery = new URLSearchParams(location.search);
  const [form, setForm] = useState({
    source: initialQuery.get("source") || "",
    destination: initialQuery.get("destination") || "",
    departureDate: initialQuery.get("departureDate") || ""
  });
  const update = event => setForm({ ...form, [event.target.name]: event.target.value });
  function submit(event) {
    event.preventDefault();
    if (onSearch) onSearch(form);
    else {
      const params = new URLSearchParams(Object.entries(form).filter(([, value]) => value));
      navigate(`/flights?${params}`);
    }
  }
  return (
    <form className={compact ? "search-panel compact" : "search-panel"} onSubmit={submit}>
      <div className="panel-heading">
        <Search size={18} />
        <span>Find your flight</span>
      </div>
      <label>From<input name="source" value={form.source} onChange={update} placeholder="Delhi" /></label>
      <label>To<input name="destination" value={form.destination} onChange={update} placeholder="Mumbai" /></label>
      <label>Date<input name="departureDate" type="date" value={form.departureDate} onChange={update} /></label>
      <button className="primary-btn" type="submit"><Search size={17} />Search</button>
    </form>
  );
}

function FlightCard({ flight, selectable = true, onPick, onDelete }) {
  const price = bestPrice(flight);
  const seats = (flight.economySeatsAvailable || 0) + (flight.businessSeatsAvailable || 0) + (flight.firstClassSeatsAvailable || 0);
  return (
    <article className="flight-card">
      <div className="flight-main">
        <div>
          <p className="airline">{flight.airline}</p>
          <h3>{flight.flightNumber}</h3>
          <span className={`status ${String(flight.status || "").toLowerCase()}`}>{flight.status || "SCHEDULED"}</span>
        </div>
        <div className="route-line">
          <div><strong>{flight.source}</strong><span>{formatTime(flight.departureTime)}</span></div>
          <ArrowRight size={22} />
          <div><strong>{flight.destination}</strong><span>{formatTime(flight.arrivalTime)}</span></div>
        </div>
      </div>
      <div className="fare-panel">
        <span>from</span>
        <strong>{money(price)}</strong>
        <small>{seats} seats left</small>
        {flight.discountPercentage > 0 && <small className="discount">{flight.discountPercentage}% off</small>}
        {selectable && <Link className="primary-btn" to={`/book/${flight.id}`}>Book</Link>}
        {onPick && <button className="ghost-btn" type="button" onClick={() => onPick(flight)}>Edit</button>}
        {onDelete && <button className="icon-btn danger" title="Delete flight" type="button" onClick={() => onDelete(flight.id)}><Trash2 size={18} /></button>}
      </div>
    </article>
  );
}

function BookingPage() {
  const { flightId } = useParams();
  const auth = useAuth();
  const navigate = useNavigate();
  const [flight, setFlight] = useState(null);
  const [returns, setReturns] = useState([]);
  const [booking, setBooking] = useState({
    travelClass: "ECONOMY",
    seatsBooked: 1,
    selectedSeatNumbers: "",
    returnFlightId: "",
    returnSelectedSeatNumbers: "",
    bookingForSelf: true,
    passengerName: "",
    passengerEmail: ""
  });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    apiRequest(`/api/flights/${flightId}`).then(next => {
      setFlight(next);
      return apiRequest(`/api/flights/search?source=${encodeURIComponent(next.destination)}&destination=${encodeURIComponent(next.source)}`);
    }).then(setReturns).catch(err => setError(cleanError(err)));
  }, [flightId]);

  async function submit(event) {
    event.preventDefault();
    setError("");
    setMessage("");
    try {
      const payload = {
        flightId: Number(flightId),
        returnFlightId: booking.returnFlightId ? Number(booking.returnFlightId) : null,
        travelClass: booking.travelClass,
        seatsBooked: Number(booking.seatsBooked),
        selectedSeatNumbers: splitSeats(booking.selectedSeatNumbers),
        returnSelectedSeatNumbers: splitSeats(booking.returnSelectedSeatNumbers),
        bookingForSelf: booking.bookingForSelf,
        passengerName: booking.bookingForSelf ? null : booking.passengerName,
        passengerEmail: booking.bookingForSelf ? null : booking.passengerEmail
      };
      const created = await apiRequest("/api/bookings", { method: "POST", body: JSON.stringify(payload) }, auth.token);
      setMessage(`Booking confirmed. Ticket ${created.ticketNumber}`);
      setTimeout(() => navigate("/bookings"), 900);
    } catch (err) {
      setError(cleanError(err));
    }
  }

  if (!flight && !error) return <div className="loading page-pad">Loading booking...</div>;

  return (
    <section className="booking-layout">
      <div className="content-stack">
        <PageTitle icon={<Ticket />} title="Complete booking" subtitle="Choose cabin, seats, passenger, and optional return flight." />
        {error && <Alert tone="error" message={error} />}
        {message && <Alert tone="success" message={message} />}
        {flight && <FlightCard flight={flight} selectable={false} />}
        <form className="form-card" onSubmit={submit}>
          <div className="form-row">
            <label>Travel class<select value={booking.travelClass} onChange={e => setBooking({ ...booking, travelClass: e.target.value })}>{travelClasses.map(item => <option key={item}>{item}</option>)}</select></label>
            <label>Seats<input type="number" min="1" value={booking.seatsBooked} onChange={e => setBooking({ ...booking, seatsBooked: e.target.value })} /></label>
          </div>
          <label>Seat numbers<input value={booking.selectedSeatNumbers} onChange={e => setBooking({ ...booking, selectedSeatNumbers: e.target.value })} placeholder="A1, A2" /></label>
          <label className="check-row"><input type="checkbox" checked={booking.bookingForSelf} onChange={e => setBooking({ ...booking, bookingForSelf: e.target.checked })} /> Booking for myself</label>
          {!booking.bookingForSelf && (
            <div className="form-row">
              <label>Passenger name<input value={booking.passengerName} onChange={e => setBooking({ ...booking, passengerName: e.target.value })} /></label>
              <label>Passenger email<input type="email" value={booking.passengerEmail} onChange={e => setBooking({ ...booking, passengerEmail: e.target.value })} /></label>
            </div>
          )}
          <label>Return flight<select value={booking.returnFlightId} onChange={e => setBooking({ ...booking, returnFlightId: e.target.value })}><option value="">One way</option>{returns.filter(item => String(item.id) !== String(flightId)).map(item => <option key={item.id} value={item.id}>{item.flightNumber} - {formatDateTime(item.departureTime)}</option>)}</select></label>
          {booking.returnFlightId && <label>Return seat numbers<input value={booking.returnSelectedSeatNumbers} onChange={e => setBooking({ ...booking, returnSelectedSeatNumbers: e.target.value })} placeholder="B1, B2" /></label>}
          <button className="primary-btn big" type="submit"><CheckCircle2 size={18} />Confirm booking</button>
        </form>
      </div>
      <aside className="summary-panel">
        <h3>Fare summary</h3>
        {flight && <FareSummary flight={flight} cabin={booking.travelClass} seats={booking.seatsBooked} />}
      </aside>
    </section>
  );
}

function MyBookingsPage() {
  const auth = useAuth();
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");

  async function load() {
    try {
      setBookings(await apiRequest("/api/bookings/my", {}, auth.token));
    } catch (err) {
      setError(cleanError(err));
    }
  }

  useEffect(() => { load(); }, []);

  async function cancel(ticketNumber) {
    await apiRequest(`/api/bookings/ticket/${ticketNumber}/cancel`, { method: "PATCH" }, auth.token);
    load();
  }

  async function download(ticketNumber) {
    const response = await fetch(`${API_BASE_URL}/api/bookings/ticket/${ticketNumber}/download`, { headers: { Authorization: `Bearer ${auth.token}` } });
    const blob = await response.blob();
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement("a");
    anchor.href = url;
    anchor.download = `${ticketNumber}.txt`;
    anchor.click();
    URL.revokeObjectURL(url);
  }

  return (
    <section className="page-pad content-stack">
      <PageTitle icon={<Ticket />} title="My trips" subtitle="View, cancel, and download your confirmed tickets." />
      {error && <Alert tone="error" message={error} />}
      <div className="booking-list">
        {bookings.length === 0 ? <EmptyState title="No bookings yet" text="Book a flight to see tickets here." /> : bookings.map(item => (
          <article className="booking-card" key={item.bookingId}>
            <div>
              <span className={`status ${String(item.status).toLowerCase()}`}>{item.status}</span>
              <h3>{item.source} to {item.destination}</h3>
              <p>{item.airline} {item.flightNumber} - {formatDateTime(item.departureTime)}</p>
              {item.roundTrip && <p>Return: {item.returnAirline} {item.returnFlightNumber} - {formatDateTime(item.returnDepartureTime)}</p>}
            </div>
            <div className="ticket-side">
              <strong>{item.ticketNumber}</strong>
              <span>{money(item.totalPrice)}</span>
              <div className="button-row">
                <button className="icon-btn" title="Download ticket" onClick={() => download(item.ticketNumber)}><Download size={18} /></button>
                {item.status !== "CANCELLED" && <button className="icon-btn danger" title="Cancel booking" onClick={() => cancel(item.ticketNumber)}><XCircle size={18} /></button>}
              </div>
            </div>
          </article>
        ))}
      </div>
    </section>
  );
}

function SavedPassengersPage() {
  const auth = useAuth();
  const [passengers, setPassengers] = useState([]);
  useEffect(() => {
    apiRequest("/api/bookings/saved-passengers", {}, auth.token).then(setPassengers).catch(() => setPassengers([]));
  }, [auth.token]);
  return (
    <section className="page-pad content-stack">
      <PageTitle icon={<Users />} title="Saved passengers" subtitle="People you have booked tickets for before." />
      <div className="passenger-grid">
        {passengers.length === 0 ? <EmptyState title="No saved passengers" text="Book for someone else to create a passenger shortcut." /> : passengers.map(item => (
          <article className="passenger-card" key={`${item.passengerName}-${item.passengerEmail}`}>
            <CircleUserRound size={28} />
            <h3>{item.passengerName}</h3>
            <p>{item.passengerEmail}</p>
          </article>
        ))}
      </div>
    </section>
  );
}

function AdminFlightsPage() {
  const auth = useAuth();
  const [flights, setFlights] = useState([]);
  const [form, setForm] = useState(blankFlight);
  const [editingId, setEditingId] = useState("");
  const [error, setError] = useState("");

  async function load() {
    setFlights(await apiRequest("/api/admin/flights", {}, auth.token));
  }

  useEffect(() => { load().catch(err => setError(cleanError(err))); }, []);

  function edit(flight) {
    setEditingId(flight.id);
    setForm({ ...blankFlight, ...flight, departureTime: toInputDateTime(flight.departureTime), arrivalTime: toInputDateTime(flight.arrivalTime) });
  }

  async function submit(event) {
    event.preventDefault();
    setError("");
    const payload = Object.fromEntries(Object.entries(form).map(([key, value]) => [key, numericFlightField(key) ? Number(value || 0) : value]));
    try {
      await apiRequest(editingId ? `/api/admin/flights/${editingId}` : "/api/admin/flights", { method: editingId ? "PUT" : "POST", body: JSON.stringify(payload) }, auth.token);
      setForm(blankFlight);
      setEditingId("");
      load();
    } catch (err) {
      setError(cleanError(err));
    }
  }

  async function remove(id) {
    await apiRequest(`/api/admin/flights/${id}`, { method: "DELETE" }, auth.token);
    load();
  }

  return (
    <section className="admin-layout">
      <div className="content-stack">
        <PageTitle icon={<BriefcaseBusiness />} title="Admin flights" subtitle="Create and maintain schedules, fares, seats, and statuses." />
        {error && <Alert tone="error" message={error} />}
        <div className="flight-list">{flights.map(flight => <FlightCard key={flight.id} flight={flight} selectable={false} onPick={edit} onDelete={remove} />)}</div>
      </div>
      <form className="form-card admin-form" onSubmit={submit}>
        <div className="panel-heading"><Plus size={18} /><span>{editingId ? "Update flight" : "Add flight"}</span></div>
        {Object.keys(blankFlight).map(key => (
          <label key={key}>{labelize(key)}
            <input
              type={key.includes("Time") ? "datetime-local" : numericFlightField(key) ? "number" : "text"}
              value={form[key] ?? ""}
              onChange={e => setForm({ ...form, [key]: e.target.value })}
              required={["flightNumber", "airline", "source", "destination"].includes(key)}
            />
          </label>
        ))}
        <button className="primary-btn" type="submit">{editingId ? "Save changes" : "Create flight"}</button>
        {editingId && <button className="ghost-btn" type="button" onClick={() => { setForm(blankFlight); setEditingId(""); }}>Cancel edit</button>}
      </form>
    </section>
  );
}

function AuthPage({ mode }) {
  const isRegister = mode === "register";
  const auth = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  async function submit(event) {
    event.preventDefault();
    setError("");
    setMessage("");
    try {
      const path = isRegister ? "/api/auth/register" : "/api/auth/login";
      const body = isRegister ? form : { email: form.email, password: form.password };
      const result = await apiRequest(path, { method: "POST", body: JSON.stringify(body) });
      if (isRegister) {
        setMessage("Account created. You can login now.");
      } else {
        auth.login(result.token, result.email || form.email, result.name || "", result.role || "");
        navigate(location.state?.from?.pathname || "/flights");
      }
    } catch (err) {
      setError(cleanError(err));
    }
  }

  return (
    <section className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <Plane size={30} />
        <h1>{isRegister ? "Create your account" : "Welcome back"}</h1>
        {error && <Alert tone="error" message={error} />}
        {message && <Alert tone="success" message={message} />}
        {isRegister && <label>Name<input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} required /></label>}
        <label>Email<input type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required /></label>
        <label>Password<input type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} required /></label>
        <button className="primary-btn big" type="submit">{isRegister ? "Create account" : "Login"}</button>
        <p>{isRegister ? "Already have an account?" : "New here?"} <Link to={isRegister ? "/login" : "/register"}>{isRegister ? "Login" : "Register"}</Link></p>
        <small>Admin demo: admin@flight.com / admin123</small>
      </form>
    </section>
  );
}

function PageTitle({ icon, title, subtitle }) {
  return <div className="page-title"><span>{icon}</span><div><h1>{title}</h1><p>{subtitle}</p></div></div>;
}

function Stat({ icon, label, value }) {
  return <div className="stat"><span>{icon}</span><div><strong>{label}</strong><small>{value}</small></div></div>;
}

function Alert({ message, tone }) {
  return <div className={`alert ${tone}`}>{message}</div>;
}

function EmptyState({ title, text }) {
  return <div className="empty-state"><h3>{title}</h3><p>{text}</p></div>;
}

function FareSummary({ flight, cabin, seats }) {
  const unit = cabinPrice(flight, cabin);
  const discount = Number(flight.discountPercentage || 0);
  const total = unit * Number(seats || 1) * Math.max(0, (100 - discount) / 100);
  return (
    <div className="fare-summary">
      <p><span>Cabin</span><strong>{cabin}</strong></p>
      <p><span>Seats</span><strong>{seats}</strong></p>
      <p><span>Base fare</span><strong>{money(unit)}</strong></p>
      <p><span>Discount</span><strong>{discount}%</strong></p>
      <p className="total"><span>Total</span><strong>{money(total)}</strong></p>
    </div>
  );
}

function bestPrice(flight) {
  return Math.min(...[flight.economyPrice, flight.businessPrice, flight.firstClassPrice, flight.price].filter(value => Number(value) > 0));
}

function cabinPrice(flight, cabin) {
  if (cabin === "BUSINESS") return Number(flight.businessPrice || flight.price || 0);
  if (cabin === "FIRST_CLASS") return Number(flight.firstClassPrice || flight.price || 0);
  return Number(flight.economyPrice || flight.price || 0);
}

function money(value) {
  return new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR", maximumFractionDigits: 0 }).format(Number(value || 0));
}

function formatTime(value) {
  if (!value) return "--";
  return new Intl.DateTimeFormat("en-IN", { hour: "2-digit", minute: "2-digit" }).format(new Date(value));
}

function formatDateTime(value) {
  if (!value) return "--";
  return new Intl.DateTimeFormat("en-IN", { dateStyle: "medium", timeStyle: "short" }).format(new Date(value));
}

function toInputDateTime(value) {
  return value ? String(value).slice(0, 16) : "";
}

function splitSeats(value) {
  return value ? value.split(",").map(item => item.trim()).filter(Boolean) : [];
}

function numericFlightField(key) {
  return ["price", "economyPrice", "businessPrice", "firstClassPrice", "economySeatsAvailable", "businessSeatsAvailable", "firstClassSeatsAvailable", "discountPercentage"].includes(key);
}

function labelize(key) {
  return key.replace(/([A-Z])/g, " $1").replace(/^./, char => char.toUpperCase());
}

function cleanError(error) {
  try {
    const parsed = JSON.parse(error.message);
    return parsed.message || parsed.error || error.message;
  } catch {
    return error.message;
  }
}

createRoot(document.getElementById("root")).render(<App />);
