package a1.assignment1fx.backend;


/**
 * Maintains Person properties such as id, name, username, and password
 * Acts as a super class for shared functionality for different levels of user
 *
 * @author Omar Hussein
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    /**
     * Default constructor, initializes object as empty
     */
    public User() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
    }

    /**
     * Constructor overload to set object from user input
     * @param id id
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     */
    public User(int id, String username, String password, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Getter for ID
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for ID
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for Username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for Username
     * @param username id
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for Password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for Password
     * @param password id
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for Name
     * @return full name
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Getter for First Name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for First Name
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for Last Name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for Last Name
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for printable User detail string
     * @return formatted user details
     */
    public String printableString() {
        String userClass = "";
        if(this instanceof Student)
            userClass = "STUDENT";
        else if (this instanceof Librarian)
            userClass = "LIBRARIAN";
        else if (this instanceof Admin)
            userClass = "ADMIN";

        return "Name : " + getName() + " - " + userClass + "\n\tID : " + id + " - " +" Username : " + username;
    }
}
