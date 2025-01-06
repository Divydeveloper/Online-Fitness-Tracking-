import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * User class handles all user-specific functionalities like workout management,
 * challenges, and progress tracking.
 */
public class User {
    public static void main(int[] is) {
        int userId = is[0]; // Current logged-in user ID

        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                displayMenu(); // Display the user menu
                int choice = getValidatedChoice(scanner, 1, 10);

                switch (choice) {
                    case 1 -> handleAddWorkout(scanner, userDAO, userId);
                    case 2 -> handleViewWorkouts(userDAO, userId);
                    case 3 -> handleUpdateWorkout(scanner, userDAO, userId);
                    case 4 -> handleDeleteWorkout(scanner, userDAO);
                    case 5 -> userDAO.viewProgress(userId);
                    case 6 -> handleShowChallenges(userDAO);
                    case 7 -> handleJoinChallenge(scanner, userDAO, userId);
                    case 8 -> userDAO.displayChallengeHistory(userId);
                    case 9 -> handleViewReviews(userDAO, userId);
                    case 10 -> {
                        System.out.println("Exiting. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database.");
            e.printStackTrace();
        }
    }

    /**
     * Displays the fitness tracker menu.
     */
    private static void displayMenu() {
        System.out.println("\nFitness Tracker Menu:");
        System.out.println("1. Add Workout");
        System.out.println("2. View All Workouts");
        System.out.println("3. Update Workout");
        System.out.println("4. Delete Workout");
        System.out.println("5. View Progress");
        System.out.println("6. Show Challenges");
        System.out.println("7. Join Challenges");
        System.out.println("8. Display Challenge History");
        System.out.println("9. Check For Reviews");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
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

    /**
     * Handles the addition of a new workout.
     */
    private static void handleAddWorkout(Scanner scanner, UserDAO userDAO, int userId) {
        System.out.println("Enter workout type:");
        String type = scanner.nextLine().trim();
        if (type.isEmpty()) {
            System.out.println("Workout type cannot be empty.");
            return;
        }

        int duration = getValidatedInput(scanner, "Enter duration in minutes:", 1, Integer.MAX_VALUE);
        int calories = getValidatedInput(scanner, "Enter calories burned:", 1, Integer.MAX_VALUE);

        System.out.println("Enter date (YYYY-MM-DD):");
        String dateInput = scanner.nextLine().trim();
        Date date;
        try {
            date = Date.valueOf(dateInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
            return;
        }

        Workout workout = new Workout(userId, type, duration, calories, date);
        if (userDAO.addWorkout(workout)) {
            System.out.println("Workout added successfully.");
        } else {
            System.out.println("Failed to add workout.");
        }
    }

    /**
     * Handles viewing all workouts for the user.
     */
    private static void handleViewWorkouts(UserDAO userDAO, int userId) {
        List<Workout> workouts = userDAO.getWorkoutById(userId);
        if (workouts.isEmpty()) {
            System.out.println("No workouts found.");
        } else {
            workouts.forEach(System.out::println);
        }
    }

    /**
     * Handles updating an existing workout.
     */
    private static void handleUpdateWorkout(Scanner scanner, UserDAO userDAO, int userId) {
        int workoutId = getValidatedInput(scanner, "Enter workout ID to update:", 1, Integer.MAX_VALUE);

        System.out.println("Enter new workout type:");
        String newType = scanner.nextLine().trim();
        int newDuration = getValidatedInput(scanner, "Enter new duration in minutes:", 1, Integer.MAX_VALUE);
        int newCalories = getValidatedInput(scanner, "Enter new calories burned:", 1, Integer.MAX_VALUE);

        System.out.println("Enter new date (YYYY-MM-DD):");
        String dateInput = scanner.nextLine().trim();
        Date date;
        try {
            date = Date.valueOf(dateInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
            return;
        }

        Workout updatedWorkout = new Workout(userId, newType, newDuration, newCalories, date);
        updatedWorkout.setId(workoutId);
        if (userDAO.updateWorkout(updatedWorkout)) {
            System.out.println("Workout updated successfully.");
        } else {
            System.out.println("Failed to update workout. Check if the Workout ID exists.");
        }
    }

    /**
     * Handles deleting a workout by ID.
     */
    private static void handleDeleteWorkout(Scanner scanner, UserDAO userDAO) {
        int workoutId = getValidatedInput(scanner, "Enter workout ID to delete:", 1, Integer.MAX_VALUE);
        if (userDAO.deleteWorkout(workoutId)) {
            System.out.println("Workout deleted successfully.");
        } else {
            System.out.println("Failed to delete workout. Check if the Workout ID exists.");
        }
    }

    /**
     * Handles displaying available challenges.
     */
    private static void handleShowChallenges(UserDAO userDAO) {
        List<Challenge> challenges = userDAO.showChallenges();
        if (challenges.isEmpty()) {
            System.out.println("No challenges available.");
        } else {
            challenges.forEach(System.out::println);
        }
    }

    /**
     * Handles joining a challenge.
     */
    private static void handleJoinChallenge(Scanner scanner, UserDAO userDAO, int userId) {
        int challengeId = getValidatedInput(scanner, "Enter Challenge ID to join:", 1, Integer.MAX_VALUE);
        String result = userDAO.joinChallenge(userId, challengeId);
        System.out.println(result);
    }

    /**
     * Handles viewing reviews for the user.
     */
    private static void handleViewReviews(UserDAO userDAO, int userId) {
        List<String> feedbacks = userDAO.viewReview(userId);
        if (feedbacks.isEmpty()) {
            System.out.println("No reviews found.");
        } else {
            feedbacks.forEach(System.out::println);
        }
    }

    /**
     * Validates and retrieves numeric input from the user.
     */
    private static int getValidatedInput(Scanner scanner, String prompt, int min, int max) {
        System.out.println(prompt);
        int value;
        while (true) {
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid numeric value.");
                scanner.next();
            }
            value = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (value >= min && value <= max) {
                break;
            } else {
                System.out.println("Invalid input. Value must be between " + min + " and " + max + ".");
            }
        }
        return value;
    }
}
