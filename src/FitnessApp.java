import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * FitnessApp serves as the main entry point for the Fitness Tracker application,
 * providing options for both admin and user functionalities.
 */
public class FitnessApp {
    private static int loggedInUserId = -1; // Tracks the logged-in user's ID

    /**
     * Validates user credentials against the database.
     * @param name     Username of the user
     * @param password Password of the user
     * @return true if credentials are valid, false otherwise
     */
    private static boolean validateUser(String name, String password) {
        boolean isValid = false;

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
            try (var statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, password);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        isValid = true;
                        loggedInUserId = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during user validation.");
            e.printStackTrace();
        }

        return isValid;
    }

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AdminDAO adminService = new AdminDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to the Fitness Tracker App!");
            System.out.println("Press 1 for Admin functions");
            System.out.println("Press 2 for User functions");
            System.out.print("Enter your choice: ");

            int choice = getValidatedChoice(scanner, 1, 2);

            if (choice == 1) {
                handleAdminFunctions(scanner);
            } else if (choice == 2) {
                handleUserFunctions(scanner, adminService);
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
        }
    }

    /**
     * Handles admin-specific functionality.
     * @param scanner Scanner for user input
     */
    private static void handleAdminFunctions(Scanner scanner) {
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine().trim();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine().trim();

        if ("Admin".equals(adminUsername) && "Admin1234".equals(adminPassword)) {
            System.out.println("Admin login successful!");
            AdminApp.main(new String[]{}); // Call AdminApp main method
        } else {
            System.out.println("Invalid admin credentials. Please try again.");
        }
    }

    /**
     * Handles user-specific functionality like sign-up and login.
     * @param scanner Scanner for user input
     * @param adminService DAO for admin operations
     */
    private static void handleUserFunctions(Scanner scanner, AdminDAO adminService) {
        System.out.println("Press 1 for Sign up");
        System.out.println("Press 2 for Login");
        System.out.print("Enter your choice: ");

        int userChoice = getValidatedChoice(scanner, 1, 2);

        switch (userChoice) {
            case 1:
                handleUserSignUp(scanner, adminService);
                break;
            case 2:
                handleUserLogin(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }

    /**
     * Handles user sign-up functionality.
     * @param scanner Scanner for user input
     * @param adminService DAO for admin operations
     */
    private static void handleUserSignUp(Scanner scanner, AdminDAO adminService) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        if (!ValidationUtil.isValidName(name)) {
            System.out.println("Invalid name. Please try again.");
            return;
        }

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Invalid email format. Please try again.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (!ValidationUtil.isValidPassword(password)) {
            System.out.println("Password must be at least 6 characters long.");
            return;
        }

        loggedInUserId = adminService.createUser(name, email, password);
        if (loggedInUserId != -1) {
            System.out.println("User signed up successfully!");
            User.main(new int[]{loggedInUserId}); // Launch User module
        } else {
            System.out.println("Sign-up failed. Please try again.");
        }
    }

    /**
     * Handles user login functionality.
     * @param scanner Scanner for user input
     */
    private static void handleUserLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (validateUser(username, password)) {
            System.out.println("Login successful!");
            User.main(new int[]{loggedInUserId}); // Launch User module
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    /**
     * Validates user input choice within a specified range.
     * @param scanner Scanner for user input
     * @param min     Minimum valid choice
     * @param max     Maximum valid choice
     * @return Validated choice
     */
    private static int getValidatedChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice >= min && choice <= max) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
            }
        }
        return choice;
    }
}
