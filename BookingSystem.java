import java.util.ArrayList;

public class BookingSystem {
    private ArrayList<User> users;
    private ArrayList<Flight> flights;
    private ArrayList<Booking> bookings;
    private ArrayList<Payment> payments;

    public BookingSystem() {
        this.users = new ArrayList<>();
        this.flights = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public ArrayList<Flight> searchFlights(String origin, String destination, String date) {
        ArrayList<Flight> results = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getOrigin().equalsIgnoreCase(origin) &&
                flight.getDestination().equalsIgnoreCase(destination) &&
                flight.getDepartureTime().equals(date)) {
                results.add(flight);
            }
        }
        return results;
    }

    public Booking createBooking(Customer customer, Flight flight, ArrayList<Passenger> passengers) {
        if (customer == null || flight == null || passengers == null || passengers.isEmpty()) {
            System.out.println("Invalid booking data.");
            return null;
        }
        
        Booking booking = new Booking(customer, flight, passengers);
        bookings.add(booking);
        return booking;
    }

    public boolean processPayment(Booking booking, String paymentMethod) {
        if (booking == null || paymentMethod == null || paymentMethod.isEmpty()) {
            System.out.println("Invalid payment data.");
            return false;
        }
        
        return booking.processPayment(paymentMethod);
    }

    public void generateTicket(Booking booking) {
        if (booking == null) {
            System.out.println("Invalid booking.");
            return;
        }
        
        booking.generateItinerary();
    }

    public void saveAllData() {
        FileManager.saveUsers(users);
        FileManager.saveFlights(flights);
        FileManager.saveBookings(bookings);
        FileManager.savePassengers(getAllPassengers());
    }

    public void loadAllData() {
        // First load users and flights
        users = FileManager.loadUsers();
        flights = FileManager.loadFlights();
        
        // Initialize the static flights collection in Flight class
        Flight.initializeFlights(flights);
        
        // Now load bookings (which depend on users, flights, and passengers)
        bookings = FileManager.loadBookings();
        
        System.out.println("All data loaded successfully.");
        System.out.println("- Users: " + users.size());
        System.out.println("- Flights: " + flights.size());
        System.out.println("- Bookings: " + bookings.size());
    }

    private ArrayList<Passenger> getAllPassengers() {
        ArrayList<Passenger> allPassengers = new ArrayList<>();
        for (Booking booking : bookings) {
            allPassengers.addAll(booking.getPassengers());
        }
        return allPassengers;
    }

    // Getters
    public ArrayList<User> getUsers() { return users; }
    public ArrayList<Flight> getFlights() { return flights; }
    public ArrayList<Booking> getBookings() { return bookings; }
    public ArrayList<Payment> getPayments() { return payments; }
}
