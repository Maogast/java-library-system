package a1.assignment1fx.backend;

/**
 * Handles logic and events for Admin scene
 *
 * @author Gaurav Saini
 *
 * @version 1.0.0
 * @since 07/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.frontend.AdminController
 * @see a1.assignment1fx.frontend.AddLibrarianController
 */
public class Admin extends User {
    /**
     * Default Constructor, calls super class constructor (User)
     */
    public Admin() {
        super();
    }

    /**
     * Overloaded Constructor, calls super class constructor (User)
     * @param id id
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     */
    public Admin(int id, String username, String password, String firstName, String lastName) {
        super(id, username, password, firstName, lastName);
    }
}
