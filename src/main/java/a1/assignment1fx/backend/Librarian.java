package a1.assignment1fx.backend;

import a1.assignment1fx.App;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Handles logic for Librarian scene
 *
 * @author Omar Hussein
 *
 * @version 1.0.4
 * @since 02/03/2022
 *
 * @see a1.assignment1fx.App
 * @see a1.assignment1fx.frontend.LibrarianController
 * @see java.io
 */
public class Librarian extends User {

    /**
     * Default Constructor, calls super class constructor (User)
     */
    public Librarian() {
        super();
    }

    /**
     * Overloaded Constructor, calls super class constructor (User)
     * @param id id
     * @param username username
     * @param password password
     * @param firstName first name
     * @param lastName last name
     */
    public Librarian(int id, String username, String password, String firstName, String lastName) {
        super(id, username, password, firstName, lastName);
    }

    /**
     * Opens Item record file, finds all appropriate matches based on search type (all, id, title, category)
     * Can accept a partial title and will find all similar matches, category type,
     * @param type search type
     * @param pattern search pattern
     * @param oneRecord search single record
     * @return list of matching items
     */
    public static ArrayList<Item> findItems(String type, String pattern, boolean oneRecord) {

        int idx = 1;
        switch (type) {
            case "all":
                pattern = "";
                break;
            case "id":
                idx = 0;
                break;
            case "title":
                break;
            case "category":
                idx = 3;
                break;
            default:
                App.log.info("Bad Search Type");
                return new ArrayList<>();
        }
        ArrayList<Item> items = new ArrayList<>();
        File f = new File("./src/main/resources/items.csv");
        String line;
        try(FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr)) {
            while((line = br.readLine()) != null){
                String[] record = line.split(",");
                if (record.length == 0) continue;

                if((idx == 0 && Integer.parseInt(record[idx]) == Integer.parseInt(pattern)) ||
                    record[idx].toLowerCase().contains(pattern.toLowerCase())) {
                    items.add(new Item(
                            Integer.parseInt(record[0]),
                            record[1],
                            record[2],
                            record[3],
                            Integer.parseInt(record[4]),
                            Integer.parseInt(record[5]))
                    );
                    if(idx == 0 || oneRecord) break;
                }
            }
        }
        catch (FileNotFoundException e) {
            App.log.severe("Unable to Load Record File");
            e.printStackTrace();
        }
        catch (Exception e) {
            App.log.severe("Unknown Critical Error Occurred");
            e.printStackTrace();
        }

        if(items.size() == 0) App.log.info("No Matching " + type + " Found for " + pattern);

