package a1.assignment1fx.frontend;

import a1.assignment1fx.App;
import a1.assignment1fx.backend.Item;
import a1.assignment1fx.backend.Librarian;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles events for Librarian scene
 *
 * @author Omar Hussein
 *
 * @version 1.0.1
 * @since 02/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.backend.Librarian
 * @see javafx.fxml
 * @see javafx.stage
 * @see javafx.scene
 * @see javafx.collections.ObservableList
 */
public class LibrarianController {

    private static ArrayList<Integer> removedIDs = new ArrayList<>();
    private static ArrayList<Item> currentList = new ArrayList<>();

    @FXML
    private ListView<String> userDetails;

    @FXML
    private Text errorText;

    @FXML
    private TextField searchText;

    @FXML
    private ListView<String> libItem;

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
     * Navigate to requests view
     * @param event unused
     * @throws IOException Unable to find view File
     */
    @FXML
    void goRequests(ActionEvent event) throws IOException {
        App app = new App();
        app.changeScene("requests-view.fxml");
    }

    /**
     * Updates Item search list and enables multiple selection
     */
    private void updateList() {
        libItem.getItems().clear();
        libItem.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        libItem.getItems().addAll(Librarian.printableString(currentList, false));
    }

    /**
     * Updates the details of the items based on the user input
     * @param mode update mode
     * @throws IOException Unable to crate detail popup window handler
     */
    private void updateItemDetails(int mode) throws IOException {
        if (libItem.getSelectionModel().getSelectedItems().size() == 0) return;

        ArrayList<Item> match = new ArrayList<>();
        for (String s : libItem.getSelectionModel().getSelectedItems()) {

            s = s.split(" - ")[0];
            int idx = -1;

            for (Item i : currentList) {

                if(mode == 3) ++idx;
                if (i.getId() == Integer.parseInt(s)) {

                    switch (mode) {
                        case 1:
                            i.setCount(i.getCount() + 1);
                            break;
                        case 2:
                            i.setCount(i.getCount() - 1);
                            break;
                        case 4:
                            match.add(i);
                            break;
                    }
                    break;
                }
            }

            if(mode == 3 && idx >= 0) {
                removedIDs.add(currentList.get(idx).getId());
                currentList.remove(idx);
            }
        }
        if(mode == 4) Librarian.createPopup(Librarian.printableString(match, true), "Item Details");
        else {
            updateList();
            if(removedIDs.size() > 0) {
                int[] ids = new int[removedIDs.size()];
                for (int i = 0; i < removedIDs.size(); ++i)
                    ids[i] = removedIDs.get(i);

                Librarian.removeRecords("waitingList.csv", ids, 0);
                Librarian.removeRecords("requests.csv", ids, 0);
            }
            Librarian.modifyRecords(currentList, removedIDs);
        }
    }



    /**
     * Search Item records for matching id or title based on user input
     * If user input is a numeric value the function will request an ID search,
     * if user input is a string the function will request a Title search
     * @param event unused
     */
    @FXML
    void searchItems(ActionEvent event) {
        errorText.setText("");
        if (searchText.getText().isEmpty()) errorText.setText("No Search Pattern Provided");
        else {
            String type = searchText.getText().matches("\\d+") ? "ID" : "Title";
            currentList = Librarian.findItems(type.toLowerCase(), searchText.getText(), false);
            if(currentList.size() == 0) errorText.setText("No Matching " + type + " Found");
            else updateList();
        }
    }

    /**
     * Request search for all Library records
     * @param event unused
     */
    @FXML
    void findAll(ActionEvent event) {
        errorText.setText("");
        currentList = Librarian.findItems("all", "", false);
        updateList();
    }

    /**
     * Request record search by user specified category
     * @param event user category selection
     */
    @FXML
    void findCategory(ActionEvent event) {
        MenuItem mi = (MenuItem) event.getSource();
        errorText.setText("");
        currentList = Librarian.findItems("category", mi.getText(), false);
        updateList();
    }

    /**
     * Create popup window with details of User selected Items
     * @param event unused
     * @throws IOException Unable to crate detail popup window
     */
    @FXML
    void getDetails(ActionEvent event) throws IOException {
        updateItemDetails(4);
    }

    /**
     * Add copy to Item selection
     * @param event unused
     * @throws IOException Unable to crate detail popup window handler
     */
    @FXML
    void addCopy(ActionEvent event) throws IOException {
        updateItemDetails(1);
    }

    /**
     * Remove copy from Item selection
     * @param event unused
     * @throws IOException Unable to crate detail popup window handler
     */
    @FXML
    void removeCopy(ActionEvent event) throws IOException {
       updateItemDetails(2);
    }

    /**
     * Remove selected Items from record list
     * @param event unused
     * @throws IOException Unable to crate detail popup window handler
     */
    @FXML
    void removeItem(ActionEvent event) throws IOException {
        updateItemDetails(3);
    }
}
