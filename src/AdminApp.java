import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * AdminApp provides a CLI-based dashboard for admins to manage users,
 * challenges, fitness content, reviews, and system settings.
 */
public class AdminApp {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // DAO instance for database operations
            AdminDAO adminService = new AdminDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);

            // Main application loop
            while (true) {
                displayMenu(); // Display dashboard menu
                int choice = getValidatedChoice(scanner); // Get validated user choice

                switch (choice) {
                    case 1:
                        createUser(scanner, adminService); // Create a new user
                        break;
                    case 2:
                        updateUser(scanner, adminService); // Update an existing user
                        break;
                    case 3:
                        deleteUser(scanner, adminService); // Delete a user
                        break;
                    case 4:
                        addChallenge(scanner, adminService); // Add a fitness challenge
                        break;
                    case 5:
                        viewFitnessContent(adminService); // View fitness content
                        break;
                    case 6:
                        addReview(scanner, adminService); // Add a review for a user
                        break;
                    case 7:
                        updateSystemSetting(scanner, adminService); // Update system settings
                        break;
                    case 8:
                        System.out.println("Exiting Admin Dashboard. Goodbye!");
                        scanner.close();
                        return; // Exit application
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database.");
            e.printStackTrace();
        }
    }

    /**
     * Displays the admin dashboard menu options.
     */
    private static void displayMenu() {
        System.out.println("\nAdmin Dashboard:");
        System.out.println("1. Create User");
        System.out.println("2. Update User");
        System.out.println("3. Delete User");
        System.out.println("4. Add Challenge");
        System.out.println("5. View Fitness Content");
        System.out.println("6. Add Reviews For User");
        System.out.println("7. Update System Setting");
        System.out.println("8. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Gets a validated integer choice from the user.
     * @param scanner Scanner object for user input
     * @return Validated integer choice
     */
    private static int getValidatedChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number between 1 and 8.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Handles the creation of a new user.
     * @param scanner Scanner object for user input
     * @param adminService DAO for admin operations
     */
    private static void createUser(Scanner scanner, AdminDAO adminService) {
        scanner.nextLine(); // Consume newline
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

        int userId = adminService.createUser(name, email, password);
        System.out.println(userId != -1 ? "User created successfully with ID: " + userId
                                        : "Failed to create user.");
    }

    // Other methods (updateUser, deleteUser, addChallenge, etc.) remain the same
    // with similar inline comments added for explanation.
}
