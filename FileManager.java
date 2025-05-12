import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FileManager {
    private static final String USERS_FILE = "users.txt";
    private static final String FLIGHTS_FILE = "flights.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final String PASSENGERS_FILE = "passengers.txt";

    public static void saveUsers(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                if (user instanceof Customer) {
                    Customer customer = (Customer) user;
                    writer.println("CUSTOMER|" + 
                                  customer.getUserId() + "|" +
                                  customer.getUsername() + "|" +
                                  customer.getPassword() + "|" +
                                  customer.getName() + "|" +
                                  customer.getEmail() + "|" +
                                  customer.getContactInfo() + "|" +
                                  customer.getAddress());
                } else if (user instanceof Agent) {
                    Agent agent = (Agent) user;
                    writer.println("AGENT|" + 
                                  agent.getUserId() + "|" +
                                  agent.getUsername() + "|" +
                                  agent.getPassword() + "|" +
                                  agent.getName() + "|" +
                                  agent.getEmail() + "|" +
                                  agent.getContactInfo() + "|" +
                                  agent.getDepartment() + "|" +
                                  agent.getCommission());
                } else if (user instanceof Administrator) {
                    Administrator admin = (Administrator) user;
                    writer.println("ADMIN|" + 
                                  admin.getUserId() + "|" +
                                  admin.getUsername() + "|" +
                                  admin.getPassword() + "|" +
                                  admin.getName() + "|" +
                                  admin.getEmail() + "|" +
                                  admin.getContactInfo() + "|" +
                                  admin.getSecurityLevel());
                }
            }
            System.out.println("Users saved successfully to " + USERS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        
        // If file doesn't exist or is empty, return empty list and add default admin
        if (!file.exists() || file.length() == 0) {
            System.out.println("No users file found or file is empty. Creating default admin user.");
            // Create a default admin user if no users exist
            Administrator defaultAdmin = new Administrator("admin", "admin123", "System Administrator", 
                                                         "admin@system.com", "n/a", 10);
            users.add(defaultAdmin);
            return users;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 7) {
                        System.out.println("Warning: Invalid user data format at line " + lineNumber + ", skipping.");
                        continue;
                    }
                    
                    String type = parts[0];
                    String userId = parts[1];
                    String username = parts[2];
                    String password = parts[3];
                    String name = parts[4];
                    String email = parts[5];
                    String contactInfo = parts[6];
                    
                    if (type.equals("CUSTOMER") && parts.length >= 8) {
                        String address = parts[7];
                        Customer customer = new Customer(username, password, name, email, contactInfo, address);
                        users.add(customer);
                    } else if (type.equals("AGENT") && parts.length >= 9) {
                        try {
                            String department = parts[7];
                            double commission = Double.parseDouble(parts[8]);
                            Agent agent = new Agent(username, password, name, email, contactInfo, department, commission);
                            users.add(agent);
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Invalid commission format for agent at line " + lineNumber + ": " + e.getMessage() + ", skipping.");
                        }
                    } else if (type.equals("ADMIN") && parts.length >= 8) {
                        try {
                            int securityLevel = Integer.parseInt(parts[7]);
                            Administrator admin = new Administrator(username, password, name, email, contactInfo, securityLevel);
                            users.add(admin);
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Invalid security level format for admin at line " + lineNumber + ": " + e.getMessage() + ", skipping.");
                        }
                    } else {
                        System.out.println("Warning: Unknown user type '" + type + "' at line " + lineNumber + ", skipping.");
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Error processing user at line " + lineNumber + ": " + e.getMessage() + ", skipping.");
                }
            }
            System.out.println("Users loaded successfully from " + USERS_FILE);
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        
        // If no users were loaded (all were invalid), create a default admin
        if (users.isEmpty()) {
            System.out.println("No valid users found in file. Creating default admin user.");
            Administrator defaultAdmin = new Administrator("admin", "admin123", "System Administrator", 
                                                         "admin@system.com", "n/a", 10);
            users.add(defaultAdmin);
        }
        
        return users;
    }

    public static void saveFlights(ArrayList<Flight> flights) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FLIGHTS_FILE))) {
            for (Flight flight : flights) {
                writer.println(flight.getFlightNumber() + "|" +
                              flight.getAirline() + "|" +
                              flight.getOrigin() + "|" +
                              flight.getDestination() + "|" +
                              flight.getDepartureTime() + "|" +
                              flight.getArrivalTime());
            }
            System.out.println("Flights saved successfully to " + FLIGHTS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving flights: " + e.getMessage());
        }
    }

    public static ArrayList<Flight> loadFlights() {
        ArrayList<Flight> flights = new ArrayList<>();
        File file = new File(FLIGHTS_FILE);
        
        // If file doesn't exist or is empty, create sample flights
        if (!file.exists() || file.length() == 0) {
            System.out.println("No flights file found or file is empty. Creating sample flights.");
            // Add some sample flights
            Flight sampleFlight1 = new Flight("FL001", "EgyptAir", "Cairo", "London", "2025-06-01 10:00", "2025-06-01 14:00");
            Flight sampleFlight2 = new Flight("FL002", "British Airways", "Cairo", "London", "2025-06-01 14:30", "2025-06-01 18:30");
            Flight sampleFlight3 = new Flight("FL003", "EgyptAir", "Cairo", "London", "2025-06-02 08:00", "2025-06-02 12:00");
            flights.add(sampleFlight1);
            flights.add(sampleFlight2);
            flights.add(sampleFlight3);
            
            // Save the sample flights to the file
            saveFlights(flights);
            
            return flights;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 6) {
                        System.out.println("Warning: Invalid flight data format at line " + lineNumber + ", skipping.");
                        continue;
                    }
                    
                    String flightNumber = parts[0];
                    String airline = parts[1];
                    String origin = parts[2];
                    String destination = parts[3];
                    String departureTime = parts[4];
                    String arrivalTime = parts[5];
                    
                    Flight flight = new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime);
                    
                    // Validate flight data
                    if (!flight.validateFlightData()) {
                        System.out.println("Warning: Invalid flight data at line " + lineNumber + ", skipping.");
                        continue;
                    }
                    
                    flights.add(flight);
                } catch (Exception e) {
                    System.out.println("Warning: Error processing flight at line " + lineNumber + ": " + e.getMessage() + ", skipping.");
                }
            }
            System.out.println("Flights loaded successfully from " + FLIGHTS_FILE);
        } catch (IOException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }
        
        return flights;
    }

    public static void saveBookings(ArrayList<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : bookings) {
                // Main booking info
                StringBuilder sb = new StringBuilder();
                sb.append(booking.getBookingReference()).append("|");
                sb.append(booking.getCustomer().getUserId()).append("|");
                sb.append(booking.getFlight().getFlightNumber()).append("|");
                sb.append(booking.getStatus()).append("|");
                
                // Passenger IDs
                sb.append(booking.getPassengers().size()).append("|");
                for (Passenger passenger : booking.getPassengers()) {
                    sb.append(passenger.getPassengerId()).append(",");
                }
                if (!booking.getPassengers().isEmpty()) {
                    sb.deleteCharAt(sb.length() - 1); // Remove last comma
                }
                sb.append("|");
                
                // Seat selections
                for (String seatClass : booking.getSeatSelections()) {
                    sb.append(seatClass).append(",");
                }
                if (!booking.getSeatSelections().isEmpty()) {
                    sb.deleteCharAt(sb.length() - 1); // Remove last comma
                }
                
                writer.println(sb.toString());
            }
            System.out.println("Bookings saved successfully to " + BOOKINGS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    public static ArrayList<Booking> loadBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        ArrayList<User> users = loadUsers();
        ArrayList<Flight> flights = loadFlights();
        ArrayList<Passenger> passengers = loadPassengers();
        
        File file = new File(BOOKINGS_FILE);
        
        // If file doesn't exist or is empty, return empty list
        if (!file.exists() || file.length() == 0) {
            System.out.println("No bookings file found or file is empty. Starting with empty bookings list.");
            return bookings;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 6) {
                        System.out.println("Warning: Invalid booking data format at line " + lineNumber + ", skipping.");
                        continue;
                    }
                    
                    // We don't use bookingReference because it's auto-generated in the constructor
                    String customerId = parts[1];
                    String flightNumber = parts[2];
                    String status = parts[3]; // Store status to set it later
                    
                    // Handle passenger IDs safely
                    String[] passengerIds = new String[0];
                    if (parts.length > 5 && !parts[5].isEmpty()) {
                        passengerIds = parts[5].split(",");
                    }
                    
                    // Handle seat selections safely
                    String[] seatClasses = new String[0];
                    if (parts.length > 6 && !parts[6].isEmpty()) {
                        seatClasses = parts[6].split(",");
                    }
                    
                    // Find customer
                    Customer customer = null;
                    for (User user : users) {
                        if (user instanceof Customer && ((Customer)user).getUserId().equals(customerId)) {
                            customer = (Customer) user;
                            break;
                        }
                    }
                    
                    if (customer == null) {
                        System.out.println("Warning: Customer with ID " + customerId + " not found for booking at line " + lineNumber + ", skipping.");
                        continue;
                    }
                    
                    // Find flight
                    Flight flight = null;
                    for (Flight f : flights) {
                        if (f.getFlightNumber().equals(flightNumber)) {
                            flight = f;
                            break;
                        }
                    }
                    
                    if (flight == null) {
                        System.out.println("Warning: Flight with number " + flightNumber + " not found for booking at line " + lineNumber + ", skipping.");
                        continue;
                    }
                    
                    // Find passengers
                    ArrayList<Passenger> bookingPassengers = new ArrayList<>();
                    for (String passengerId : passengerIds) {
                        boolean found = false;
                        for (Passenger passenger : passengers) {
                            if (passenger.getPassengerId().equals(passengerId)) {
                                bookingPassengers.add(passenger);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println("Warning: Passenger with ID " + passengerId + " not found, skipping this passenger.");
                        }
                    }
                    
                    if (bookingPassengers.isEmpty()) {
                        System.out.println("Warning: No valid passengers found for booking at line " + lineNumber + ", creating booking with empty passenger list.");
                    }
                    
                    // Create booking
                    Booking booking = new Booking(customer, flight, bookingPassengers);
                    
                    // Add seat selections
                    for (String seatClass : seatClasses) {
                        booking.getSeatSelections().add(seatClass);
                    }
                    
                    bookings.add(booking);
                } catch (Exception e) {
                    System.out.println("Warning: Error processing booking at line " + lineNumber + ": " + e.getMessage() + ", skipping.");
                }
            }
            System.out.println("Bookings loaded successfully from " + BOOKINGS_FILE);
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }

    public static void savePassengers(ArrayList<Passenger> passengers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PASSENGERS_FILE))) {
            for (Passenger passenger : passengers) {
                StringBuilder sb = new StringBuilder();
                sb.append(passenger.getPassengerId()).append("|");
                sb.append(passenger.getName()).append("|");
                sb.append(passenger.getPassportNumber()).append("|");
                sb.append(passenger.getDateOfBirth()).append("|");
                
                // Special requests
                ArrayList<String> requests = passenger.getSpecialRequests();
                for (String request : requests) {
                    sb.append(request).append(",");
                }
                if (!requests.isEmpty()) {
                    sb.deleteCharAt(sb.length() - 1); // Remove last comma
                }
                
                writer.println(sb.toString());
            }
            System.out.println("Passengers saved successfully to " + PASSENGERS_FILE);
        } catch (IOException e) {
            System.out.println("Error saving passengers: " + e.getMessage());
        }
    }

    public static ArrayList<Passenger> loadPassengers() {
        ArrayList<Passenger> passengers = new ArrayList<>();
        File file = new File(PASSENGERS_FILE);
        
        // If file doesn't exist or is empty, return empty list
        if (!file.exists() || file.length() == 0) {
            System.out.println("No passengers file found or file is empty. Starting with empty passengers list.");
            return passengers;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 4) {
                        System.out.println("Warning: Invalid passenger data format at line " + lineNumber + ", skipping.");
                        continue;
                    }
                    
                    String passengerId = parts[0];
                    String name = parts[1];
                    String passportNumber = parts[2];
                    
                    // Handle date parsing separately to catch specific exceptions
                    LocalDate dateOfBirth;
                    try {
                        dateOfBirth = LocalDate.parse(parts[3]);
                    } catch (DateTimeParseException e) {
                        System.out.println("Warning: Invalid date format at line " + lineNumber + ": " + e.getMessage() + ", skipping.");
                        continue;
                    }
                    
                    Passenger passenger = new Passenger(name, passportNumber, dateOfBirth);
                    
                    // Add special requests if any
                    if (parts.length > 4 && !parts[4].isEmpty()) {
                        String[] requests = parts[4].split(",");
                        for (String request : requests) {
                            passenger.addSpecialRequest(request);
                        }
                    }
                    
                    passengers.add(passenger);
                } catch (Exception e) {
                    System.out.println("Warning: Error processing passenger at line " + lineNumber + ": " + e.getMessage() + ", skipping.");
                }
            }
            System.out.println("Passengers loaded successfully from " + PASSENGERS_FILE);
        } catch (IOException e) {
            System.out.println("Error loading passengers: " + e.getMessage());
        }
        return passengers;
    }
}
