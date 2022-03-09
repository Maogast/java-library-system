package a1.assignment1fx.backend;

import a1.assignment1fx.App;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles logic and events for Student scene
 *
 * @author Nolan Miller
 *
 * @version 1.0.0
 * @since 00/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.frontend.StudentController
 */
public class Student extends User {

    private String type;

    /**
     * Default Constructor, calls super class constructor (User)
     */
    public Student() {
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
    public Student(int id, String username, String password, String firstName, String lastName) {
        super(id, username, password, firstName, lastName);
    }

    /**
     * Getter for Student Type
     * @return student type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for student type
     * @param type student type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Overloaded Constructor, calls super class constructor (User), supports Student type
     * @param id id
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     * @param type student type
     */
    public Student(int id, String username, String password, String firstName, String lastName, String type) {
        super(id, username, password, firstName, lastName);
        this.type = type;
    }

    /**
     * Getter for printable User detail string
     * @return formatted user details
     */
    @Override
    public String printableString() {
        return "Name : " + getName() + " - " + type.toUpperCase() + "\n\tID : " + getId() + " - " +" Username : " + getUsername();
    }

    /**
     * Loads log/list records from file and formats them for printing
     * mode 1 : lending history
     * mode 2 : currently borrowed list
     * mode 3 : current request list
     * default : provide file name
     * @param mode read mode
     * @param zeroMode optional filename with mode 0
     * @return formatted history list
     */
    public static ArrayList<String> loadLogs(int mode, String zeroMode) {
        ArrayList<String> records = new ArrayList<>();

        String line = switch (mode) {
            case 1 -> "lendingLog.log";
            case 2 -> "lendingList.csv";
            case 3 -> "requests.csv";
            case 4 -> "waitingList.csv";
            default -> zeroMode;
        };

        File f = new File("./src/main/resources/" + line);
        try (FileReader fr = new FileReader(f);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                boolean check = false;
                String[] details = line.split(",");
                if (details.length == 0) continue;

                if (mode == 4) {
                    line = "";
                    for (int i = 1; i < details.length; ++i) {
                        line += details[i] + (i < details.length - 1 ? "," : "");
                        if (Integer.parseInt(details[i]) == App.getCurrentUser().getId()) {
                            check = true;
                        }
                    }
                }
                if (details[0].equals(App.getCurrentUser().getUsername()) || check) {
                    line = switch (mode) {
                        case 1 -> "Username : " + details[0] + " - Title : " + details[1] +
                                "\n\tBorrowed at : " + details[2] + " - Returned at : " + details[3];
                        case 2 -> "Username : " + details[0] + " - Title : " + details[1] +
                                "\n\tBorrowed at : " + details[2];
                        case 3 -> "UserID : " + details[0] + " - ID : " + details[1] + " at : " + details[2];
                        case 4 -> "ItemID : " + details[0] + " - List : " + line;
                        default -> line;
                    };
                    records.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            line = switch (mode) {
                case 1 -> "History Log";
                case 2 -> "Borrowed Item List";
                case 3 -> "Request List";
                case 4 -> "Waiting List";
                default -> zeroMode;
            };
            App.log.severe("Unable to Read " + line + " File");
            e.printStackTrace();
        } catch (Exception e) {
            App.log.severe("Unknown Critical Error Occurred");
            e.printStackTrace();
        }


        return records;
    }
}
