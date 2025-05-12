import java.util.ArrayList;

public class Agent extends User {
    private String agentId;
    private String department;
    private double commission;

    public Agent(String username, String password, String name, String email, String contactInfo,
                String department, double commission) {
        super(username, password, name, email, contactInfo);
        this.agentId = "A" + System.currentTimeMillis();
        this.department = department;
        this.commission = commission;
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

    public ArrayList<Flight> manageFlights() {
        // Access the flights list from the Flight class
        return Flight.getAllFlights();
    }

    public boolean addFlight(String flightNumber, String airline, String origin,
                           String destination, String departureTime, String arrivalTime) {
        return Flight.addFlight(flightNumber, airline, origin, destination,
                              departureTime, arrivalTime);
    }
    
    public boolean addFlight(String flightNumber, String airline, String origin,
                           String destination, String departureTime, String arrivalTime,
                           int economySeats, int businessSeats, int firstClassSeats,
                           double economyPrice, double businessPrice, double firstClassPrice) {
        return Flight.addFlight(flightNumber, airline, origin, destination,
                              departureTime, arrivalTime,
                              economySeats, businessSeats, firstClassSeats,
                              economyPrice, businessPrice, firstClassPrice);
    }

    public boolean updateFlight(Flight flight, String departureTime, String arrivalTime) {
        if (flight == null) {
            return false;
        }
        flight.updateSchedule(departureTime, arrivalTime);
        return true;
    }

    public Booking createBookingForCustomer(Customer customer, Flight flight, ArrayList<Passenger> passengers) {
        if (customer == null || flight == null || passengers == null || passengers.isEmpty()) {
            System.out.println("Invalid booking data.");
            return null;
        }
        
        Booking booking = new Booking(customer, flight, passengers);
        customer.getBookingHistory().add(booking);
        return booking;
    }

    public boolean modifyBooking(Booking booking, ArrayList<Passenger> newPassengers) {
        if (booking == null || newPassengers == null || newPassengers.isEmpty()) {
            System.out.println("Invalid booking data.");
            return false;
        }
        
        booking.setPassengers(newPassengers);
        return true;
    }

    private ArrayList<Booking> bookings = new ArrayList<>();

    public ArrayList<String> generateReports() {
        ArrayList<String> reports = new ArrayList<>();
        reports.add("Total Bookings: " + bookings.size());
        reports.add("Total Revenue: $" + calculateTotalRevenue());
        return reports;
    }

    private double calculateTotalRevenue() {
        double total = 0;
        for (Booking booking : bookings) {
            if (booking.getStatus().equals("Confirmed")) {
                total += booking.calculateTotalPrice();
            }
        }
        return total;
    }

    // Getters and Setters
    public String getAgentId() { return agentId; }
    public String getDepartment() { return department; }
    public double getCommission() { return commission; }

    public void setDepartment(String department) { this.department = department; }
    public void setCommission(double commission) { this.commission = commission; }
}
