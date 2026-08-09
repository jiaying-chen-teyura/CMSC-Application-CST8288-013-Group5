package transferobjects;

import java.time.LocalDateTime;

/**
 * Represents a user record transferred between the Data, Business,
 * and Presentation layers.
 *
 * This class stores user account information, including authentication
 * details, account type, account status, and login timestamps.
 */
public class UserDTO {

    /**
     * Defines the available user types in the CMSC system.
     */
    public enum UserType {
        USER,
        TRAINER,
        SHOP_TECH
    }

    /**
     * Defines the possible statuses of a user account.
     */
    public enum AccountStatus {
        ACTIVE,
        SUSPENDED,
        INACTIVE
    }

    private Integer userId;
    private String name;
    private String email;
    private String passwordHash;
    private UserType userType;
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    /**
     * Creates an empty UserDTO.
     */
    public UserDTO() {
    }

    /**
     * Creates a UserDTO with the information required to register a user.
     *
     * @param name the user's full name
     * @param email the user's email address
     * @param passwordHash the user's hashed password
     * @param userType the type of user account
     */
    public UserDTO(String name, String email, String passwordHash,
            UserType userType) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
    }

    /**
     * Returns the user's unique identifier.
     *
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the user's unique identifier.
     *
     * @param userId the user ID
     */
    public void setUserId(Integer userId) {
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
     * @param name the user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's email address.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's hashed password.
     *
     * @return the hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the user's hashed password.
     *
     * @param passwordHash the hashed password
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the user's account type.
     *
     * @return the user type
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Sets the user's account type.
     *
     * @param userType the user type
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Returns the current status of the user account.
     *
     * @return the account status
     */
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the status of the user account.
     *
     * @param accountStatus the account status
     */
    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    /**
     * Returns the date and time when the account was created.
     *
     * @return the account creation date and time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the date and time when the account was created.
     *
     * @param createdAt the account creation date and time
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the date and time of the user's most recent login.
     *
     * @return the last login date and time
     */
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    /**
     * Sets the date and time of the user's most recent login.
     *
     * @param lastLoginAt the last login date and time
     */
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    /**
     * Determines whether the user is a trainer.
     *
     * @return true if the user is a trainer; otherwise false
     */
    public boolean isTrainer() {
        return userType == UserType.TRAINER;
    }

    /**
     * Determines whether the user is a Shop-Tech.
     *
     * @return true if the user is a Shop-Tech; otherwise false
     */
    public boolean isShopTech() {
        return userType == UserType.SHOP_TECH;
    }
}