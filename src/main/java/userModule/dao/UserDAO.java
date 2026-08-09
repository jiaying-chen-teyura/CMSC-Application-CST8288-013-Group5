/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule.dao;

import java.sql.SQLException;
import java.util.List;
import userModule.dto.UserDTO;

/**
 * Defines the database operations available for user records.
 *
 * The business layer depends on this interface instead of depending
 * directly on UserDAOImpl.
 *
 * @author Tianzhu Li
 * @version 1.0
 */
public interface UserDAO {
    
    /**
     * Finds a user using the user's database identifier.
     *
     * @param userId the user ID to search for
     * @return the matching UserDTO, or null if no user is found
     * @throws SQLException if the database query fails
     */
    UserDTO findById(int userId) throws SQLException;
    
    /**
     * Finds a user using the user's email address.
     *
     * @param email the email address to search for
     * @return the matching UserDTO, or null if no user is found
     * @throws SQLException if the database query fails
     */
    UserDTO findByEmail(String email) throws SQLException;
    
    /**
     * Returns all users stored in the database.
     *
     * @return a list containing all users
     * @throws SQLException if the database query fails
     */
    List<UserDTO> findAll() throws SQLException;
    
    /**
     * Inserts a new user into the database.
     *
     * @param user the user information to insert
     * @return true if one user was inserted; otherwise false
     * @throws SQLException if the database operation fails
     */
    boolean insert(UserDTO user) throws SQLException;
    
    /**
     * Updates an existing user in the database.
     *
     * @param user the user information containing the updated values
     * @return true if one user was updated; otherwise false
     * @throws SQLException if the database operation fails
     */
    boolean update(UserDTO user) throws SQLException;
    
    /**
     * Deletes a user using the user's database identifier.
     *
     * @param userId the ID of the user to delete
     * @return true if one user was deleted; otherwise false
     * @throws SQLException if the database operation fails
     */
    boolean delete(int userId) throws SQLException;
}