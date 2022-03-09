package a1.assignment1fx.frontend;

import a1.assignment1fx.App;
import a1.assignment1fx.backend.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Handles logic and events for Student scene
 *
 * @author Nolan Miller
 *
 * @version 1.0.0
 * @since 06/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.backend.Student
 */
public class StudentController {
    private static ArrayList<Item> currentList = new ArrayList<>();

    @FXML
    private ListView<String> userDetails;

    @FXML
    private Text errorText;

    @FXML
    private TextField searchText;

    @FXML
    private ListView<String> libItem;

    private ArrayList<Integer> requestedIds = new ArrayList<>();

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
     * Updates Item search list and enables multiple selection
     */
    private void updateList() {
        checkIDs();
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
        ObservableList<String> selectedItems = libItem.getSelectionModel().getSelectedItems();
        ArrayList<Item> match = new ArrayList<>();
        for (String s : selectedItems) {

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
                currentList.remove(idx);
            }
        }
        if(mode == 4) Librarian.createPopup(Librarian.printableString(match, true), "Item Details");
        else {
            updateList();
            Librarian.modifyRecords(currentList, new ArrayList<>());
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
     * Adds an item to the requests file to be processed and removes that item from the list
     * @param event unused
     */
    @FXML
    void requestCopy(ActionEvent event) {
        ArrayList<String> selection = new ArrayList<>();
        String userSelection = libItem.getSelectionModel().getSelectedItem();
        selection.add(App.getCurrentUser().getId() + "," + userSelection.split(" ")[0] + "," +  LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        Librarian.writeLogs(selection, "requests.csv", true);
        for(int i = 0; i < currentList.size(); ++i){
            if(currentList.get(i).getId() == Integer.parseInt(userSelection.split(" ")[0])){
                requestedIds.add(currentList.get(i).getId());
                currentList.remove(i);
                break;
            }
        }
        updateList();
    }

    /**
     * Checks requested items and removes them from search results
     */
    void checkIDs(){
        for(int id:requestedIds){
            for(int i = 0; i < currentList.size(); ++i){
                if(currentList.get(i).getId() == id){
                    currentList.remove(i);
                    break;
                }
            }
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
     * Navigate to owned view
     * @param event unused
     * @throws IOException Unable to find view File
     */
    @FXML
    void goOwned(ActionEvent event) throws IOException {
        App app = new App();
        app.changeScene("studentChecked-view.fxml");
    }
}
