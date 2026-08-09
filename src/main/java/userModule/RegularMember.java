/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule;

/**
 * Represents a regular member of the Campus Maker Space Co-op.
 * A regular member is the standard user type created when the registration
 * form specifies code USER.
 * @author Tianzhu Li
 * @version 1.0
 */
public class RegularMember extends User {
    
    /**
     * Constructs a new regular member.
     *
     * @param name the full name of the regular member
     * @param email the unique email address of the regular member
     * @param passwordHash the hashed version of the member's password
     */
    public RegularMember(String name, String email, String passwordHash) {
        super(name, email, passwordHash);
    }
}