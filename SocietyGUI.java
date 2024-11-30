package miniproject; // Package declaration

import javax.swing.*; // Import for Swing components
import java.awt.*; // Import for layout management
import java.awt.event.ActionEvent; // Import for action event handling
import java.awt.event.ActionListener; // Import for action listener interface
import java.sql.*; // Import for SQL and JDBC components

public class MainGUI extends JFrame { // Main class extending JFrame for GUI functionality
    private static final String URL = "jdbc:mysql://localhost:3306/society"; // Database URL
    private static final String USER = "root"; // Database username
    private static final String PASSWORD = "password"; // Database password
    private Connection conn; // Connection object to manage database connection

    public MainGUI() { // Constructor for the GUI class
        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Show an error message if database connection fails
            JOptionPane.showMessageDialog(this, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Print stack trace for debugging
            System.exit(1); // Exit program if connection fails
        }
        
        // Set the title of the JFrame
        setTitle("Society Management System");
        // Set the size of the JFrame window
        setSize(400, 400);
        // Specify the operation when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Use a grid layout with 8 rows and 1 column
        setLayout(new GridLayout(8, 1));

        // Add buttons to the GUI with appropriate action listeners
        addButton("Insert", e -> openInsertDialog()); // Button to insert guest
        addButton("Display", e -> displayGuests()); // Button to display all guests
        addButton("Update", e -> openUpdateDialog()); // Button to update guest details
        addButton("Delete", e -> openDeleteDialog()); // Button to delete a guest
        addButton("Check Interactions", e -> openInteractionsDialog()); // Button to check guest interactions
        addButton("Parking Slots Available", e -> displayParkingSlots()); // Button to display parking slots
        addButton("Check Resident Availability", e -> openResidentDialog()); // Button to check resident availability
        addButton("Exit", e -> System.exit(0)); // Button to exit the application
    }

    // Helper method to add buttons with titles and action listeners
    private void addButton(String title, ActionListener action) {
        JButton button = new JButton(title); // Create a new JButton with the specified title
        button.addActionListener(action); // Attach the provided action listener to the button
        add(button); // Add the button to the JFrame
    }

