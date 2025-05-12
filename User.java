public abstract class User {
    private String userId;
    private String username;
    private String password;
    private String name;
    private String email;
    private String contactInfo;

    public User(String username, String password, String name, String email, String contactInfo) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.contactInfo = contactInfo;
        this.userId = generateUserId();
    }

    private String generateUserId() {
        return "U" + System.currentTimeMillis();
    }

    public boolean validatePassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasNumber = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasNumber = true;
        }
        
        return hasLetter && hasNumber;
    }

    public abstract boolean login(String username, String password);
    
    public void logout() {
        System.out.println("You have been logged out.");
    }
    
    public void updateProfile(String name, String email, String contactInfo) {
        if (name != null && !name.isEmpty()) {
            setName(name);
        }
        if (email != null && !email.isEmpty()) {
            setEmail(email);
        }
        if (contactInfo != null && !contactInfo.isEmpty()) {
            setContactInfo(contactInfo);
        }
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getContactInfo() { return contactInfo; }

    // Setters
    protected void setUsername(String username) { this.username = username; }
    protected void setPassword(String password) { this.password = password; }
    protected void setName(String name) { this.name = name; }
    protected void setEmail(String email) { this.email = email; }
    protected void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
