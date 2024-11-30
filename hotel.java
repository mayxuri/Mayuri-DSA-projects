import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Create an instance of the Operations class
        Operations objt = new Operations();

        // Add cities and hotels to the domain
        objt.AddToDomain();

        // Prompt the user for login or signup
        objt.accept();

        // Display available cities to the user
        objt.displayCities();
    }
}

// The SignUp class handles user authentication (login/signup)
class SignUp {
    Scanner sc = new Scanner(System.in); // Scanner object for user input

    // Method to display login/signup options
    void accept() {
        System.out.println("1. Login      2. SignUp     :");
        int SL = sc.nextInt(); // Get the user's choice
        sc.nextLine(); // Consume newline

        // Perform login or signup based on user's choice
        if (SL == 1) {
            login();
        } else if (SL == 2) {
            signup();
            login(); // Automatically log in after signing up
        } else {
            System.out.println("Wrong choice"); // Handle invalid input
            accept();
        }
    }

    ArrayList<User> userList = new ArrayList<>(); // List to store registered users

    // Helper method to capture username and password
    String[] entry() {
        System.out.println("Enter username:");
        String usernamee = sc.nextLine();
        System.out.println("Enter password:");
        String passwordd = sc.nextLine();
        return new String[] { usernamee, passwordd }; // Return entered credentials
    }

    // Login functionality
    User login() {
        System.out.println("Login page");
        int bool = 3; // Flag for successful login
        String[] user = entry(); // Get user credentials
        String usernamee = user[0], passwordd = user[1];

        // Check if credentials match any user in the userList
        for (User item : userList) {
            if (usernamee.equals(item.username) && passwordd.equals(item.password)) {
                bool = 0; // Set flag to indicate success
                System.out.println("Logged-in successfully!");
                return item;
            }
        }

        // If login fails, prompt user again
        if (bool != 0) {
            System.out.println("Wrong Passsword or Username");
            accept();
        }
        return null;
    }

    // Signup functionality
    User signup() {
        System.out.println("Signup page");
        int bool = 3; // Flag to track if username is unique
        String[] user = entry(); // Get user credentials
        String usernamee = user[0], passwordd = user[1];

        // Check if username already exists
        for (User item : userList) {
            if (usernamee.equals(item.username)) {
                System.out.println("Username already exists, please create a new one");
                bool = 0; // Set flag to indicate username is not unique
            }
        }

        // Add user if username is unique
        if (bool != 0) {
            User objU = new User(usernamee, passwordd); // Create new User object
            userList.add(objU); // Add user to the list
            System.out.println("Signed-up successfully!");
            return objU;
        } else {
            signup(); // Recursively call signup if username is not unique
        }
        return null;
    }
}

// Class to represent a user with a username and password
class User {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

// Domain class represents cities and hotels available in the system
class Domain extends SignUp {
    ArrayList<String> cities = new ArrayList<>(); // List of available cities
    ArrayList<Hotels> hotels = new ArrayList<>(); // List of hotels

    // Add default cities and hotels to the domain
    void AddToDomain() {
        // Add cities to the list
        cities.add("pune");
        cities.add("mumbai");
        cities.add("kolhapur");

        // Add hotels in Kolhapur
        double[] ck5a = { 7000, 11000, 18000 };
        int[] ak5a = { 10, 12, 15 };
        Hotels k5a = new Hotels("kolhapur", 5, "sayaji", ck5a, ak5a);

        double[] ck5b = { 8500, 12000, 20000 };
        int[] ak5b = { 14, 9, 7 };
        Hotels k5b = new Hotels("kolhapur", 5, "radisson blu", ck5b, ak5b);

        double[] ck4a = { 4500, 7000, 9500 };
        int[] ak4a = { 7, 5, 10 };
        Hotels k4a = new Hotels("kolhapur", 4, "citrus", ck4a, ak4a);

        double[] ck4b = { 4000, 6500, 8000 };
        int[] ak4b = { 10, 3, 8 };
        Hotels k4b = new Hotels("kolhapur", 4, "maratha regency", ck4b, ak4b);

        double[] ck3a = { 2500, 3500, 6000 };
        int[] ak3a = { 6, 4, 7 };
        Hotels k3a = new Hotels("kolhapur", 3, "panchshil", ck3a, ak3a);

        double[] ck3b = { 2000, 3000, 5500 };
        int[] ak3b = { 5, 9, 6 };
        Hotels k3b = new Hotels("kolhapur", 3, "pearl", ck3b, ak3b);

        // Add hotels to the list
        hotels.add(k5a);
        hotels.add(k5b);
        hotels.add(k4a);
        hotels.add(k4b);
        hotels.add(k3a);
        hotels.add(k3b);

          // pune
        double[] cp5a = { 10000, 15000, 22000 };
        int[] ap5a = { 12, 20, 10 };
        Hotels p5a = new Hotels("pune", 5, "ritz carton", cp5a, ap5a);

        double[] cp5b = { 9000, 13000, 20000 };
        int[] ap5b = { 10, 15, 12 };
        Hotels p5b = new Hotels("pune", 5, "jw marriot", cp5b, ap5b);

        double[] cp4a = { 5000, 7000, 10000 };
        int[] ap4a = { 14, 11, 9 };
        Hotels p4a = new Hotels("pune", 4, "orchid", cp4a, ap4a);
        double[] cp4b = { 5500, 6000, 9000 };

        int[] ap4b = { 8, 20, 10 };
        Hotels p4b = new Hotels("pune", 4, "lemon tree", cp4b, ap4b);
        double[] cp3a = { 4000, 5000, 8000 };

        int[] ap3a = { 12, 18, 5 };
        Hotels p3a = new Hotels("pune", 3, "deccan pavillion", cp3a, ap3a);
        double[] cp3b = { 3500, 5000, 7500 };

        int[] ap3b = { 7, 20, 10 };
        Hotels p3b = new Hotels("pune", 3, "phoenix", cp3b, ap3b);

        hotels.add(p5a);
        hotels.add(p5b);
        hotels.add(p4a);
        hotels.add(p4b);
        hotels.add(p3a);
        hotels.add(p3b);

        // mumbai
        double[] cm5a = { 22000, 27000, 34000 };
        int[] am5a = { 4, 5, 2 };
        Hotels m5a = new Hotels("mumbai", 5, "taj", cm5a, am5a);
        double[] cm5b = { 20000, 25000, 30000 };
        int[] am5b = { 5, 2, 4 };
        Hotels m5b = new Hotels("mumbai", 5, "oberoi", cm5b, am5b);
        double[] cm4a = { 17000, 21000, 24000 };
        int[] am4a = { 3, 2, 4 };
        Hotels m4a = new Hotels("mumbai", 4, "fern", cm4a, am4a);
        double[] cm4b = { 16000, 18000, 21000 };
        int[] am4b = { 4, 5, 3 };
        Hotels m4b = new Hotels("mumbai", 4, "della", cm4b, am4b);
        double[] cm3a = { 11000, 14000, 16000 };
        int[] am3a = { 2, 4, 2 };
        Hotels m3a = new Hotels("mumbai", 3, "ibis", cm3a, am3a);
        double[] cm3b = { 10000, 13000, 15000 };
        int[] am3b = { 5, 4, 7 };
        Hotels m3b = new Hotels("mumbai", 3, "south coast", cm3b, am3b);

        hotels.add(m5a);
        hotels.add(m5b);
        hotels.add(m4a);
        hotels.add(m4b);
        hotels.add(m3a);
        hotels.add(m3b);

    }

}

class Hotels {
    String place;
    int star;
    String name;
    String[] roomtype = { "single", "double", "suite" };
    double[] cost = new double[3];
    int[] availability = new int[3]; // Track availability

