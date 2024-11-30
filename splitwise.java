package miniproj; // Declare the package name

import java.util.*; // Import all classes from the java.util package

// Class to represent a user
class User {
    int userId; // Unique ID for the user
    String name; // Name of the user
    String email; // Email address of the user
    String mobileNumber; // Mobile number of the user
    List<String> history; // List to store the user's transaction history

    // Constructor to initialize the user details
    public User(int userId, String name, String email, String mobileNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.history = new ArrayList<>(); // Initialize an empty transaction history
    }

    // Adds a transaction record to the user's history
    public void addToHistory(String transaction) {
        history.add(transaction);
    }

    // Displays the transaction history for the user
    public void displayHistory() {
        System.out.println("Transaction History for " + name + ":");
        if (history.isEmpty()) { // Check if there are no transactions
            System.out.println("No transactions found.");
        } else {
            for (String transaction : history) { // Loop through the transaction history
                System.out.println(transaction);
            }
        }
    }

    // Returns a string representation of the user for display purposes
    @Override
    public String toString() {
        return String.format("%-5d | %-15s | %-25s | %-15s", userId, name, email, mobileNumber);
    }
}

// Class to represent an expense
class Expense {
    int payerId; // ID of the user who paid for the expense
    double amount; // Total amount of the expense
    List<Integer> participants; // List of participant user IDs
    String type; // Type of expense (equal or exact split)
    Map<Integer, Double> splitDetails; // Map to store participant ID and their share of the expense

    // Constructor to initialize the expense details
    public Expense(int payerId, double amount, List<Integer> participants, String type,
            Map<Integer, Double> splitDetails) {
        this.payerId = payerId;
        this.amount = amount;
        this.participants = participants;
        this.type = type;
        this.splitDetails = splitDetails;
    }
}

// Main application class for the Splitwise app
class SplitwiseApp {
    Map<Integer, User> users = new HashMap<>(); // Map of users with userId as the key
    Map<Integer, Map<Integer, Double>> balances = new HashMap<>(); // Nested map to track balances between users

    // Adds a new user to the system
    public void addUser(int userId, String name, String email, String mobileNumber) {
        User user = new User(userId, name, email, mobileNumber); // Create a new user object
        users.put(userId, user); // Add the user to the map
        balances.put(userId, new HashMap<>()); // Initialize the user's balance map
    }

    // Adds a new expense and updates the balances
    public void addExpense(int payerId, double amount, List<Integer> participants, String type,
            Map<Integer, Double> splitDetails) {
        if (type.equals("equal")) { // Check if the expense is split equally
            double splitAmount = amount / participants.size(); // Calculate the split amount per participant
            for (int participant : participants) { // Loop through each participant
                if (participant != payerId) { // Skip the payer
                    updateBalance(payerId, participant, splitAmount); // Update the balances
                }
            }
        } else if (type.equals("exact")) { // Check if the expense is split exactly
            double total = splitDetails.values().stream().mapToDouble(Double::doubleValue).sum(); // Calculate the total
                                                                                                  // of exact shares
            if (total != amount) { // Check if the total doesn't match the amount
                System.out.println("Error: Exact shares don't match the total amount.");
                return; // Exit the method if there's an error
            }
            for (int participant : splitDetails.keySet()) { // Loop through each participant in splitDetails
                if (participant != payerId) { // Skip the payer
                    updateBalance(payerId, participant, splitDetails.get(participant)); // Update the balances
                }
            }
        }
        logTransactionHistory(payerId, amount, participants, splitDetails, type); // Log the transaction history
    }

    // Updates the balance between two users
    private void updateBalance(int payerId, int participantId, double amount) {
        // Update the participant's balance (they owe money)
        balances.get(participantId).put(payerId, balances.get(participantId).getOrDefault(payerId, 0.0) + amount);
        // Update the payer's balance (they are owed money)
        balances.get(payerId).put(participantId, balances.get(payerId).getOrDefault(participantId, 0.0) - amount);
    }

    // Logs the transaction in the payer's and participants' history
    private void logTransactionHistory(int payerId, double amount, List<Integer> participants,
            Map<Integer, Double> splitDetails, String type) {
        String payerName = users.get(payerId).name; // Get the payer's name
        String description = payerName + " paid " + amount; // Create a description for the transaction
        users.get(payerId).addToHistory(description); // Log the transaction for the payer

        for (int participant : participants) { // Loop through each participant
            if (participant != payerId) { // Skip the payer
                double owedAmount = (type.equals("equal")) ? amount / participants.size()
                        : splitDetails.get(participant); // Determine the owed amount
                String detail = users.get(participant).name + " owes " + owedAmount + " to " + payerName; // Create a
                                                                                                          // detailed
                                                                                                          // transaction
                users.get(participant).addToHistory(detail); // Log the transaction for the participant
            }
        }
    }

    // Displays all balances between users
    public void showAllBalances() {
        System.out.println("Final Balances:");
        for (int userId : balances.keySet()) { // Loop through each user
            for (int otherUserId : balances.get(userId).keySet()) { // Loop through each user's balances
                double balance = balances.get(userId).get(otherUserId); // Get the balance
                if (balance > 0) { // If the user owes money
                    System.out
                            .println(users.get(userId).name + " owes " + users.get(otherUserId).name + ": " + balance);
                } else if (balance < 0) { // If the user is owed money
                    System.out.println(
                            users.get(otherUserId).name + " owes " + users.get(userId).name + ": " + Math.abs(balance));
                }
            }
        }
    }

    // Displays the transaction history for a specific user
    public void showUserHistory(int userId) {
        if (users.containsKey(userId)) { // Check if the user exists
            users.get(userId).displayHistory(); // Display the user's transaction history
        } else {
            System.out.println("User ID not found."); // Print an error if the user doesn't exist
        }
    }

