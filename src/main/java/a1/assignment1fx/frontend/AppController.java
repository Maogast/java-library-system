package a1.assignment1fx.frontend;

import a1.assignment1fx.backend.Admin;
import a1.assignment1fx.App;
import a1.assignment1fx.backend.Librarian;
import a1.assignment1fx.backend.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * Handles events, errors, and login checking
 *
 * @author Omar Hussein
 * @author Nolan Miller
 * @author Gaurav Saini
 *
 * @version 1.0.1
 * @since 02/03/2022
 *
 * @see a1.assignment1fx.App
 * @see javafx.event
 * @see javafx.fxml
 * @see javafx.scene
 */
public class AppController {
    @FXML
    private Label bad_login;

    @FXML
    private PasswordField password;

    @FXML
    private RadioButton admin, librarian, student;

    @FXML
    private TextField username;

    /**
     * Performs error checking on login submission event
     * Clears error message, Checks for form input, Checks for radio button input, Checks for valid user credentials
     * @param event unused
     */
    @FXML
    void checkLogin(ActionEvent event) {

        String errorText = "";
        bad_login.setText(errorText);

        if(username.getText().isEmpty()) {
            bad_login.setText("Enter Username");
            return;
        }
        else if(password.getText().isEmpty()) {
            bad_login.setText("Enter Password");
            return;
        }

        String filename = "";
        if(admin.isSelected()) {
            filename = "admin";
            App.setCurrentUser(new Admin());
        }
        else if (librarian.isSelected()) {
            filename = "librarian";
            App.setCurrentUser(new Librarian());
        }
        else if(student.isSelected()) {
            filename = "student";
            App.setCurrentUser(new Student());
        }
        else {
            bad_login.setText("Please Choose Your Login Type");
            return;
        }


        switch (App.checkLogin(username.getText(), password.getText(), filename)) {
            case 1 :
                bad_login.setText("No Such " + filename + " Username");
                break;
            case 2:
                bad_login.setText("Incorrect Password");
                break;
            case 3:
                bad_login.setText("Unable to Complete Login");
                break;
        }
    }
}