import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Administrator extends User {
    private String adminId;
    private int securityLevel;
    private static final String LOG_FILE = "system_logs.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Administrator(String username, String password, String name, String email, String contactInfo,
                        int securityLevel) {
        super(username, password, name, email, contactInfo);
        this.adminId = "AD" + System.currentTimeMillis();
        this.securityLevel = securityLevel;
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

    public User createUser(String username, String password, String name, String email, String contactInfo,
                         String userType, String additionalInfo) {
        if (!super.validatePassword(password)) {
            System.out.println("Invalid password.");
            return null;
        }
        
        switch (userType) {
            case "Customer":
                return new Customer(username, password, name, email, contactInfo, additionalInfo);
            case "Agent":
                return new Agent(username, password, name, email, contactInfo, additionalInfo, 0.05);
            case "Administrator":
                return new Administrator(username, password, name, email, contactInfo, 5);
            default:
                System.out.println("Invalid user type.");
                return null;
        }
    }

    public void modifySystemSettings(String setting, String value) {
        // Implement system settings modification
    }

    public ArrayList<String> viewSystemLogs() {
        ArrayList<String> logs = new ArrayList<>();
        File logFile = new File(LOG_FILE);
        
        if (!logFile.exists()) {
            logs.add("No system logs found.");
            return logs;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
            
            // Add a log entry for this viewing action
            logSystemAction("Administrator " + this.getName() + " viewed system logs");
            
            return logs;
        } catch (IOException e) {
            logs.add("Error reading system logs: " + e.getMessage());
            return logs;
        }
    }
    
    /**
     * Adds a log entry to the system log file
     * @param action The action to log
     */
    public void logSystemAction(String action) {
        try {
            File logFile = new File(LOG_FILE);
            boolean newFile = !logFile.exists();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                // If it's a new file, add a header
                if (newFile) {
                    writer.write("===== FLIGHT MANAGEMENT SYSTEM LOGS =====\n");
                }
                
                // Format: [DateTime] Action
                String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
                writer.write("[" + timestamp + "] " + action + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to system log: " + e.getMessage());
        }
    }

    /**
     * Manages user access by changing their role
     * @param user The user to modify
     * @param newRole The new role for the user ("Customer", "Agent", or "Administrator")
     * @return A new User object with the updated role, or null if the operation failed
     */
    public User manageUserAccess(User user, String newRole) {
        if (user == null) {
            System.out.println("Invalid user.");
            return null;
        }
        
        if (!hasPermission()) {
            System.out.println("Insufficient permissions to change user roles.");
            return null;
        }
        
        // Get user's basic information
        String username = user.getUsername();
        String password = user.getPassword();
        String name = user.getName();
        String email = user.getEmail();
        String contactInfo = user.getContactInfo();
        
        User newUser = null;
        
        // Create a new user with the appropriate role
        switch (newRole) {
            case "Customer":
                String address = "";
                if (user instanceof Customer) {
                    address = ((Customer) user).getAddress();
                }
                newUser = new Customer(username, password, name, email, contactInfo, address);
                break;
                
            case "Agent":
                String department = "Sales"; // Default department
                double commissionRate = 0.05; // Default commission rate
                
                if (user instanceof Agent) {
                    department = ((Agent) user).getDepartment();
                    commissionRate = ((Agent) user).getCommission();
                }
                
                newUser = new Agent(username, password, name, email, contactInfo, department, commissionRate);
                break;
                
            case "Administrator":
                int securityLevel = 1; // Default security level
                
                if (user instanceof Administrator) {
                    securityLevel = ((Administrator) user).getSecurityLevel();
                } else {
                    // New administrators start at level 1
                    securityLevel = 1;
                }
                
                newUser = new Administrator(username, password, name, email, contactInfo, securityLevel);
                break;
                
            default:
                System.out.println("Invalid role specified. Use 'Customer', 'Agent', or 'Administrator'.");
                return null;
        }
        
        // Log the role change
        logSystemAction("Administrator " + this.getName() + " changed user " + username + "'s role to " + newRole);
        
        return newUser;
    }
    
    /**
     * Checks if the administrator has permission to manage user access
     * @return true if the administrator has sufficient security level
     */
    private boolean hasPermission() {
        // Administrators with security level 3 or higher can manage user roles
        return securityLevel >= 3;
    }

    // Getters and Setters
    public String getAdminId() { return adminId; }
    public int getSecurityLevel() { return securityLevel; }

    public void setSecurityLevel(int securityLevel) { this.securityLevel = securityLevel; }
}
