package a1.assignment1fx.frontend;

import a1.assignment1fx.App;
import a1.assignment1fx.backend.Librarian;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles events for Requests scene
 *
 * @author Omar Hussein
 *
 * @version 1.0.1
 * @since 02/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.backend.Librarian
 * @see javafx.fxml
 * @see javafx.scene
 */

public class RequestsController {

    @FXML
    private ListView<String> userDetails;

    @FXML
    private ListView<String> borrowList;

    @FXML
    private ComboBox<String> itemChoice;

    @FXML
    private Label listLabel;

    @FXML
    private Text returnItemError;

    @FXML
    private Text viewListError;

    /**
     * Update current request list
     */
    private void updateItemChoice() {
        itemChoice.getItems().clear();
        itemChoice.setPromptText("Choose Item to Release/Reject");
        itemChoice.getItems().addAll(Librarian.loadLogs(3, ""));
    }

    /**
     * Get User selection and process request, refer to Librarian.processRequest()
     * @param type is release
     */
    private void processRequest(boolean type) {
        Librarian.processRequest(itemChoice.getSelectionModel().getSelectedItem(), type);
        updateItemChoice();
    }

    /**
     * Clears error text placeholders
     */
    private void clearErrors() {
        returnItemError.setText("");
        viewListError.setText("");
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
        borrowList.getItems().addAll(Librarian.loadLogs(mode, zeroMode));
    }

    /**
     * Initialize lists to reflect current Library record status and set User details
     */
    @FXML
    public void initialize() {
        updateItemChoice();
        borrowList.getItems().addAll(Librarian.loadLogs(2, ""));
        userDetails.getItems().add(App.getCurrentUser().printableString());
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
     * Navigate to add item view
     * @param event unused
     * @throws IOException Unable to find view File
     */
    @FXML
    void goAddItem(ActionEvent event) throws IOException {
        App app = new App();
        app.changeScene("addItem-view.fxml");
    }

    /**
     * Navigate to Librarian management view
     * @param event unused
     * @throws IOException Unable to find view File
     */
    @FXML
    void goManage(ActionEvent event) throws IOException {
        App app = new App();
        app.changeScene("librarian-view.fxml");
    }

    /**
     * Rejects a Student request to borrow an Item
     * @param event unused
     */
    @FXML
    void rejectItem(ActionEvent event) {
        processRequest(false);
    }

    /**
     * Releases a Student request to borrow an Item
     * @param event unused
     */
    @FXML
    void releaseItem(ActionEvent event) {
        processRequest(true);
        updateListView("Borrowed Items", 2, "");
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
        updateListView("Lending History", 1, "");
    }

    /**
     * Loads waiting list
     * @param event unused
     */
    @FXML
    void loadWaiting(ActionEvent event) {
        clearErrors();
        updateListView("Waiting List", 4, "");
    }

    /**
     * Returns Item from Student to Library
     * @param event unused
     */
    @FXML
    void returnItem(ActionEvent event) {
        clearErrors();
        if(!listLabel.getText().matches("Borrowed Items")) {
            returnItemError.setText("Load Borrowed Items");
            return;
        }
        if(borrowList.getSelectionModel().getSelectedItem() == null) {
            returnItemError.setText("No Item Selected");
            return;
        }
        Librarian.returnItem(borrowList.getSelectionModel().getSelectedItem());
        updateItemChoice();
        updateListView("Borrowed Items", 2, "");
    }

    /**
     * Generates popup window to view list of current waiting list for Item
     * @param event unused
     * @throws IOException Unable to create popup window
     */
    @FXML
    void viewList(ActionEvent event) throws IOException {
        clearErrors();
        if(!listLabel.getText().matches("Waiting List")) {
            viewListError.setText("Load Waiting List");
            return;
        }
        if(borrowList.getSelectionModel().getSelectedItem() == null) {
            viewListError.setText("No Item Selected");
            return;
        }
        ArrayList<String> users = new ArrayList<>();
        for (String s : borrowList.getSelectionModel().getSelectedItem().split(" : ")[2].split(","))
            users.add(Librarian.findUser(s, "student").printableString());

        Librarian.createPopup(users, "Waiting List");
    }
}
