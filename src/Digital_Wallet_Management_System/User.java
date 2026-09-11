package Digital_Wallet_Management_System;

public class User {

    private String userId;
    private String name;
    private String email;
    private String mobileNumber;
    private String password;

    // LOGIN SECURITY
    private int failedLoginAttempts;
    private boolean accountLocked;
    private long lockedUntil;

    public User(String userId, String name, String email,
                String mobileNumber, String password) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;

        // Default security values
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
        this.lockedUntil = 0;
    }

    // GETTERS

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public long getLockedUntil() {
        return lockedUntil;
    }

    // SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setLockedUntil(long lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}