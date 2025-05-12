import java.util.ArrayList;

public class Booking {
    private String bookingReference;
    private Customer customer;
    private Flight flight;
    private ArrayList<Passenger> passengers;
    private ArrayList<String> seatSelections;
    private String status;
    private Payment payment;

    public Booking(Customer customer, Flight flight, ArrayList<Passenger> passengers) {
        this.customer = customer;
        this.flight = flight;
        this.passengers = passengers;
        this.seatSelections = new ArrayList<>();
        this.status = "Reserved";
        this.bookingReference = "B" + System.currentTimeMillis();
        this.payment = new Payment(this);
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (String seatClass : seatSelections) {
            total += flight.calculatePrice(seatClass);
        }
        return total;
    }

    public boolean confirmBooking() {
        if (payment.processPayment()) {
            status = "Confirmed";
            return true;
        }
        return false;
    }

    public void cancelBooking() {
        status = "Cancelled";
    }

    public void generateItinerary() {
        System.out.println("\n=== Itinerary Details ===");
        System.out.println("Booking Reference: " + bookingReference);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Flight: " + flight.getFlightNumber() + " - " + flight.getAirline());
        System.out.println("From: " + flight.getOrigin() + " to " + flight.getDestination());
        System.out.println("Departure: " + flight.getDepartureTime());
        System.out.println("Arrival: " + flight.getArrivalTime());
        
        System.out.println("\nPassengers:");
        for (Passenger passenger : passengers) {
            System.out.println("- " + passenger.getName());
        }
        
        System.out.println("\nSeat Selections:");
        for (String seatClass : seatSelections) {
            System.out.println("- " + seatClass);
        }
        
        System.out.println("\nTotal Price: $" + calculateTotalPrice());
        System.out.println("Payment Status: " + payment.getStatus());
        System.out.println("Booking Status: " + status);
    }

    public boolean processPayment(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            System.out.println("Payment method is required.");
            return false;
        }
        
        payment.setMethod(paymentMethod);
        if (payment.processPayment()) {
            status = "Confirmed";
            return true;
        }
        return false;
    }

    public void updateSpecialRequests(String passengerId, ArrayList<String> requests) {
        for (Passenger passenger : passengers) {
            if (passenger.getPassengerId().equals(passengerId)) {
                passenger.setSpecialRequests(requests);
                break;
            }
        }
    }

    // Getters and Setters
    public String getBookingReference() { return bookingReference; }
    public Customer getCustomer() { return customer; }
    public Flight getFlight() { return flight; }
    public ArrayList<Passenger> getPassengers() { return passengers; }
    public ArrayList<String> getSeatSelections() { return seatSelections; }
    public String getStatus() { return status; }
    public Payment getPayment() { return payment; }

    public void setPassengers(ArrayList<Passenger> passengers) {
        if (passengers == null) {
            throw new IllegalArgumentException("Passengers list cannot be null");
        }
        this.passengers = passengers;
    }
}
