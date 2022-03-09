package a1.assignment1fx.backend;

/**
 * Maintains Item properties such as id, title, type, count, borrowed count, and waiting list
 * Acts as a container class for defining library items
 *
 * @author Omar Hussein
 *
 * @version 1.0.1
 * @since 02/03/2022
 *
 * @see Librarian
 * @see Student
 */
public class Item {
    private int id;
    private String title;
    private String author;
    private String type;
    private int count;
    private int borrowed;

    /**
     * Default constructor, initializes object as empty
     */
    public Item() {
        this.id = 0;
        this.title = "";
        this.author = "";
        this.type = "";
        this.count = 0;
        this.borrowed = 0;
    }

    /**
     * Constructor overload to set object from user input
     * @param id id
     * @param title title
     * @param author author
     * @param type item type (magazine, document, book, video, audio)
     * @param count number of duplicate items
     * @param borrowed number of borrowed items
     */
    public Item(int id, String title, String author, String type, int count, int borrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
        this.count = count;
        this.borrowed = borrowed;
    }

    /**
     * Getter for ID
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for ID
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for Title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for Title
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for Author
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter for Author
     * @param author author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter for Item Type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for Item Type
     * @param type item type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for Item Count
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * Setter for Item Count
     * @param count item count
     */
    public void setCount(int count) {
        if(count >= 0 && count >= this.borrowed) this.count = count;
    }

    /**
     * Getter for Borrowed count
     * @return borrowed count
     */
    public int getBorrowed() {
        return borrowed;
    }

    /**
     * Setter for Borrowed count, must be less than Item count
     * @param borrowed borrowed status
     */
    public void setBorrowed(int borrowed) {
        if(borrowed >= 0 && borrowed <= this.count) this.borrowed = borrowed;
    }

    /**
     * Getter for current available copies to borrow
     * @return available copies
     */
    public int getAvailable() { return count - borrowed;}

    /**
     * Creates a printable csv string from the Item object
     * @return printable csv string
     */
    public String csvItemString() {
        return id + "," + title + "," + author + "," + type + "," + count + "," + borrowed + "\n";
    }
}
