package a1.assignment1fx;

import a1.assignment1fx.backend.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;
import java.util.logging.*;

/**
 * Handles the main loop, runs the Library engine for the app and launches UI
 *
 * @author Omar Hussein
 * @author Nolan Miller
 * @author Gaurav Saini
 *
 * @version 1.0.2
 * @since 02/03/2022
 *
 * @see a1.assignment1fx.frontend.AppController
 * @see javafx.application
 * @see javafx.fxml
 * @see javafx.stage
 * @see javafx.stage
 * @see java.io
 * @see java.util.Objects
 * @see java.util.logging
 */
public class App extends Application {

    private static Stage appStage;
    private static User currentUser;

    /**
     * Global Logger Object, allows logging anywhere in the application
     */
    public static Logger log;

    /**
     * Overridden function, Sets up login page
     * @param stage Main stage
     * @throws IOException Unable to locate main App class
     */
    @Override
    public void start(Stage stage) throws IOException {
        LogManager.getLogManager().reset();
        log = Logger.getAnonymousLogger();
        log.setLevel(Level.ALL);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new SimpleFormatter());
        ch.setLevel(Level.ALL);

        FileHandler fh = new FileHandler("./src/main/resources/appLog.log",true);
        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.WARNING);

        log.addHandler(ch);
        log.addHandler(fh);

        appStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainPage-view.fxml"));
        stage.setTitle("Library System");
        allowResize(false);
        stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
        stage.show();

    }

    /**
     * Switches the application scene to fxml file at specified path
     * @param fxml Scene filename
     * @throws IOException Unable to locate scene fxml file
     */
    public void changeScene(String fxml) throws IOException {
        if (appStage != null) {
            Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            appStage.getScene().setRoot(pane);
            log.finest("Switching Scene");
        }
    }

    /**
     * Switch scene state to enable or disable resizing
     * @param state allow resize
     */
    public static void allowResize(boolean state) {
        if(appStage != null) {
            appStage.setResizable(state);
            log.finest((state ? "Enabled" : "Disabled") + " Stage Resizing");
        }
    }

    /**
     * Logs user out and returns to home login page
     * @throws IOException Unable to locate scene fxml file
     */
    public static void appLogout() throws IOException {
        App app = new App();
        App.allowResize(false);
        app.changeScene("mainPage-view.fxml");
        log.fine("User " + currentUser.getId() + "- " + currentUser.getUsername() + " Logged out");
        currentUser = null;
    }

    /**
     * Getter for current logged-in user
     * @return current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Setter for current logged-in user
     * @param currentUser new current user
     */
    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    /**
     * Checker user credentials and validates their login
     * Scans file system for user match, Scans entire file associated to user class, Checks for username match,
     * Checks for password match, Saves current user as matched user
     *
     * @param username username
     * @param password password
     * @param filename user class type
     * @return status code
     */
    public static int checkLogin(String username, String password, String filename) {

        int status = 0;
        String[] userInfo;
        File f = new File("./src/main/resources/" + filename + ".csv");
        try (FileReader fr = new FileReader(f);
             BufferedReader bw = new BufferedReader(fr)) {

            String line;
            while ((line = bw.readLine()) != null) {

                userInfo = line.split(",");
                if(userInfo.length == 0) continue;

                if (userInfo[1].equals(username)) {

                    if (userInfo[4].equals(password)) {

                        try {
                            if(filename.matches("^student$"))
                                setUserData(Integer.parseInt(userInfo[0]), userInfo[1], userInfo[4], userInfo[2], userInfo[3], userInfo[5]);
                            else setUserData(Integer.parseInt(userInfo[0]), userInfo[1], userInfo[4], userInfo[2], userInfo[3], "");
                            status = 0;
                        }
                        catch (NumberFormatException e) {
                            App.log.severe("Login Credential File Partially Corrupted");
                            e.printStackTrace();
                            return 3;
                        }
                        catch (Exception e) {
                            App.log.severe("Unknown Critical Error Occurred");
                            e.printStackTrace();
                            return 3;
                        }

                        try {
                            App app = new App();
                            App.allowResize(true);
                            app.changeScene(filename + "-view.fxml");
                        }
                        catch (Exception e) {
                            App.log.info("No Stage to Setup (Test Mode)");
                        }

                    } else status = 2;

                    break;

                } else status = 1;
            }

        } catch (FileNotFoundException e) {
            App.log.severe(filename + ".csv has Possibly been Corrupted. Unable to Recover Login Info.");
            e.printStackTrace();
        } catch (Exception e) {
            App.log.severe("Unknown Critical Error Occurred");
            e.printStackTrace();
        }

        switch (status) {
            case 0 -> App.log.fine(filename.toUpperCase() + " User " + username + " Logged in Successfully");
            case 1 -> App.log.info("No Matching Username Found in " + filename.toUpperCase() + " Records : " + username);
            case 2 -> App.log.warning("Invalid Password Attempt for " + filename.toUpperCase() + " User " + username);
        }

        return status;
    }

    /**
     * Set current user data to input from file
     * @param id id
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     */
    private static void setUserData(int id, String username, String password, String firstName, String lastName, String type) {
        App.getCurrentUser().setId(id);
        App.getCurrentUser().setUsername(username);
        App.getCurrentUser().setPassword(password);
        App.getCurrentUser().setFirstName(firstName);
        App.getCurrentUser().setLastName(lastName);
        if (!type.isBlank()) ((Student)App.getCurrentUser()).setType(type);
    }

    /**
     * Launches Library Manager GUI application
     * @param args unused
     */
    public static void main(String[] args) { launch(); }

    /**
     * Helper to generate test data
     * @param start min (inclusive)
     * @param end max (inclusive)
     * @return random int
     */
    private static int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    /**
     * Helper to generate test data
     * @param startYear min (inclusive)
     * @param endYear max (inclusive)
     * @return random date string
     */
    private static String createRandomDate(int startYear, int endYear) {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 3);
        int year = createRandomIntBetween(startYear, endYear);
        return year + "/" + (month < 10 ? "0"+month : month) +"/"+ (day < 10 ? "0"+day : day);
    }
}
