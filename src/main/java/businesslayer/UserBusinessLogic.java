package businesslayer;

import dataaccesslayer.UserDao;
import dataaccesslayer.UserDaoImpl;
import java.util.List;
import transferobjects.UserDTO;

/**
 * Provides business logic for user registration, authentication,
 * and user-data retrieval.
 *
 * This class validates user input before sending user information
 * to the Data Access Layer.
 *
 * @author Tianzhu Li
 */
public class UserBusinessLogic {

    private final UserDao userDao;

    /**
     * Creates a UserBusinessLogic using the default UserDaoImpl.
     */
    public UserBusinessLogic() {
        this(new UserDaoImpl());
    }

    /**
     * Creates a UserBusinessLogic using the specified UserDao.
     *
     * This constructor allows a different UserDao implementation to be
     * supplied when testing or extending the application.
     *
     * @param userDao the UserDao used for database operations
     */
    public UserBusinessLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Registers a new user after validating the supplied information.
     *
     * The password is hashed before the user is stored in the database.
     *
     * @param name the user's full name
     * @param email the user's email address
     * @param plainPassword the user's plain-text password
     * @param type the selected user type
     * @return the newly registered UserDTO
     * @throws ValidationException if the supplied information is invalid
     */
    public UserDTO register(
            String name,
            String email,
            String plainPassword,
            UserDTO.UserType type) throws ValidationException {

        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required.");
        }

        if (email == null
                || !email.matches(
                        "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {

            throw new ValidationException(
                    "A valid email is required."
            );
        }

        if (plainPassword == null
                || plainPassword.length() < 6) {

            throw new ValidationException(
                    "Password must be at least 6 characters."
            );
        }

        if (type == null) {
            throw new ValidationException(
                    "A user type must be selected."
            );
        }

        if (userDao.getUserByEmail(email) != null) {
            throw new ValidationException(
                    "An account with that email already exists."
            );
        }

        UserDTO user = new UserDTO(
                name,
                email,
                PasswordUtil.hash(plainPassword),
                type
        );

        int generatedUserId = userDao.addUser(user);
        user.setUserId(generatedUserId);

        return user;
    }

    /**
     * Authenticates a user using an email address and password.
     *
     * The account must exist, the password must match, and the account
     * status must be active.
     *
     * @param email the user's email address
     * @param plainPassword the user's plain-text password
     * @return the authenticated UserDTO
     * @throws ValidationException if authentication fails
     */
    public UserDTO login(
            String email,
            String plainPassword) throws ValidationException {

        UserDTO user = userDao.getUserByEmail(email);

        if (user == null
                || !PasswordUtil.matches(
                        plainPassword,
                        user.getPasswordHash())) {

            throw new ValidationException(
                    "Invalid email or password."
            );
        }

        if (user.getAccountStatus()
                != UserDTO.AccountStatus.ACTIVE) {

            throw new ValidationException(
                    "This account is not active. "
                    + "Contact a Shop-Tech."
            );
        }

        userDao.touchLastLogin(user.getUserId());

        return user;
    }

    /**
     * Retrieves a user using the user's unique identifier.
     *
     * @param userId the unique identifier of the user
     * @return the matching UserDTO, or null if the user is not found
     */
    public UserDTO getById(int userId) {
        return userDao.getUserById(userId);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list containing all users
     */
    public List<UserDTO> getAllUsers() {
        return userDao.getAllUsers();
    }
}