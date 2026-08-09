/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule;

/**
 * Represents the common information shared by all users of the Campus
 * Maker Space Co-op system.
 * This abstract class is the base product used by the Simple Factory
 * pattern. Concrete user types, including RegularMember, Trainer, and ShopTech, 
 * extend this class.
 * @author Tianzhu Li
 * @version 1.0
 */
public abstract class User {
    
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    
    /**
     * Constructs a new user that has not yet been saved to the database.
     * The initial user ID is set to 0 because the database assigns
     * the actual ID using its auto-increment column.
     *
     * @param name the full name of the user
     * @param email the unique email address of the user
     * @param passwordHash the hashed version of the user's password
     */
    public User(String name, String email, String passwordHash) {
        this.userId = 0;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    
    /**
     * Returns the user's unique database identifier.
     *
     * @return the user ID, or 0 if the user has not yet been
     * saved to the database
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Returns the user's full name.
     *
     * @return the full name of the user
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the user's email address.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Returns the hashed version of the user's password.
     *
     * @return the user's hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }
    
    /**
     * Returns a formatted description of the user.
     * The password hash is intentionally excluded to prevent sensitive
     * authentication information from being displayed.
     *
     * @return a formatted string containing the user ID, name, and email
     */
    public String getUserDetails() {
        return String.format(
                "User ID: %d%nName: %s%nEmail: %s",
                userId,
                name,
                email);
    }
}