import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminApp {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AdminDAO adminService = new AdminDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
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
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 8.");
                    scanner.next();
                }
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine().trim();
                        if (name.isEmpty()) {
                            System.out.println("Name cannot be empty.");
                            break;
                        }
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine().trim();
                        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                            System.out.println("Invalid email format.");
                            break;
                        }
                        System.out.print("Enter password: ");
                        String pass = scanner.nextLine().trim();
                        if (pass.length() < 6) {
                            System.out.println("Password must be at least 6 characters.");
                            break;
                        }
                        int userId = adminService.createUser(name, email, pass);
                        if (userId != -1) {
                            System.out.println("User created successfully with ID: " + userId);
                        } else {
                            System.out.println("Failed to create user.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter user ID to update: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric User ID.");
                            scanner.next();
                        }
                        userId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new name: ");
                        name = scanner.nextLine().trim();
                        System.out.print("Enter new email: ");
                        email = scanner.nextLine().trim();
                        System.out.print("Enter new password: ");
                        pass = scanner.nextLine().trim();
                        if (adminService.updateUser(userId, name, email, pass)) {
                            System.out.println("User updated successfully.");
                        } else {
                            System.out.println("Failed to update user. Check if the User ID exists.");
                        }
                        break;

                    case 3:
                        System.out.print("Enter user ID to delete: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric User ID.");
                            scanner.next();
                        }
                        userId = scanner.nextInt();
                        if (adminService.deleteUser(userId)) {
                            System.out.println("User deleted successfully.");
                        } else {
                            System.out.println("Failed to delete user. Check if the User ID exists.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter challenge name: ");
                        String challengeName = scanner.nextLine().trim();
                        System.out.print("Enter challenge description: ");
                        String description = scanner.nextLine().trim();
                        Challenge challenge = new Challenge(challengeName, description);
                        if (adminService.addChallenge(challenge)) {
                            System.out.println("Challenge added successfully.");
                        } else {
                            System.out.println("Failed to add challenge.");
                        }
                        break;

                    case 5:
                        List<Workout> workouts = adminService.getAllWorkouts();
                        if (workouts.isEmpty()) {
                            System.out.println("No fitness content available.");
                        } else {
                            workouts.forEach(System.out::println);
                        }
                        break;

                    case 6:
                        System.out.print("Enter user ID to review: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric User ID.");
                            scanner.next();
                        }
                        userId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter review: ");
                        String feedback = scanner.nextLine().trim();
                        if (adminService.addReview(userId, feedback)) {
                            System.out.println("Review added successfully.");
                        } else {
                            System.out.println("Failed to add review. Check if the User ID exists.");
                        }
                        break;

                    case 7:
                        System.out.print("Enter setting name: ");
                        String settingName = scanner.nextLine().trim();
                        System.out.print("Enter setting value: ");
                        String settingValue = scanner.nextLine().trim();
                        if (adminService.updateSetting(settingName, settingValue)) {
                            System.out.println("System setting updated successfully.");
                        } else {
                            System.out.println("Failed to update system setting.");
                        }
                        break;

                    case 8:
                        System.out.println("Exiting Admin Dashboard. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } while (choice != 8);

            scanner.close();
        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database.");
            e.printStackTrace();
        }
    }
}