        if(idx != 0 && !oneRecord) items.sort(Comparator.comparingInt(Item::getId));
        return items;
    }

    /**
     * Converts a list of Items into printable simple or detailed formatted strings of minimal details
     * @param list item list
     * @param detailed is detailed
     * @return printable string list
     */
    public static ArrayList<String> printableString(ArrayList<Item> list, boolean detailed) {
        ArrayList<String> printableList = new ArrayList<>();
        for(Item i : list)
            printableList.add(i.getId() + " - " + i.getTitle() + " - " + i.getType().toUpperCase() +
                    (detailed ?  "\n\tAuthor : " + i.getAuthor() : "")+ " - Available: " + i.getAvailable() +
                    (detailed ? "\n\tCopies : " + i.getCount() + " - Borrowed : " + i.getBorrowed() : "")
            );
        return printableList;
    }

    /**
     * Append new Item to record list
     * @param record item to be added
     */
    public static void appendRecord(Item record) {
        File f = new File("./src/main/resources/items.csv");
        try(FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(record.csvItemString());
            App.log.warning("New Item Record Added : " + record.csvItemString());
        }
        catch (IOException e) {
            App.log.warning("Unable to Append New Record");
            e.printStackTrace();
        }
        catch (Exception e) {
            App.log.severe("Unknown Critical Error Occurred");
            e.printStackTrace();
        }
    }

    /**
     * Generates a list of current occupied IDs in the Library
     * @return id list
     */
    public static ArrayList<Integer> getIDList() {
        ArrayList<Integer> idList = new ArrayList<>();
        for (Item i : findItems("all", "", false))
            idList.add(i.getId());
        return idList;
    }

    /**
     * Modify Item record list and persist changes
     * @param records modified records
     * @param removedIDList removed records
     */
    public static void modifyRecords(ArrayList<Item> records, ArrayList<Integer> removedIDList) {
        ArrayList<Item> items = findItems("all", "", false);
        if (!removedIDList.isEmpty()) {
            items.removeIf((itm) -> removedIDList.contains(itm.getId()));
            App.log.warning("Removed Items with ID : " + removedIDList);
            removedIDList.clear();
        }

        for (Item rec : records) {
            for (Item itm : items) {
                if (itm.getId() == rec. getId()) {
                    itm.setCount(rec.getCount());
                    itm.setBorrowed(rec.getBorrowed());
                }
            }
        }

        File f = new File("./src/main/resources/items.csv");
        try (FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw)) {
            for (Item i : items)
                bw.write(i.csvItemString());
            App.log.warning("Updated Item Records");
        }
        catch (IOException e){
            App.log.warning("Unable to Modify Records");
            e.printStackTrace();
        }
        catch (Exception e) {
            App.log.severe("Unknown Critical Error Occurred");
            e.printStackTrace();
        }
    }

    /**
     * Loads log/list records from file and formats them for printing
     * mode 1 : lending history
     * mode 2 : currently borrowed list
     * mode 3 : current request list
     * default : provide file name
     * @param mode read mode
     * @param zeroMode optional filename with mode 0
     * @return formatted history list
     */
    public static ArrayList<String> loadLogs(int mode, String zeroMode) {
        ArrayList<String> records = new ArrayList<>();
        String line = switch (mode) {
            case 1 -> "lendingLog.log";
            case 2 -> "lendingList.csv";
            case 3 -> "requests.csv";
            case 4 -> "waitingList.csv";
            default -> zeroMode;
        };

        if(!line.matches("..*\\..*")) {
            App.log.info("Bad Filename");
            return records;
        }

        File f = new File("./src/main/resources/" + line);
        try (FileReader fr = new FileReader(f);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if(details.length == 0) continue;

                if(mode == 4) {
                    line = "";
                    for (int i = 1; i < details.length; ++i)
                        line += details[i] + (i < details.length - 1 ? "," : "" );
                }

                line = switch (mode) {
                    case 1 -> "Username : " + details[0] + " - Title : " + details[1] +
                            "\n\tBorrowed at : " + details[2] + " - Returned at : " + details[3];
                    case 2 -> "Username : " + details[0] + " - Title : " + details[1] +
                            "\n\tBorrowed at : " + details[2];
                    case 3 -> "UserID : " + details[0] + " - ItemID : " + details[1] + " at : " + details[2];
                    case 4 -> "ItemID : " + details[0] + " - List : " + line;
                    default -> line;
                };
                records.add(line);
            }
        }
        catch (FileNotFoundException e){
            line = switch (mode) {
                case 1 -> "History Log";
                case 2 -> "Borrowed Item List";
                case 3 -> "Request List";
                case 4 -> "Waiting List";
                default -> zeroMode;
            };
            App.log.severe("Unable to Read " + line + " File");
            e.printStackTrace();
        }
        catch (Exception e) {
            App.log.severe("Unknown Critical Error Occurred");
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Write updated records to log/list files
     * @param writeList updated list
     * @param filename target file
     * @param append append mode
     */
    public static void writeLogs(ArrayList<String> writeList, String filename, boolean append) {
        File f = new File("./src/main/resources/" + filename);
        try(FileWriter fw = new FileWriter(f, append);
            BufferedWriter bw = new BufferedWriter(fw)) {

            for (String rec : writeList)
                bw.write(rec + "\n");

            App.log.warning("Updated Records in " + filename);

        } catch (IOException e) {
            App.log.warning("Unable to Write to " + filename);
            e.printStackTrace();
        }
        catch (Exception e) {
            App.log.severe("Unknown Critical Error Occurred");
            e.printStackTrace();
        }
    }

    /**
     * Processes a Student request to borrow a book, Release or Reject
     * @param request request string
     * @param release is release
     */
    public static void processRequest(String request, boolean release) {
        ArrayList<String> requestList = loadLogs(0, "requests.csv");
        String[] part = request.split(" - ");
        int userID = Integer.parseInt(part[0].split(" : ")[1]);
        int itemID = Integer.parseInt(part[1].split(" : ")[1].split(" ")[0]);
        requestList.removeIf((itm) -> Integer.parseInt(itm.split(",")[0]) == userID);
        writeLogs(requestList, "requests.csv", false);

        if(release) {
            ArrayList<Item> records = findItems("all", "", false);
            requestList.clear();
            for(Item rec : records) {
                if(rec.getId() == itemID){
                    if (rec.getAvailable() == 0) modifyWaitList(itemID, userID, true);
                    else rec.setBorrowed(rec.getBorrowed() + 1);
            System.out.println(rec.getBorrowed());

                    requestList.add(findUser(Integer.toString(userID), "student").getUsername() + "," + rec.getTitle() + "," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    break;
                }
            }
            writeLogs(requestList,"lendingList.csv", true);
            modifyRecords(records, new ArrayList<>());
        }

        App.log.info("Request for Item #" + itemID + " by User ID : " + userID + " has been " + (release ? "RELEASED" : "REJECTED"));
    }

    /**
     * Finds current user ids in the library system
     * @param userClass user class
     * @return list of ids
     */
    public static ArrayList<Integer> getUserIDList(String userClass) {
        ArrayList<Integer> idList = new ArrayList<>();

        for (String s : Librarian.loadLogs(0, userClass + ".csv"))
            idList.add(Integer.parseInt(s.split(",")[0]));
        return idList;
    }

    /**
     * Find user from record list with matching id and specified class (student, librarian, admin)
     * @param pattern user id or username
     * @param userClass user class
     * @return user object
     */
    public static User findUser(String pattern, String userClass) {
        int mode = 1;

        if(pattern.isEmpty() || userClass.isEmpty() ||
                (!userClass.matches("^student$") && !userClass.matches("^librarian$") && !userClass.matches("^admin$")))
            return new User(-1, "", "", "", "");

        if(pattern.matches("\\d+")) mode = 0;
        for(String s : loadLogs(0, userClass + ".csv"))
            if (pattern.matches(s.split(",")[mode])) {
                String[] details = s.split(",");
                return switch (userClass) {
                    case "student" -> new Student(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3], details[5]);
                    case "librarian" -> new Librarian(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3]);
                    case "admin" -> new Admin(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3]);
                    default -> new User(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3]);
                };
            }
        return new User(-1, "", "", "", "");
    }

    /**
     * Find all users from record list with specified class (student, librarian, admin)
     * @param userClass user class
     * @return user list
     */
    public static ArrayList<User> findAllUser(String userClass) {
        ArrayList<User> users = new ArrayList<>();

        if(!userClass.matches("^student$") && !userClass.matches("^librarian$") && !userClass.matches("^admin$"))
            return users;

        for(String s : loadLogs(0, userClass + ".csv")) {
            String[] details = s.split(",");
            switch (userClass) {
                case "student" -> users.add(new Student(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3], details[5]));
                case "librarian" -> users.add(new Librarian(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3]));
                case "admin" -> users.add(new Admin(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3]));
                default -> users.add(new User(Integer.parseInt(details[0]), details[1], details[4], details[2], details[3]));
            }
        }
        return users;
    }

    /**
     * Returns borrowed Item to Library and updates borrowed logs and puts next in waiting list in line
     * @param selection selection string
     */
    public static void returnItem(String selection) {
        String[] part = selection.split(" : ");
        String  username = part[1].split(" - ")[0];
        String itemName = part[2].split("\n")[0];

        ArrayList<String> logString = new ArrayList<>();
        ArrayList<Item> item = findItems("title", itemName, true);

        removeRecord("lendingList.csv", username, 0);
        logString.add(username + "," + itemName + "," + part[3] + "," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        writeLogs(logString, "lendingLog.log", true);

        modifyWaitList(item.get(0).getId(), 0, false);

        item.get(0).setBorrowed(item.get(0).getBorrowed() - 1);
        modifyRecords(item, new ArrayList<>());
    }

    /**
     * Loads a record file, finds all matching key's in the record at specified position and removes them
     * @param filename filename
     * @param idList uniquely defining integer key list
     * @param pos key position
     */
    public static void removeRecords(String filename, int[] idList, int pos) {
        ArrayList<String> records = loadLogs(0, filename);
        for (int id : idList)
            for (int i = 0; i < records.size(); ++i)
                if(Integer.parseInt(records.get(i).split(",")[pos]) == id) {
                    records.remove(i);
                    break;
                }
        writeLogs(records, filename, false);
    }

    /**
     * Loads a record file, finds matching key in the records at specified position and removes it
     * @param filename filename
     * @param key identifying key
     * @param pos key position
     */
    public static void removeRecord(String filename, String key, int pos) {
        ArrayList<String> records = loadLogs(0, filename);
        for (int i = 0; i < records.size(); ++i)
            if(key.matches(records.get(i).split(",")[pos])) {
                records.remove(i);
                break;
            }
        writeLogs(records, filename, false);
    }

    /**
     * Update waiting list information for Items
     * Will add first user in waiting list to request list if in remove mode and a match is found
     * @param itemID item id
     * @param userID user id
     * @param add add/remove mode
     */
    private static void modifyWaitList(int itemID, int userID, boolean add) {
        ArrayList<String> waitList = loadLogs(0, "waitingList.csv");
        int newUserID = -1;

        for (String s : waitList) {
            ++newUserID;
            if (Integer.parseInt(s.split(",")[0]) == itemID) {
                if (add) s += "," + userID;
                else {
                    waitList.set(newUserID, s.replaceFirst(",\\d+", ""));
                    newUserID = Integer.parseInt(s.split(",")[1]);

                    ArrayList<String> newRequest = new ArrayList<>();
                    newRequest.add(newUserID + "," + itemID + "," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    writeLogs(newRequest, "requests.csv", true);
                }

                writeLogs(waitList, "waitingList.csv", false);
                break;
            }
        }
    }

    /**
     * Create popup window with User selected Item details
     * @param list user selection
     * @param title window title
     * @throws IOException Unable to crate detail popup window
     */
    public static void createPopup(ArrayList<String> list, String title) throws IOException {
        ListView<String> popupList = new ListView<>();
        popupList.getItems().addAll(list);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(popupList, 300, 100));
        stage.show();
    }
}