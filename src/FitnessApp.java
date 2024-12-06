import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class FitnessApp {
    private static int loggedInUserId = -1;

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

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                scanner.next();
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Admin functionality
                System.out.print("Enter admin username: ");
                String adminUsername = scanner.nextLine().trim();
                System.out.print("Enter admin password: ");
                String adminPassword = scanner.nextLine().trim();

                if ("Admin".equals(adminUsername) && "Admin1234".equals(adminPassword)) {
                    System.out.println("Admin login successful!");
                    AdminApp adminApp = new AdminApp();
                    adminApp.main(new String[]{});
                } else {
                    System.out.println("Invalid admin credentials. Please try again.");
                }

            } else if (choice == 2) {
                // User functionality
                System.out.println("Press 1 for Sign up");
                System.out.println("Press 2 for Login");
                System.out.print("Enter your choice: ");

                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter 1 or 2.");
                    scanner.next();
                }
                int userChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (userChoice) {
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
                        String password = scanner.nextLine().trim();
                        if (password.length() < 6) {
                            System.out.println("Password must be at least 6 characters.");
                            break;
                        }
                        loggedInUserId = adminService.createUser(name, email, password);
                        if (loggedInUserId != -1) {
                            System.out.println("User signed up successfully!");
                            User user = new User();
                            user.main(new int[]{loggedInUserId});
                        } else {
                            System.out.println("Sign-up failed. Please try again.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine().trim();
                        System.out.print("Enter password: ");
                        password = scanner.nextLine().trim();

                        if (validateUser(username, password)) {
                            System.out.println("Login successful!");
                            User user = new User();
                            user.main(new int[]{loggedInUserId});
                        } else {
                            System.out.println("Invalid username or password. Please try again.");
                        }
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                }

            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
        }
    }
}
