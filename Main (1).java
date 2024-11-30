package miniproject;

// Importing necessary libraries for SQL operations and user input
import java.sql.*;
import java.util.Scanner;

public class Main {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/society";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        // Try-with-resources block to manage database connection and scanner
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                // Display menu options
                System.out.println("MENU:");
                System.out.println("1. Insert");
                System.out.println("2. Display");
                System.out.println("3. Update");
                System.out.println("4. Delete");
                System.out.println("5. Check Interactions");
                System.out.println("6. Parking slots available.");
                System.out.println("7. Check resident availability.");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                
                // Read user choice
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                // Switch case to handle user input and invoke corresponding methods
                switch (choice) {
                    case 1:
                        insert(conn, scanner); // Call the insert method
                        break;
                    case 2:
                        display(conn); // Call the display method
                        break;
                    case 3:
                        update(conn, scanner); // Call the update method
                        break;
                    case 4:
                        delete(conn, scanner); // Call the delete method
                        break;
                    case 5:
                        guestinter(conn, scanner); // Call the guest interaction check method
                        break;
                    case 6:
                        parking(conn); // Call the parking availability method
                        break;
                    case 7:
                        resguest(conn, scanner); // Call the resident-guest interaction insertion method
                        break;
                    case 0:
                        System.out.println("Exiting..."); // Exit message
                        return; // Exit the loop and terminate the program
                    default:
                        System.out.println("Invalid choice. Please try again."); // Handle invalid input
                }
            }
        } catch (SQLException e) {
            // Print SQL exception stack trace
            e.printStackTrace();
        }
    }

    // Method to insert a new guest record into the Guests table
    private static void insert(Connection conn, Scanner scanner) throws SQLException {
        // Gather user input for the guest details
        System.out.print("Enter guest id: ");
        int Guest_ID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter guest name: ");
        String Name = scanner.nextLine();
        System.out.print("Enter contact no: ");
        int Contact_Info = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter in time: ");
        String Check_In_Time = scanner.nextLine();
        System.out.print("Enter parking slot: ");
        String Parking_Slots = scanner.nextLine();

        // SQL query to insert guest details
        String sql = "INSERT INTO Guests (Guest_ID, Name, Contact_Info, Check_In_Time, Parking_Slots) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Guest_ID); // Set guest ID
            pstmt.setString(2, Name); // Set guest name
            pstmt.setInt(3, Contact_Info); // Set contact info
            pstmt.setString(4, Check_In_Time); // Set check-in time
            pstmt.setString(5, Parking_Slots); // Set parking slot
            pstmt.executeUpdate(); // Execute the query
            System.out.println("Insertion successful."); // Confirm successful insertion
        }
    }

    // Method to display all guest records from the Guests table
    private static void display(Connection conn) throws SQLException {
        // SQL query to fetch all guest details
        String sql = "SELECT * FROM Guests";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Guest Details:");
            // Iterate over the result set and display guest details
            while (rs.next()) {
                System.out.println("Guest_ID: " + rs.getInt("Guest_ID") +
                        ", Guest Name: " + rs.getString("Name") +
                        ", Contact no: " + rs.getInt("Contact_Info") +
                        ", In time: " + rs.getString("Check_In_Time") +
                        ", Parking slot: " + rs.getString("Parking_Slots"));
            }
        }
    }

    // Method to update an interaction record
    private static void update(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter guest id to update: ");
        int Guest_ID = scanner.nextInt(); // Read guest ID to update
        scanner.nextLine();
        System.out.print("Enter CORRECT resident ID: ");
        int R_ID = scanner.nextInt(); // Read correct resident ID

        // SQL query to update the interaction record
        String sql = "UPDATE Interactions SET R_ID = ? WHERE Guest_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, R_ID); // Set new resident ID
            pstmt.setInt(2, Guest_ID); // Set guest ID to update
            int rows = pstmt.executeUpdate(); // Execute the query
            if (rows > 0) {
                System.out.println("Guest for resident updated."); // Success message
            } else {
                System.out.println("Guest not found."); // Error message
            }
        }
    }

    // Method to delete a guest record from the Guests table
    private static void delete(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Guest_id of guest exiting: ");
        int Guest_ID = scanner.nextInt(); // Read guest ID to delete

        // SQL query to delete the guest record
        String sql = "DELETE FROM Guests WHERE Guest_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Guest_ID); // Set guest ID to delete
            int rows = pstmt.executeUpdate(); // Execute the query
            if (rows > 0) {
                System.out.println("Verified guest. Guest can exit."); // Success message
            } else {
                System.out.println("Invalid guest id."); // Error message
            }
        }
    }

    // Method to check interactions of a specific guest
    public static void guestinter(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter guest_id of guest whose interactions you want to view: ");
        int Guest_ID = scanner.nextInt(); // Read guest ID to check interactions

        // SQL query to call a stored function and get interaction count
        String sql = "SELECT CountInteractions(?) AS InteractionCount";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Guest_ID); // Set guest ID
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int interactionCount = rs.getInt("InteractionCount"); // Fetch interaction count
                    System.out.println("Total interactions for Guest_ID " + Guest_ID + ": " + interactionCount);
                } else {
                    System.out.println("No interactions found for this guest."); // Handle no interactions
                }
            }
        }
    }

    // Method to display parking slot availability
    private static void parking(Connection conn) throws SQLException {
        // SQL query to fetch parking slot details
        String sql = "SELECT * FROM Parking";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Parking Details:");
            // Iterate over the result set and display parking details
            while (rs.next()) {
                System.out.println("Parking slot: " + rs.getString("Parking_Slot_ID") +
                        "\nIs available: " + rs.getInt("Is_Available") +
                        ",\nAlloted to: " + rs.getInt("Assigned_To"));
                System.out.println("***********************");
            }
        }
    }

    // Method to insert a new interaction record between a guest and a resident
    private static void resguest(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter guest id: ");
        int Guest_ID = scanner.nextInt(); // Read guest ID
        scanner.nextLine();
        System.out.print("Enter resident id: ");
        int R_ID = scanner.nextInt(); // Read resident ID
        System.out.print("Is resident at home?: ");
        int Is_Home = scanner.nextInt(); // Read resident availability

        // SQL query to insert an interaction record
        String sql = "INSERT INTO Interactions (Guest_ID, R_ID, Is_Home, Date_Of_Interaction) VALUES (?, ?, ?, CURDATE())";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Guest_ID); // Set guest ID
            pstmt.setInt(2, R_ID); // Set resident ID
            pstmt.setInt(3, Is_Home); // Set availability status
            pstmt.executeUpdate(); // Execute the query
            System.out.println("Insertion successful."); // Confirm successful insertion
        }
    }
}
