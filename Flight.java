import java.util.ArrayList;
import java.util.HashMap;

public class Flight {
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private HashMap<String, Integer> availableSeats;
    private HashMap<String, Double> prices;
    
    // Static collection of all flights
    private static ArrayList<Flight> flights = new ArrayList<>();
    
    // Static method to initialize flights from loaded data
    public static void initializeFlights(ArrayList<Flight> loadedFlights) {
        flights.clear();
        if (loadedFlights != null) {
            flights.addAll(loadedFlights);
        }
    }

    public Flight(String flightNumber, String airline, String origin, String destination,
                 String departureTime, String arrivalTime) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = new HashMap<>();
        this.prices = new HashMap<>();
        
        // Initialize seat classes and prices with default values
        initializeSeats();
        
        // Add this flight to the static collection if it doesn't exist already
        boolean flightExists = false;
        for (Flight existingFlight : flights) {
            if (existingFlight.getFlightNumber().equals(this.flightNumber)) {
                flightExists = true;
                break;
            }
        }
        
        if (!flightExists) {
            flights.add(this);
        }
    }
    
    public Flight(String flightNumber, String airline, String origin, String destination,
                 String departureTime, String arrivalTime, 
                 int economySeats, int businessSeats, int firstClassSeats,
                 double economyPrice, double businessPrice, double firstClassPrice) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = new HashMap<>();
        this.prices = new HashMap<>();
        
        // Set custom seat counts and prices
        availableSeats.put("Economy", economySeats);
        availableSeats.put("Business", businessSeats);
        availableSeats.put("First Class", firstClassSeats);
        
        prices.put("Economy", economyPrice);
        prices.put("Business", businessPrice);
        prices.put("First Class", firstClassPrice);
    }

    private void initializeSeats() {
        availableSeats.put("Economy", 150);
        availableSeats.put("Business", 30);
        availableSeats.put("First Class", 10);
        
        prices.put("Economy", 500.0);
        prices.put("Business", 1000.0);
        prices.put("First Class", 1500.0);
    }

    public boolean validateFlightData() {
        if (flightNumber == null || flightNumber.isEmpty()) {
            System.out.println("Flight number is required.");
            return false;
        }
        
        if (airline == null || airline.isEmpty()) {
            System.out.println("Airline is required.");
            return false;
        }
        
        if (origin == null || origin.isEmpty()) {
            System.out.println("Origin is required.");
            return false;
        }
        
        if (destination == null || destination.isEmpty()) {
            System.out.println("Destination is required.");
            return false;
        }
        
        if (departureTime == null || departureTime.isEmpty()) {
            System.out.println("Departure time is required.");
            return false;
        }
        
        if (arrivalTime == null || arrivalTime.isEmpty()) {
            System.out.println("Arrival time is required.");
            return false;
        }
        
        return true;
    }
    
    // Static methods for flight management
    public static ArrayList<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    public static boolean addFlight(String flightNumber, String airline, String origin,
                           String destination, String departureTime, String arrivalTime) {
        Flight newFlight = new Flight(flightNumber, airline, origin, destination,
                                    departureTime, arrivalTime);
        
        if (!newFlight.validateFlightData()) {
            return false;
        }
        
        for (Flight existingFlight : flights) {
            if (existingFlight.getFlightNumber().equals(flightNumber)) {
                System.out.println("Flight number already exists.");
                return false;
            }
        }
        
        flights.add(newFlight);
        return true;
    }
    
    public static boolean addFlight(String flightNumber, String airline, String origin,
                           String destination, String departureTime, String arrivalTime,
                           int economySeats, int businessSeats, int firstClassSeats,
                           double economyPrice, double businessPrice, double firstClassPrice) {
        Flight newFlight = new Flight(flightNumber, airline, origin, destination,
                                    departureTime, arrivalTime,
                                    economySeats, businessSeats, firstClassSeats,
                                    economyPrice, businessPrice, firstClassPrice);
        
        if (!newFlight.validateFlightData()) {
            return false;
        }
        
        for (Flight existingFlight : flights) {
            if (existingFlight.getFlightNumber().equals(flightNumber)) {
                System.out.println("Flight number already exists.");
                return false;
            }
        }
        
        flights.add(newFlight);
        return true;
    }

    // Instance methods
    public boolean checkAvailability(String seatClass) {
        return availableSeats.getOrDefault(seatClass, 0) > 0;
    }

    public void updateSchedule(String departureTime, String arrivalTime) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public boolean selectSeat(String seatClass) {
        if (!availableSeats.containsKey(seatClass)) {
            return false;
        }
        
        int seats = availableSeats.get(seatClass);
        if (seats > 0) {
            availableSeats.put(seatClass, seats - 1);
            return true;
        }
        return false;
    }

    public double calculatePrice(String seatClass) {
        return prices.getOrDefault(seatClass, 0.0);
    }

    public boolean reserveSeat(String seatClass) {
        if (checkAvailability(seatClass)) {
            availableSeats.put(seatClass, availableSeats.get(seatClass) - 1);
            return true;
        }
        return false;
    }

    // Getters
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    
    // Setters
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public HashMap<String, Integer> getAvailableSeats() { return availableSeats; }
    public HashMap<String, Double> getPrices() { return prices; }
}
