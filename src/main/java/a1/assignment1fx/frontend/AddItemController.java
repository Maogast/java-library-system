package a1.assignment1fx.frontend;

import a1.assignment1fx.App;
import a1.assignment1fx.backend.Item;
import a1.assignment1fx.backend.Librarian;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles events for AddItem scene
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
public class AddItemController {

    private static ArrayList<Integer> currentIDs = Librarian.getIDList();

    @FXML
    private ListView<String> userDetails;

    @FXML
    private TextField addAuthor;

    @FXML
    private TextField addCount;

    @FXML
    private Text addError;

    @FXML
    private TextField addID;

    @FXML
    private Text addSuccess;

    @FXML
    private TextField addTitle;

    @FXML
    private Text setType;

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
     * Add an Item to record list
     * @param event unused
     */
    @FXML
    void addItem(ActionEvent event) {
        addError.setText("");
        addSuccess.setText("");

        if (addID.getText().isEmpty())
            addError.setText("Must Insert ID");
        else if (!addID.getText().matches("\\d+"))
            addError.setText("ID Must be a Positive Number");
        else if(currentIDs.contains(Integer.parseInt(addID.getText())))
            addError.setText("ID Already Exists");
        else if (addTitle.getText().isEmpty())
            addError.setText("Must Insert Title");
        else if (addAuthor.getText().isEmpty())
            addError.setText("Must Insert Author");
        else if(setType.getText().isEmpty())
            addError.setText("Must Select Type");
        else if (addCount.getText().isEmpty())
            addError.setText("Must Insert Number of Copies");
        else if (!addCount.getText().matches("\\d+"))
            addError.setText("Copies Must be a Positive Number");
        else {
            Librarian.appendRecord(new Item(
                    Integer.parseInt(addID.getText()),
                    addTitle.getText(),
                    addAuthor.getText(),
                    setType.getText().toLowerCase(),
                    Integer.parseInt(addCount.getText()),
                    0)
            );
            currentIDs.add(Integer.parseInt(addID.getText()));
            addSuccess.setText(setType.getText() + " Added Successfully!");
            setType.setText("");
        }
    }

    /**
     * Selects type of item to be added
     * @param event unused
     */
    @FXML
    void addType(ActionEvent event) {
        addError.setText("");
        MenuItem mi = (MenuItem) event.getSource();
        setType.setText(mi.getText().toUpperCase());
    }
}
