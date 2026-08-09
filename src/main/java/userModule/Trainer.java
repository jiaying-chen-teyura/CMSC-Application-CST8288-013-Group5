/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule;

/**
 * Represents a trainer in the Campus Maker Space Co-op.
 * Trainers are specialized users who can deliver safety and equipment
 * training sessions to regular members.
 * @author Tianzhu Li
 * @version 1.0
 */
public class Trainer extends User {
    
    /**
     * Constructs a new trainer.
     *
     * @param name the full name of the trainer
     * @param email the unique email address of the trainer
     * @param passwordHash the hashed version of the trainer's password
     */
    public Trainer(String name, String email, String passwordHash) {
        super(name, email, passwordHash);
    }
}