import java.util.ArrayList;
import java.time.LocalDate;

public class Passenger {
    private String passengerId;
    private String name;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private ArrayList<String> specialRequests;

    public Passenger(String name, String passportNumber, LocalDate dateOfBirth) {
        this.passengerId = "P" + System.currentTimeMillis();
        this.name = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.specialRequests = new ArrayList<>();
    }

    public boolean validatePassengerData() {
        if (name == null || name.isEmpty()) {
            System.out.println("Name is required.");
            return false;
        }
        
        if (passportNumber == null || passportNumber.isEmpty()) {
            System.out.println("Passport number is required.");
            return false;
        }
        
        if (dateOfBirth == null) {
            System.out.println("Date of birth is required.");
            return false;
        }
        
        return true;
    }

    public void updateInfo(String name, String passportNumber, LocalDate dateOfBirth) {
        if (!validatePassengerData()) {
            return;
        }
        
        this.name = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public ArrayList<String> getPassengerDetails() {
        ArrayList<String> details = new ArrayList<>();
        details.add("Passenger ID: " + passengerId);
        details.add("Name: " + name);
        details.add("Passport: " + passportNumber);
        details.add("Date of Birth: " + dateOfBirth);
        if (!specialRequests.isEmpty()) {
            details.add("Special Requests: " + String.join(", ", specialRequests));
        }
        return details;
    }

    public void addSpecialRequest(String request) {
        if (request != null && !request.isEmpty()) {
            specialRequests.add(request);
        }
    }

    public void removeSpecialRequest(String request) {
        specialRequests.remove(request);
    }

    // Getters
    public String getPassengerId() { return passengerId; }
    public String getName() { return name; }
    public String getPassportNumber() { return passportNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public ArrayList<String> getSpecialRequests() { return specialRequests; }

    // Setters
    public void setSpecialRequests(ArrayList<String> specialRequests) { this.specialRequests = specialRequests; }
}
