import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User {
    private String customerId;
    private String address;
    private ArrayList<Booking> bookingHistory;
    private ArrayList<String> preferences;

    public Customer(String username, String password, String name, String email, String contactInfo, String address) {
        super(username, password, name, email, contactInfo);
        this.customerId = "C" + System.currentTimeMillis();
        this.address = address;
        this.bookingHistory = new ArrayList<>();
        this.preferences = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        if (!super.validatePassword(password)) {
            System.out.println("Password must be at least 6 characters with letters and numbers.");
            return false;
        }
        
        // Check if username and password match
        if (this.getUsername().equals(username) && this.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    // Get password from User class
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void logout() {
        super.logout();
    }

    @Override
    public void updateProfile(String name, String email, String contactInfo) {
        super.updateProfile(name, email, contactInfo);
        if (address != null && !address.isEmpty()) {
            setAddress(address);
        }
    }

    public ArrayList<Flight> searchFlights(String origin, String destination, String date) {
        ArrayList<Flight> results = new ArrayList<>();
        for (Flight flight : Flight.getAllFlights()) {
            if (flight.getOrigin().equalsIgnoreCase(origin) &&
                flight.getDestination().equalsIgnoreCase(destination) &&
                flight.getDepartureTime().equals(date)) {
                results.add(flight);
            }
        }
        return results;
    }

    public Booking createBooking(Flight flight, ArrayList<Passenger> passengers) {
        if (flight == null || passengers == null || passengers.isEmpty()) {
            System.out.println("Invalid booking data.");
            return null;
        }
        
        for (Passenger passenger : passengers) {
            if (passenger == null) {
                System.out.println("Invalid passenger data.");
                return null;
            }
        }
        
        // Check if there are enough seats available
        HashMap<String, Integer> availableSeats = flight.getAvailableSeats();
        int totalSeatsNeeded = passengers.size();
        int totalSeatsAvailable = 0;
        
        for (String seatClass : availableSeats.keySet()) {
            totalSeatsAvailable += availableSeats.get(seatClass);
        }
        
        if (totalSeatsAvailable < totalSeatsNeeded) {
            System.out.println("Not enough seats available on this flight. Available: " + 
                              totalSeatsAvailable + ", Required: " + totalSeatsNeeded);
            return null;
        }
        
        Booking booking = new Booking(this, flight, passengers);
        bookingHistory.add(booking);
        return booking;
    }

    public void viewBookings() {
        System.out.println("\n=== Your Bookings ===");
        if (bookingHistory.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        
        for (Booking booking : bookingHistory) {
            System.out.println("\nBooking Reference: " + booking.getBookingReference());
            System.out.println("Flight: " + booking.getFlight().getFlightNumber());
            System.out.println("Status: " + booking.getStatus());
            System.out.println("Total Price: $" + booking.calculateTotalPrice());
        }
    }

    public void cancelBooking(String bookingReference) {
        for (Booking booking : bookingHistory) {
            if (booking.getBookingReference().equals(bookingReference)) {
                booking.cancelBooking();
                System.out.println("Booking cancelled successfully.");
                return;
            }
        }
        System.out.println("Booking not found.");
    }



    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public String getAddress() { return address; }
    public ArrayList<Booking> getBookingHistory() { return bookingHistory; }
    public ArrayList<String> getPreferences() { return preferences; }

    public void setAddress(String address) { this.address = address; }
    public void setPreferences(ArrayList<String> preferences) { this.preferences = preferences; }
}
