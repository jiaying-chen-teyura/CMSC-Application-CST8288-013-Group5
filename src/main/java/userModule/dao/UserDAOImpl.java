/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import userModule.dto.UserDTO;
import util.DataSource;

/**
 * JDBC implementation of UserDAO.
 *
 * This class contains the SQL operations for the users table. Business
 * rules and input validation belong in the business layer.
 *
 * @author Tianzhu Li
 * @version 1.0
 */
public class UserDAOImpl implements UserDAO {
    
    private final DataSource dataSource;
    
    /**
     * Constructs a UserDAOImpl using the shared application DataSource.
     */
    public UserDAOImpl() {
        this.dataSource = DataSource.getInstance();
    }
    
    /**
     * Finds a user using the user's database identifier.
     *
     * @param userId the user ID to search for
     * @return the matching UserDTO, or null if no user is found
     * @throws SQLException if the database query fails
     */
    @Override
    public UserDTO findById(int userId) throws SQLException {
        String sql = """
                SELECT user_id, name, email, password_hash, user_type,
                       account_status, created_at, last_login_at
                FROM users
                WHERE user_id = ?
                """;
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }
    
    /**
     * Finds a user using the user's email address.
     *
     * @param email the email address to search for
     * @return the matching UserDTO, or null if no user is found
     * @throws SQLException if the database query fails
     */
    @Override
    public UserDTO findByEmail(String email) throws SQLException {
        String sql = """
                SELECT user_id, name, email, password_hash, user_type,
                       account_status, created_at, last_login_at
                FROM users
                WHERE email = ?
                """;
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns all users stored in the database.
     *
     * @return a list containing all users
     * @throws SQLException if the database query fails
     */
    @Override
    public List<UserDTO> findAll() throws SQLException {
        String sql = """
                SELECT user_id, name, email, password_hash, user_type,
                       account_status, created_at, last_login_at
                FROM users
                ORDER BY user_id
                """;
        
        List<UserDTO> users = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        }
        
        return users;
    }
    
    /**
     * Inserts a new user into the database.
     *
     * The database generates the user ID, creation time, and initial
     * account status.
     *
     * @param user the user information to insert
     * @return true if one user was inserted; otherwise false
     * @throws SQLException if the database operation fails
     */
    @Override
    public boolean insert(UserDTO user) throws SQLException {
        String sql = """
                INSERT INTO users
                    (name, email, password_hash, user_type)
                VALUES (?, ?, ?, ?)
                """;
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getUserType());
            
            return statement.executeUpdate() == 1;
        }
    }
    
    /**
     * Updates an existing user in the database.
     *
     * The creation time and user ID are not modified.
     *
     * @param user the user information containing the updated values
     * @return true if one user was updated; otherwise false
     * @throws SQLException if the database operation fails
     */
    @Override
    public boolean update(UserDTO user) throws SQLException {
        String sql = """
                UPDATE users
                SET name = ?,
                    email = ?,
                    password_hash = ?,
                    user_type = ?,
                    account_status = ?,
                    last_login_at = ?
                WHERE user_id = ?
                """;
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getUserType());
            statement.setString(5, user.getAccountStatus());
            
            if (user.getLastLoginAt() == null) {
                statement.setNull(6, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        6,
                        Timestamp.valueOf(user.getLastLoginAt())
                );
            }
            
            statement.setInt(7, user.getUserId());
            
            return statement.executeUpdate() == 1;
        }
    }
    
    /**
     * Deletes a user using the user's database identifier.
     *
     * @param userId the ID of the user to delete
     * @return true if one user was deleted; otherwise false
     * @throws SQLException if the database operation fails
     */
    @Override
    public boolean delete(int userId) throws SQLException {
        String sql = """
                DELETE FROM users
                WHERE user_id = ?
                """;
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement
                = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            
            return statement.executeUpdate() == 1;
        }
    }
    
    /**
     * Converts the current ResultSet row into a UserDTO.
     *
     * @param resultSet the ResultSet positioned at a user record
     * @return a UserDTO containing the current row
     * @throws SQLException if a column cannot be read
     */
    private UserDTO mapUser(ResultSet resultSet) throws SQLException {
        Timestamp createdTimestamp
                = resultSet.getTimestamp("created_at");
        Timestamp lastLoginTimestamp
                = resultSet.getTimestamp("last_login_at");
        
        return new UserDTO(
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password_hash"),
                resultSet.getString("user_type"),
                resultSet.getString("account_status"),
                createdTimestamp == null
                        ? null : createdTimestamp.toLocalDateTime(),
                lastLoginTimestamp == null
                        ? null : lastLoginTimestamp.toLocalDateTime()
        );
    }
}