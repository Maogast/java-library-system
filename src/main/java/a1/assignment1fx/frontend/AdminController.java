package a1.assignment1fx.frontend;

import a1.assignment1fx.backend.Admin;
import a1.assignment1fx.App;
import a1.assignment1fx.backend.Librarian;
import a1.assignment1fx.backend.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
public class AdminController {

    private ArrayList<User> currentList = new ArrayList<>();

    @FXML
    private Text errorText;

    @FXML
    private TextField findLib;

    @FXML
    private ListView<String> librarianList;

    @FXML
    private ListView<String> userDetails;

    /**
     * Updates Item search list and enables multiple selection
     */
    private void updateList(ArrayList<User> list) {
        errorText.setText("");
        currentList.clear();
        librarianList.getItems().clear();
        ArrayList<String> print = new ArrayList<>();
        for (User user : list) {
            currentList.add(user);
            print.add(user.printableString());
        }
        librarianList.getItems().addAll(print);
    }

    /**
     * Initialize User Details
     */
    @FXML
    public void initialize() {
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
     * Find all Librarians in the file list
     * @param event unused
     */
    @FXML
    void findAll(ActionEvent event) {
        updateList(Librarian.findAllUser("librarian"));
    }

    /**
     * navigate to add Librarian page
     * @param event unused
     * @throws IOException unable to find scene fxml file
     */
    @FXML
    void goAddLib(ActionEvent event) throws IOException {
        App app = new App();
        app.changeScene("addLib-view.fxml");
    }

    /**
     * Remove librarian from the list
     * @param event unused
     */
    @FXML
    void removeLib(ActionEvent event) {
        String removedID = librarianList.getSelectionModel().getSelectedItem().split("\n")[1].split(" ")[2];
        Librarian.removeRecord("librarian.csv", removedID, 0); ;
        for (int i = 0; i < currentList.size(); ++i)
            if (currentList.get(i).getId() == Integer.parseInt(removedID)) {
                currentList.remove(i);
                break;
            }
        updateList(new ArrayList<>(currentList));
    }

    /**
     * Search for specific username or id
     * @param event unused
     */
    @FXML
    void searchUsers(ActionEvent event) {
        User match = Librarian.findUser(findLib.getText(), "librarian");
        if(match.getId() == -1) errorText.setText("No Match Found");
        librarianList.getItems().add(match.printableString());
    }

}