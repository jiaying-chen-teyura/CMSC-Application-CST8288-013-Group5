/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule;

/**
 * Represents a Shop-Tech in the Campus Maker Space Co-op.
 * Shop-Techs are specialized users responsible for equipment,
 * maintenance activities, and work orders.
 * @author Tianzhu Li
 * @version 1.0
 */
public class ShopTech extends User {
    
    /**
     * Constructs a new Shop-Tech.
     *
     * @param name the full name of the Shop-Tech
     * @param email the unique email address of the Shop-Tech
     * @param passwordHash the hashed version of the Shop-Tech's password
     */
    public ShopTech(String name, String email, String passwordHash) {
        super(name, email, passwordHash);
    }
}