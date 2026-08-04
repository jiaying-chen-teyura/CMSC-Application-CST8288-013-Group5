/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package userModule;

/**
 * Demonstrates the User Simple Factory.
 *
 * @author Tianzhu Li
 */
public class UserFactoryTest {
    
    /**
     * Creates each supported user type through UserFactory.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        
        User member = UserFactory.createUser(
                "user",
                "Jane Member",
                "member@example.com",
                "memberHash"
        );
        
        User trainer = UserFactory.createUser(
                "trainer",
                "John Trainer",
                "trainer@example.com",
                "trainerHash"
        );
        
        User shopTech = UserFactory.createUser(
                "shop_tech",
                "Alex Tech",
                "tech@example.com",
                "techHash"
        );
        
        System.out.println(member.getClass().getSimpleName());
        System.out.println(member.getUserDetails());
        
        System.out.println();
        
        System.out.println(trainer.getClass().getSimpleName());
        System.out.println(trainer.getUserDetails());
        
        System.out.println();
        
        System.out.println(shopTech.getClass().getSimpleName());
        System.out.println(shopTech.getUserDetails());
    }
}