    Hotels(String place, int star, String name, double[] cost, int[] availability) {
        this.place = place;
        this.star = star;
        this.name = name;
        this.cost = cost;
        this.availability = availability;
    }
}

// Class to represent a hotel
class Hotels {
    String place; // City where the hotel is located
    int star; // Star rating of the hotel
    String name; // Name of the hotel
    String[] roomtype = { "single", "double", "suite" }; // Types of rooms
    double[] cost = new double[3]; // Cost for each room type
    int[] availability = new int[3]; // Availability for each room type

    // Constructor to initialize hotel details
    Hotels(String place, int star, String name, double[] cost, int[] availability) {
        this.place = place;
        this.star = star;
        this.name = name;
        this.cost = cost;
        this.availability = availability;
    }
}

// Operations class handles user interactions for booking
class Operations extends Domain {
    // Define user inputs and properties to track booking
    int bool;
    Scanner sc = new Scanner(System.in);
    String city;
    int stars;
    String hotel;
    String room;
    double price;
    int rooms;
    double totalR, total;
    int d, m, y, d1, m1, y1, D;
    int c;

    // Display available cities
    void displayCities() {
        System.out.println(" ");
        System.out.println("Cities:");
        for (String item : cities) {
            System.out.println("        -" + item);
        }
        chooseC(); // Prompt user to choose a city
    }
    void chooseH() {
    // Prompt user to enter the desired hotel name
    System.out.println("Enter hotel name:");
    this.hotel = sc.nextLine().toLowerCase().trim(); // Read and normalize hotel name input
    int bool = 3; // Initialize a variable to validate hotel selection

    // Prompt user to enter the room type
    System.out.println("Enter roomtype:");
    this.room = sc.nextLine().toLowerCase().trim(); // Read and normalize room type input

    // Loop through the list of hotels to find the matching hotel and room type
    for (Hotels obj : hotels) {
        if (hotel.equals(obj.name)) { // Check if the hotel matches the user's input
            for (int i = 0; i < 3; i++) { // Iterate over room types (single, double, suite)
                if (room.equals(obj.roomtype[i])) { // Check if room type matches
                    // Prompt user for the number of rooms they want to book
                    System.out.println("Enter number of rooms:");
                    this.rooms = sc.nextInt();

                    // Check if requested rooms are available
                    if (rooms <= obj.availability[i]) {
                        bool = bool * 0; // Set validation flag to success
                        this.price = obj.cost[i]; // Set price per room
                        obj.availability[i] = obj.availability[i] - rooms; // Deduct booked rooms from availability
                        display(); // Proceed to display booking details
                    } else {
                        // If requested rooms exceed availability, prompt user again
                        sc.nextLine();
                        System.out.println(
                            "Requested number of rooms not available. Please choose a different option.");
                        chooseH(); // Restart hotel and room selection
                    }
                } else {
                    bool = bool * 1; // Validation flag remains unsuccessful
                }
            }
        }
    }

    // If no matching hotel or room type was found, prompt the user again
    if (bool != 0) {
        sc.nextLine(); // Clear input buffer
        System.out.println("No such option present in the domain");
        chooseH(); // Restart hotel and room selection
    } else {
        sc.nextLine(); // Clear input buffer
    }
}

void display() {
    // Display the selected hotel details and calculated costs
    sc.nextLine(); // Clear input buffer
    System.out.println("Hotel name		: " + hotel + " (" + stars + "star hotel)"); // Hotel name and star rating
    System.out.println("Room type 		: " + room); // Type of room
    System.out.println("Number of rooms 	: " + rooms); // Number of rooms booked
    this.totalR = rooms * price; // Calculate total room cost for one night
    System.out.println("Total cost		: " + totalR + "(per night)"); // Display total cost for one night
    meal(); // Proceed to meal selection
}

}