    // Open a dialog for inserting a new guest
    private void openInsertDialog() {
        JTextField guestIdField = new JTextField(); // Text field for guest ID
        JTextField nameField = new JTextField(); // Text field for name
        JTextField contactField = new JTextField(); // Text field for contact info
        JTextField checkInField = new JTextField(); // Text field for check-in time
        JTextField parkingSlotField = new JTextField(); // Text field for parking slot

        // Create a dialog box with input fields
        Object[] message = {
            "Guest ID:", guestIdField,
            "Name:", nameField,
            "Contact Info:", contactField,
            "Check-In Time:", checkInField,
            "Parking Slot:", parkingSlotField
        };

        // Show the dialog and get the user's choice
        int option = JOptionPane.showConfirmDialog(this, message, "Insert Guest", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) { // If user clicks "OK"
            try {
                // SQL query to insert a new guest into the database
                String sql = "INSERT INTO Guests (Guest_ID, Name, Contact_Info, Check_In_Time, Parking_Slots) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(guestIdField.getText())); // Set guest ID
                    pstmt.setString(2, nameField.getText()); // Set name
                    pstmt.setInt(3, Integer.parseInt(contactField.getText())); // Set contact info
                    pstmt.setString(4, checkInField.getText()); // Set check-in time
                    pstmt.setString(5, parkingSlotField.getText()); // Set parking slot
                    pstmt.executeUpdate(); // Execute the SQL query
                    JOptionPane.showMessageDialog(this, "Insertion successful."); // Notify success
                }
            } catch (SQLException ex) { // Handle SQL exceptions
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error inserting guest.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
            }
        }
    }

    // Open a dialog for updating guest details
    private void openUpdateDialog() {
        JTextField guestIdField = new JTextField(); // Text field for guest ID
        JTextField nameField = new JTextField(); // Text field for new name
        JTextField contactField = new JTextField(); // Text field for new contact info
        JTextField checkInField = new JTextField(); // Text field for new check-in time
        JTextField parkingSlotField = new JTextField(); // Text field for new parking slot

        // Create a dialog box with input fields
        Object[] message = {
            "Guest ID:", guestIdField,
            "New Name:", nameField,
            "New Contact Info:", contactField,
            "New Check-In Time:", checkInField,
            "New Parking Slot:", parkingSlotField
        };

        // Show the dialog and get the user's choice
        int option = JOptionPane.showConfirmDialog(this, message, "Update Guest", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) { // If user clicks "OK"
            try {
                // SQL query to update guest details
                String sql = "UPDATE Guests SET Name = ?, Contact_Info = ?, Check_In_Time = ?, Parking_Slots = ? WHERE Guest_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nameField.getText()); // Set new name
                    pstmt.setInt(2, Integer.parseInt(contactField.getText())); // Set new contact info
                    pstmt.setString(3, checkInField.getText()); // Set new check-in time
                    pstmt.setString(4, parkingSlotField.getText()); // Set new parking slot
                    pstmt.setInt(5, Integer.parseInt(guestIdField.getText())); // Set guest ID
                    pstmt.executeUpdate(); // Execute the SQL query
                    JOptionPane.showMessageDialog(this, "Update successful."); // Notify success
                }
            } catch (SQLException ex) { // Handle SQL exceptions
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error updating guest.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
            }
        }
    }

        // Open a dialog for deleting a guest
    private void openDeleteDialog() {
        JTextField guestIdField = new JTextField(); // Text field for entering guest ID to delete
        Object[] message = {"Enter Guest ID to delete:", guestIdField}; // Create dialog box for input

        // Show the dialog and get the user's choice
        int option = JOptionPane.showConfirmDialog(this, message, "Delete Guest", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) { // If user clicks "OK"
            try {
                // SQL query to delete a guest based on their ID
                String sql = "DELETE FROM Guests WHERE Guest_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(guestIdField.getText())); // Set guest ID
                    int rows = pstmt.executeUpdate(); // Execute the delete query
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Guest deleted successfully."); // Notify success
                    } else {
                        JOptionPane.showMessageDialog(this, "Guest not found."); // Notify if guest ID doesn't exist
                    }
                }
            } catch (SQLException ex) { // Handle SQL exceptions
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error deleting guest.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
            }
        }
    }

    // Open a dialog to check interactions of a guest
    private void openInteractionsDialog() {
        JTextField guestIdField = new JTextField(); // Text field for entering guest ID
        Object[] message = {"Enter Guest ID to view interactions:", guestIdField}; // Create dialog box for input

        // Show the dialog and get the user's choice
        int option = JOptionPane.showConfirmDialog(this, message, "Check Interactions", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) { // If user clicks "OK"
            try {
                // SQL query to count interactions for a guest (assumes stored procedure CountInteractions exists)
                String sql = "SELECT CountInteractions(?) AS InteractionCount";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(guestIdField.getText())); // Set guest ID
                    try (ResultSet rs = pstmt.executeQuery()) { // Execute the query
                        if (rs.next()) {
                            int interactionCount = rs.getInt("InteractionCount"); // Retrieve interaction count
                            JOptionPane.showMessageDialog(this, "Total interactions for Guest ID " + guestIdField.getText() + ": " + interactionCount);
                        } else {
                            JOptionPane.showMessageDialog(this, "No interactions found for this guest."); // Notify if no data
                        }
                    }
                }
            } catch (SQLException ex) { // Handle SQL exceptions
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error retrieving interactions.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
            }
        }
    }

    // Open a dialog to check resident availability
    private void openResidentDialog() {
        JTextField residentIdField = new JTextField(); // Text field for entering resident ID
        Object[] message = {"Enter Resident ID to check availability:", residentIdField}; // Create dialog box for input

        // Show the dialog and get the user's choice
        int option = JOptionPane.showConfirmDialog(this, message, "Check Resident Availability", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) { // If user clicks "OK"
            try {
                // SQL query to check if a resident is available (assumes column Avaliability exists with values like 'Home')
                String sql = "SELECT * FROM Residents WHERE R_ID = ? AND Avaliability = 'Home'";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(residentIdField.getText())); // Set resident ID
                    try (ResultSet rs = pstmt.executeQuery()) { // Execute the query
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(this, "Resident is available."); // Notify if resident is available
                        } else {
                            JOptionPane.showMessageDialog(this, "Resident is not available."); // Notify if resident is unavailable
                        }
                    }
                }
            } catch (SQLException ex) { // Handle SQL exceptions
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error checking resident availability.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
            }
        }
    }

    // Display all guests in the database
    private void displayGuests() {
        try {
            // SQL query to select all guests
            String sql = "SELECT * FROM Guests";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) { // Execute the query

                StringBuilder result = new StringBuilder("Guest Details:\n"); // Prepare result string
                while (rs.next()) { // Iterate through result set
                    result.append("Guest_ID: ").append(rs.getInt("Guest_ID")) // Append guest ID
                          .append(", Name: ").append(rs.getString("Name")) // Append guest name
                          .append(", Contact Info: ").append(rs.getInt("Contact_Info")) // Append contact info
                          .append(", Check-In Time: ").append(rs.getString("Check_In_Time")) // Append check-in time
                          .append(", Parking Slot: ").append(rs.getString("Parking_Slots")) // Append parking slot
                          .append("\n"); // New line for each guest
                }
                JOptionPane.showMessageDialog(this, result.toString(), "Guest Details", JOptionPane.INFORMATION_MESSAGE); // Display result
            }
        } catch (SQLException ex) { // Handle SQL exceptions
            ex.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error displaying guests.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
        }
    }

    // Display all parking slot information
    private void displayParkingSlots() {
        try {
            // SQL query to select all parking slot data
            String sql = "SELECT * FROM Parking";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) { // Execute the query

                StringBuilder result = new StringBuilder("Parking Details:\n"); // Prepare result string
                while (rs.next()) { // Iterate through result set
                    result.append("Parking Slot ID: ").append(rs.getString("Parking_Slot_ID")) // Append parking slot ID
                          .append(", Is Available: ").append(rs.getInt("Is_Available")) // Append availability status
                          .append(", Assigned to Resident ID: ").append(rs.getInt("Assigned_To")) // Append assigned resident ID
                          .append("\n"); // New line for each slot
                }
                JOptionPane.showMessageDialog(this, result.toString(), "Parking Details", JOptionPane.INFORMATION_MESSAGE); // Display result
            }
        } catch (SQLException ex) { // Handle SQL exceptions
            ex.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error displaying parking slots.", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Use SwingUtilities to ensure thread-safety
            MainGUI gui = new MainGUI(); // Create an instance of MainGUI
            gui.setVisible(true); // Make the GUI visible
        });
    }
}

}

