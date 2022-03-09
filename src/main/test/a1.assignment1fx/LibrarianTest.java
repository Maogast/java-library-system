package a1.assignment1fx;

import a1.assignment1fx.backend.Item;
import a1.assignment1fx.backend.Librarian;
import a1.assignment1fx.backend.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Test module for Librarian backend logic handler
 * @author Omar Hussein
 */
class LibrarianTest {

    /**
     * Setup Test Requirements
     */
    @Test
    @BeforeEach
    @DisplayName("Test setup")
    void setup() {
        App.log = Logger.getAnonymousLogger();
        App.setCurrentUser(new User());
    }

    /**
     * Test Item search functionality
     */
    @Test
    @DisplayName("Test Item Search")
    void findItems() {
        Assertions.assertEquals(17, Librarian.findItems("id", "17", true).get(0).getId());
        Assertions.assertEquals(13, Librarian.findItems("category", "audio", true).get(0).getId());
        Assertions.assertEquals(8, Librarian.findItems("title", "The Good The Bad and The Ugly", true).get(0).getId());
        Assertions.assertEquals(0, Librarian.findItems("title", "nonExistentTitle", true).size());
        Assertions.assertEquals(0, Librarian.findItems("bad", "", true).size());
    }

    /**
     * Test proper format of printable string
     */
    @Test
    @DisplayName("Test Printable String")
    void printableString() {
        ArrayList<Item> test = new ArrayList<>();
        Assertions.assertEquals(0, Librarian.printableString(test, true).size());
        test.add(new Item());
        Assertions.assertEquals(1, Librarian.printableString(test, false).size());
        test.add(new Item());
        Assertions.assertEquals(2, Librarian.printableString(test, true).size());
    }

    /**
     * Test for proper Item id list
     */
    @Test
    @DisplayName("Test Get Item ID List")
    void getIDList() {
        ArrayList<Integer> test = Librarian.getIDList();
        Assertions.assertNotEquals(0, test.size());
        Assertions.assertEquals(1, test.get(0));
    }

    /**
     * Test proper file loaded in proper format
     */
    @Test
    @DisplayName("Test Loading Logs")
    void loadLogs() {
        Assertions.assertEquals(0, Librarian.loadLogs(0, "").size());
        Assertions.assertEquals(0, Librarian.loadLogs(0, ".csv").size());
        Assertions.assertEquals(0, Librarian.loadLogs(0, "notAFile").size());
        Assertions.assertNotEquals(0, Librarian.loadLogs(0, "admin.csv").size());
        Assertions.assertNotEquals(0, Librarian.loadLogs(1, "").size());
        Assertions.assertNotEquals(0, Librarian.loadLogs(2, "").size());
        Assertions.assertNotEquals(0, Librarian.loadLogs(3, "").size());
        Assertions.assertNotEquals(0, Librarian.loadLogs(4, "").size());
    }

    /**
     * Test User search functionality
     */
    @Test
    @DisplayName("Test Finding User")
    void findUser() {
        Assertions.assertEquals(5, Librarian.findUser("5", "librarian").getId());
        Assertions.assertEquals(17, Librarian.findUser("17", "student").getId());
        Assertions.assertEquals(911, Librarian.findUser("omar", "admin").getId());
        Assertions.assertEquals(-1, Librarian.findUser("9191991", "librarian").getId());
        Assertions.assertEquals(-1, Librarian.findUser("notAUser", "student").getId());
        Assertions.assertEquals(-1, Librarian.findUser("", "admin").getId());
        Assertions.assertEquals(-1, Librarian.findUser("999", "notAFile").getId());
        Assertions.assertEquals(-1, Librarian.findUser("999", "").getId());
    }
}