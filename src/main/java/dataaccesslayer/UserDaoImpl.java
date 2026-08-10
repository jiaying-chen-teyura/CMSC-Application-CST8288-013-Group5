package dataaccesslayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import transferobjects.UserDTO;

/**
 * Provides the JDBC implementation of the UserDao interface.
 *
 * This class performs user-related database operations and converts
 * database records into UserDTO objects.
 * @author Tianzhu Li
 */
public class UserDaoImpl implements UserDao {

    /**
     * Converts the current ResultSet row into a UserDTO object.
     *
     * @param resultSet the ResultSet containing user data
     * @return a UserDTO containing the mapped user information
     * @throws SQLException if the user data cannot be read
     */
    private UserDTO map(ResultSet resultSet) throws SQLException {
        UserDTO user = new UserDTO();

        user.setUserId(resultSet.getInt("user_id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getString("password_hash"));

        user.setUserType(
                UserDTO.UserType.valueOf(
                        resultSet.getString("user_type")
                )
        );

        user.setAccountStatus(
                UserDTO.AccountStatus.valueOf(
                        resultSet.getString("account_status")
                )
        );

        Timestamp createdAt = resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp lastLoginAt = resultSet.getTimestamp("last_login_at");

        if (lastLoginAt != null) {
            user.setLastLoginAt(lastLoginAt.toLocalDateTime());
        }

        return user;
    }

    /**
     * Retrieves a user using the user's unique identifier.
     *
     * @param userId the unique identifier of the user
     * @return the matching UserDTO, or null if the user is not found
     */
    @Override
    public UserDTO getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = DataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return map(resultSet);
                }

                return null;
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Unable to retrieve the user by ID.",
                    exception
            );
        }
    }

    /**
     * Retrieves a user using the user's email address.
     *
     * @param email the user's email address
     * @return the matching UserDTO, or null if the user is not found
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return map(resultSet);
                }

                return null;
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Unable to retrieve the user by email.",
                    exception
            );
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list containing all users
     */
    @Override
    public List<UserDTO> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY name";
        List<UserDTO> users = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(map(resultSet));
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Unable to retrieve all users.",
                    exception
            );
        }

        return users;
    }

    /**
     * Retrieves all users with the specified user type.
     *
     * @param type the user type used to filter the records
     * @return a list containing users with the specified type
     */
    @Override
    public List<UserDTO> getUsersByType(UserDTO.UserType type) {
        String sql
                = "SELECT * FROM users WHERE user_type = ? ORDER BY name";

        List<UserDTO> users = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, type.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(map(resultSet));
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Unable to retrieve users by type.",
                    exception
            );
        }

        return users;
    }

    /**
     * Adds a new user to the database.
     *
     * @param user the UserDTO containing the new user's information
     * @return the generated user ID, or -1 if an ID was not generated
     */
    @Override
    public int addUser(UserDTO user) {
        String sql
                = "INSERT INTO users "
                + "(name, email, password_hash, user_type) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getUserType().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys
                    = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            return -1;

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Unable to add the user.",
                    exception
            );
        }
    }

    /**
     * Updates an existing user record in the database.
     *
     * @param user the UserDTO containing the updated user information
     */
    @Override
    public void updateUser(UserDTO user) {
        String sql
                = "UPDATE users "
                + "SET name = ?, email = ?, user_type = ?, "
                + "account_status = ? "
                + "WHERE user_id = ?";

        try (Connection connection = DataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(
                    3,
                    user.getUserType().name()
            );
            statement.setString(
                    4,
                    user.getAccountStatus().name()
            );
            statement.setInt(5, user.getUserId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Unable to update the user.",
                    exception
            );
        }
    }

    /**
     * Updates the user's most recent login date and time.
     *
     * @param userId the unique identifier of the user
     */
    @Override
    public void touchLastLogin(int userId) {
        String sql
                = "UPDATE users SET last_login_at = ? "
                + "WHERE user_id = ?";

        try (Connection connection = DataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setTimestamp(
                    1,
                    Timestamp.valueOf(LocalDateTime.now())
            );
            statement.setInt(2, userId);

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Unable to update the user's last login time.",
                    exception
            );
        }
    }
}