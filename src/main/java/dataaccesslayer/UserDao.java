package dataaccesslayer;

import java.util.List;
import transferobjects.UserDTO;

/**
 * Defines the database operations available for user records.
 *
 * The implementation of this interface is responsible for performing
 * CRUD and user-related database operations without exposing SQL to the
 * Business Layer.
 * @author Tianzhu Li
 */
public interface UserDao {

    /**
     * Retrieves a user using the user's unique identifier.
     *
     * @param userId the unique identifier of the user
     * @return the matching UserDTO, or null if the user is not found
     */
    UserDTO getUserById(int userId);

    /**
     * Retrieves a user using the user's email address.
     *
     * @param email the user's email address
     * @return the matching UserDTO, or null if the user is not found
     */
    UserDTO getUserByEmail(String email);

    /**
     * Retrieves all users from the database.
     *
     * @return a list containing all users
     */
    List<UserDTO> getAllUsers();

    /**
     * Retrieves all users with the specified user type.
     *
     * @param type the user type used to filter the records
     * @return a list containing users with the specified type
     */
    List<UserDTO> getUsersByType(UserDTO.UserType type);

    /**
     * Adds a new user to the database.
     *
     * @param user the UserDTO containing the new user's information
     * @return the generated user ID, or -1 if an ID was not generated
     */
    int addUser(UserDTO user);

    /**
     * Updates an existing user record in the database.
     *
     * @param user the UserDTO containing the updated user information
     */
    void updateUser(UserDTO user);

    /**
     * Updates the user's most recent login date and time.
     *
     * @param userId the unique identifier of the user
     */
    void touchLastLogin(int userId);
}