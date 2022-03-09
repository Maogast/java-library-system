package a1.assignment1fx.frontend;

import a1.assignment1fx.App;
import a1.assignment1fx.backend.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles logic and events for Student Borrowed scene
 *
 * @author Nolan Miller
 *
 * @version 1.0.0
 * @since 06/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.backend.Student
 */
public class StudentCheckedController {

    @FXML
    private ListView<String> userDetails;

    @FXML
    private ListView<String> borrowList;

    @FXML
    private Label listLabel;

    @FXML
    private Text viewListError;

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
     * Update view list and set label text, refer to Librarian.loadLogs()
     * @param text label
     * @param mode mode
     * @param zeroMode optional
     */
    private void updateListView(String text, int mode, String zeroMode) {
        borrowList.getItems().clear();
        listLabel.setText(text);
        borrowList.getItems().addAll(Student.loadLogs(mode, zeroMode));
    }


    /**
     * Initialize User Details
     */
    @FXML
    public void initialize() {
        borrowList.getItems().addAll(Student.loadLogs(2, ""));
        userDetails.getItems().add(App.getCurrentUser().printableString());
    }

    /**
     * Clears error text placeholders
     */
    private void clearErrors() {
        viewListError.setText("");
    }

    /**
     * Load currently borrowed list of items
     * @param event unused
     */
    @FXML
    void loadBorrowed(ActionEvent event) {
        clearErrors();
        updateListView("Borrowed Items", 2, "");
    }

    /**
     * Load lending history log
     * @param event unused
     */
    @FXML
    void loadHistory(ActionEvent event) {
        clearErrors();
        updateListView("History", 1, "");
    }

    /**
     * Loads waiting list
     * @param event unused
     */
    @FXML
    void loadWait(ActionEvent event) {
        clearErrors();
        updateListView("Wait", 4, "");
    }

    /**
     * Generates popup window to view list of current waiting list for Item
     * @param event unused
     * @throws IOException Unable to create popup window
     */
    @FXML
    void viewList(ActionEvent event) throws  IOException{
        if(borrowList.getSelectionModel().getSelectedItem() == null)
            return;
        ArrayList<String> users = new ArrayList<>();
        for (String s : borrowList.getSelectionModel().getSelectedItem().split(" : ")[2].split(","))
            users.add(Librarian.findUser(s, "student").printableString());

        Librarian.createPopup(users, "Waiting List");
    }

    /**
     * Navigate to Student view
     * @param event unused
     * @throws IOException Unable to find view File
     */
    @FXML
    void goBack(ActionEvent event) throws IOException {
        App app = new App();
        app.changeScene("student-view.fxml");
    }
}
