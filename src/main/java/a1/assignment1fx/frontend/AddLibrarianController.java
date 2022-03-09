package a1.assignment1fx.frontend;

import a1.assignment1fx.App;
import a1.assignment1fx.backend.Librarian;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles logic and events for Admin scene
 *
 * @author Gaurav Saini
 *
 * @version 1.0.0
 * @since 07/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.backend.Admin
 */
public class AddLibrarianController {

    private ArrayList<Integer> currentIDs = Librarian.getUserIDList("librarian");

    @FXML
    private TextField libFname;

    @FXML
    private TextField libID;

    @FXML
    private TextField libLname;

    @FXML
    private TextField libPWCheck;

    @FXML
    private TextField libPassword;

    @FXML
    private TextField libUsername;

    @FXML
    private ListView<String> userDetails;

    @FXML
    private Text libSuccess;

    @FXML
    private Text libFail;

    /**
     * Initialize User Details
     */
    @FXML
    public void initialize() {
        userDetails.getItems().add(App.getCurrentUser().printableString());
    }

    /**
     * Adds a Librarian to the list of Librarians, checks for errors and prevents matching id adding
     * @param event unused
     */
    @FXML
    void addLibrarian(ActionEvent event) {
        libFail.setText("");
        libSuccess.setText("");

        if (libID.getText().isEmpty())
            libFail.setText("Must Insert ID");
        else if (!libID.getText().matches("\\d+"))
            libFail.setText("ID Must be a Positive Number");
        else if(currentIDs.contains(Integer.parseInt(libID.getText())))
            libFail.setText("ID Already Exists");
        else if (libFname.getText().isEmpty())
            libFail.setText("Must Insert First Name");
        else if (libLname.getText().isEmpty())
            libFail.setText("Must Insert Last Name");
        else if(libUsername.getText().isEmpty())
            libFail.setText("Must Insert Username");
        else if (libPassword.getText().isEmpty())
            libFail.setText("Must Insert Password");
        else if (libPWCheck.getText().isEmpty())
            libFail.setText("Must Confirm Password");
        else if (!libPWCheck.getText().matches("^" + libPassword.getText() + "$"))
            libFail.setText("Passwords Must Match");
        else {
            ArrayList<String> newLib = new ArrayList<>();
            newLib.add(libID.getText() + "," + libUsername.getText() + "," + libFname.getText() + "," + libLname.getText() + "," + libPassword.getText());
            currentIDs.add(Integer.parseInt(libID.getText()));
            Librarian.writeLogs(newLib, "librarian.csv", true);
            libSuccess.setText("Librarian " + libUsername.getText() + " Added Successfully");
        }
    }

    /**
     * Logs User out of the application
     * @param event unused
     * @throws IOException Unable to find view File
     */
    @FXML
    void appLogout(ActionEvent event) throws IOException {
        App.appLogout();
    }

    /**
     * Navigates page to Admin main page
     * @param event unused
     * @throws IOException Unable to find view File
     */
    @FXML
    void cancelAdd(ActionEvent event) throws IOException {
        App app = new App();
        app.changeScene("admin-view.fxml");
    }

}
