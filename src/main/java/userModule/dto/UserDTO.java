/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing one record from the users table.
 * This class transfers user account data between the DAO layer and the
 * business layer. It does not contain database queries or business rules.
 * @author Tianzhu Li
 * @version 1.0
 */
public class UserDTO {
    
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String userType;
    private String accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    
    /**
     * Constructs an empty UserDTO.
     * This constructor allows the DAO layer to create an empty object
     * before assigning values obtained from a database result.
     */
    public UserDTO() {
    }
    
    /**
     * Constructs a UserDTO containing the information collected during
     * registration.
     *
     * @param name the user's full name
     * @param email the user's unique email address
     * @param passwordHash the hashed version of the user's password
     * @param userType the requested account type
     */
    public UserDTO(String name, String email, String passwordHash,
            String userType) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.accountStatus = "ACTIVE";
    }
    
    /**
     * Constructs a UserDTO containing all fields from an existing database
     * record.
     *
     * @param userId the unique database identifier
     * @param name the user's full name
     * @param email the user's unique email address
     * @param passwordHash the hashed version of the user's password
     * @param userType the user's account type
     * @param accountStatus the current account status
     * @param createdAt the account creation date and time
     * @param lastLoginAt the most recent successful login date and time
     */
    public UserDTO(int userId, String name, String email,
            String passwordHash, String userType, String accountStatus,
            LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.accountStatus = accountStatus;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }
    
    /**
     * Returns the user's database identifier.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Sets the user's database identifier.
     *
     * @param userId the user ID to assign
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Returns the user's full name.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the user's full name.
     *
     * @param name the name to assign
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the user's email address.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the user's email address.
     *
     * @param email the email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Returns the user's hashed password.
     *
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }
    
    /**
     * Sets the user's hashed password.
     *
     * @param passwordHash the password hash to assign
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    /**
     * Returns the user's account type.
     *
     * @return the user type
     */
    public String getUserType() {
        return userType;
    }
    
    /**
     * Sets the user's account type.
     *
     * @param userType the account type to assign
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    /**
     * Returns the user's current account status.
     *
     * @return the account status
     */
    public String getAccountStatus() {
        return accountStatus;
    }
    
    /**
     * Sets the user's account status.
     *
     * @param accountStatus the account status to assign
     */
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    
    /**
     * Returns the account creation date and time.
     *
     * @return the creation date and time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the account creation date and time.
     *
     * @param createdAt the creation date and time to assign
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Returns the most recent successful login date and time.
     *
     * @return the last login date and time, or null if the user
     *         has never logged in
     */
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    /**
     * Sets the most recent successful login date and time.
     *
     * @param lastLoginAt the last login date and time to assign
     */
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}