    // Displays all users in a tabular format
    public void showUsers() {
        System.out.println("User Table:");
        System.out.println("UserID | Name            | Email                    | Mobile Number");
        System.out.println("-------------------------------------------------------------");

        for (User user : users.values()) { // Loop through each user
            System.out.println(user); // Print the user details
        }
    }
}

// Main class to run the Splitwise app
public class Main {
    static Scanner sc = new Scanner(System.in); // Create a scanner for user input

    public static void main(String[] args) {
        SplitwiseApp app = new SplitwiseApp(); // Create an instance of SplitwiseApp

        // Prompt the user to enter the number of users
        System.out.print("Enter the number of users: ");
        int userCount = sc.nextInt();
        sc.nextLine(); // Consume the newline

        // Loop to add users
        for (int i = 0; i < userCount; i++) {
            System.out.println("Enter details for User " + (i + 1));
            System.out.print("User ID: ");
            int userId = sc.nextInt();
            sc.nextLine(); // Consume the newline
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Mobile Number: ");
            String mobileNumber = sc.nextLine();
            app.addUser(userId, name, email, mobileNumber); // Add the user
        }

        app.showUsers(); // Display all users

        // Loop to add expenses
        boolean continueAddingExpenses = true;
        while (continueAddingExpenses) {
            System.out.println("\nAdd an Expense:");
            System.out.print("Payer User ID: ");
            int payerId = sc.nextInt();
            System.out.print("Total Amount: ");
            double amount = sc.nextDouble();
            sc.nextLine(); // Consume the newline

            // Get participants
            System.out.print("Enter number of participants: ");
            int participantCount = sc.nextInt();
            List<Integer> participants = new ArrayList<>();
            for (int i = 0; i < participantCount; i++) {
                System.out.print("Participant User ID " + (i + 1) + ": ");
                participants.add(sc.nextInt());
            }
            sc.nextLine(); // Consume the newline

            // Get expense type
            System.out.print("Type (equal/exact): ");
            String type = sc.nextLine();

            Map<Integer, Double> splitDetails = new HashMap<>();
            if (type.equals("exact")) { // If the type is exact, get the split details
                for (int participant : participants) {
                    System.out.print("Enter the exact amount for User ID " + participant + ": ");
                    splitDetails.put(participant, sc.nextDouble());
                }
                sc.nextLine(); // Consume the newline
            }

            app.addExpense(payerId, amount, participants, type, splitDetails); // Add the expense

            // Check if the user wants to add another expense
            System.out.print("Do you want to add another expense? (yes/no): ");
            continueAddingExpenses = sc.nextLine().equalsIgnoreCase("yes");
        }

        app.showAllBalances(); // Display all balances

        // Prompt the user to view transaction history
        System.out.print("Do you want to view transaction history for any user? (yes/no): ");
        if (sc.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("Enter User ID: ");
            int userId = sc.nextInt();
            app.showUserHistory(userId); // Display the user's transaction history
        }
    }
}

/*
 * Enter the number of users: 3
 * Enter details for User 1
 * User ID: 1
 * Name: anushka
 * Email: dkjchnrf
 * Mobile Number: 231872
 * Enter details for User 2
 * User ID: 2
 * Name: siddhani
 * Email: djwhnfwfw
 * Mobile Number: 2376487
 * Enter details for User 3
 * User ID: 3
 * Name: mayuri
 * Email: duawenfw
 * Mobile Number: 2837146
 * User Table:
 * UserID | Name | Email | Mobile Number
 * -------------------------------------------------------------
 * 1 | anushka | dkjchnrf | 231872
 * 2 | siddhani | djwhnfwfw | 2376487
 * 3 | mayuri | duawenfw | 2837146
 * 
 * Add an Expense:
 * Payer User ID: 1
 * Total Amount: 1500
 * Enter number of participants: 3
 * Participant User ID: 1
 * Participant User ID: 2
 * Participant User ID: 3
 * Expense Type (equal/exact): equal
 * 
 * Do you want to add another expense? (yes/no): yes
 * 
 * Do you want to view transaction history? (yes/no): no
 * 
 * Add an Expense:
 * Payer User ID: 2
 * Total Amount: 750
 * Enter number of participants: 2
 * Participant User ID: 1
 * Participant User ID: 2
 * Expense Type (equal/exact): exact
 * Amount for User ID 1: 25
 * Amount for User ID 2: 725
 * 
 * Do you want to add another expense? (yes/no): no
 * 
 * Do you want to view transaction history? (yes/no): yes
 * Enter User ID to view history: 1
 * Transaction History for anushka:
 * anushka paid 1500.0
 * anushka owes 25.0 to siddhani
 * 
 * Do you want to view transaction history? (yes/no): yes
 * Enter User ID to view history: 2
 * Transaction History for siddhani:
 * siddhani owes 500.0 to anushka
 * siddhani paid 750.0
 * 
 * Do you want to view transaction history? (yes/no): yes
 * Enter User ID to view history: 3
 * Transaction History for mayuri:
 * mayuri owes 500.0 to anushka
 * 
 * Do you want to view transaction history? (yes/no): yes
 * Enter User ID to view history: 1
 * Transaction History for anushka:
 * anushka paid 1500.0
 * anushka owes 25.0 to siddhani
 * 
 * Do you want to view transaction history? (yes/no): no
 * Final Balances:
 * siddhani owes anushka: 475.0
 * mayuri owes anushka: 500.0
 * siddhani owes anushka: 475.0
 * mayuri owes anushka: 500.0
 * 
 * Program terminated.
 */
