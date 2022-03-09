package a1.assignment1fx;

import a1.assignment1fx.backend.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Test module for App backend logic handler
 * @author Omar Hussein
 */
class AppTest {

    /**
     * Test login functionality
     */
    @Test
    @BeforeAll
    @DisplayName("Test Login Match")
    static void checkLogin() {
            App.setCurrentUser(new User());
            App.log = Logger.getAnonymousLogger();
            App.checkLogin("test", "1234", "admin");
            Assertions.assertEquals(999, App.getCurrentUser().getId());
    }

    /**
     * Tests User Login is Not NULL
     */
    @Test
    @BeforeEach
    @DisplayName("Setup Variables")
    void setup() {
        App.log = Logger.getAnonymousLogger();
        App.setCurrentUser(new User(101, "Test", "abc123", "Jon", "Doe"));
    }

    /**
     * Tests User Setting is Not NULL
     */
    @Test
    @DisplayName("Test Setting Current User")
    void setCurrentUser() {
        App.setCurrentUser(new User(101, "Test", "abc123", "Jon", "Doe"));
        Assertions.assertNotNull(App.getCurrentUser());
        App.setCurrentUser(new Student(101, "Test", "abc123", "Jon", "Doe"));
        Assertions.assertNotNull(App.getCurrentUser());
        App.setCurrentUser(new Librarian(101, "Test", "abc123", "Jon", "Doe"));
        Assertions.assertNotNull(App.getCurrentUser());
        App.setCurrentUser(new Admin(101, "Test", "abc123", "Jon", "Doe"));
        Assertions.assertNotNull(App.getCurrentUser());

        App.setCurrentUser(new User());
        Assertions.assertNotNull(App.getCurrentUser());
        App.setCurrentUser(new Student());
        Assertions.assertNotNull(App.getCurrentUser());
        App.setCurrentUser(new Librarian());
        Assertions.assertNotNull(App.getCurrentUser());
        App.setCurrentUser(new Admin());
        Assertions.assertNotNull(App.getCurrentUser());
    }

    /**
     * Test logout functionality
     * @throws IOException irrelevant
     */
    @Test
    @DisplayName("Test Logout")
    void appLogout() throws IOException {
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());

        App.setCurrentUser(new User(101, "Test", "abc123", "Jon", "Doe"));
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());
        App.setCurrentUser(new Student(101, "Test", "abc123", "Jon", "Doe"));
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());
        App.setCurrentUser(new Librarian(101, "Test", "abc123", "Jon", "Doe"));
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());
        App.setCurrentUser(new Admin(101, "Test", "abc123", "Jon", "Doe"));
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());

        App.setCurrentUser(new User());
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());
        App.setCurrentUser(new Student());
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());
        App.setCurrentUser(new Librarian());
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());
        App.setCurrentUser(new Admin());
        App.appLogout();
        Assertions.assertNull(App.getCurrentUser());
    }

    /**
     * Test current User info
     */
    @Test
    @DisplayName("Test Current User")
    void getCurrentUser() {
        Assertions.assertEquals(101, App.getCurrentUser().getId());
        Assertions.assertEquals("Test", App.getCurrentUser().getUsername());
        Assertions.assertEquals("abc123", App.getCurrentUser().getPassword());
        Assertions.assertEquals("Jon", App.getCurrentUser().getFirstName());
        Assertions.assertEquals("Doe", App.getCurrentUser().getLastName());


        App.setCurrentUser(new Student(400, "Test", "abc123", "Jon", "Doe"));
        Assertions.assertEquals(400, App.getCurrentUser().getId());
        Assertions.assertEquals("Test", App.getCurrentUser().getUsername());
        Assertions.assertEquals("abc123", App.getCurrentUser().getPassword());
        Assertions.assertEquals("Jon", App.getCurrentUser().getFirstName());
        Assertions.assertEquals("Doe", App.getCurrentUser().getLastName());

        App.setCurrentUser(new Librarian(369, "Test", "abc123", "Jon", "Doe"));
        Assertions.assertEquals(369, App.getCurrentUser().getId());
        Assertions.assertEquals("Test", App.getCurrentUser().getUsername());
        Assertions.assertEquals("abc123", App.getCurrentUser().getPassword());
        Assertions.assertEquals("Jon", App.getCurrentUser().getFirstName());
        Assertions.assertEquals("Doe", App.getCurrentUser().getLastName());

        App.setCurrentUser(new Admin(716, "Test", "abc123", "Jon", "Doe"));
        Assertions.assertEquals(716, App.getCurrentUser().getId());
        Assertions.assertEquals("Test", App.getCurrentUser().getUsername());
        Assertions.assertEquals("abc123", App.getCurrentUser().getPassword());
        Assertions.assertEquals("Jon", App.getCurrentUser().getFirstName());
        Assertions.assertEquals("Doe", App.getCurrentUser().getLastName());

        App.setCurrentUser(new User());
        Assertions.assertEquals(0, App.getCurrentUser().getId());
        Assertions.assertEquals("", App.getCurrentUser().getUsername());
        Assertions.assertEquals("", App.getCurrentUser().getPassword());
        Assertions.assertEquals("", App.getCurrentUser().getFirstName());
        Assertions.assertEquals("", App.getCurrentUser().getLastName());

    }
}