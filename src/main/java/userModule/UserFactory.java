/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule;

/**
 * Simple Factory responsible for creating the appropriate concrete User object.
 * The factory centralizes user creation so that controllers do not need
 * to directly instantiate RegularMember, Trainer, or ShopTech. 
 * This prevents user-type selection logic from being duplicated throughout 
 * the application.
 * @author Tianzhu Li
 * @version 1.0
 */
public class UserFactory {

    /**
     * Creates a concrete user object based on the requested account type.
     * Supported values are USER, TRAINER, and SHOP_TECH. 
     * The comparison is case-insensitive.
     * @param type the requested account type
     * @param name the full name of the user
     * @param email the unique email address of the user
     * @param passwordHash the hashed version of the user's password
     * @return a RegularMember, Trainer, or ShopTech, depending on type
     * @throws IllegalArgumentException if type is null or does not
     *         match a supported account type
     */
    public static User createUser(String type, String name,
            String email, String passwordHash) {

        if (type == null) {
            throw new IllegalArgumentException(
                    "User type must not be null.");
        }
        
        switch (type.toLowerCase()) {
            case "user":
                return new RegularMember(name, email, passwordHash);
                
            case "trainer":
                return new Trainer(name, email, passwordHash);
                
            case "shop_tech":
                return new ShopTech(name, email, passwordHash);
                
            default:
                throw new IllegalArgumentException(
                        "Unknown user type: " + type);
        }
    }
}