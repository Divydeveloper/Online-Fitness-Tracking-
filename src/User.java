import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class User {
    public static void main(int[] is) {
        int userId = is[0];

        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
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
                
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 10.");
                    scanner.next();
                }
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.println("Enter workout type:");
                        String type = scanner.nextLine().trim();
                        if (type.isEmpty()) {
                            System.out.println("Workout type cannot be empty.");
                            break;
                        }
                        System.out.println("Enter duration in minutes:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric value.");
                            scanner.next();
                        }
                        int duration = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter calories burned:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric value.");
                            scanner.next();
                        }
                        int calories = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter date (YYYY-MM-DD):");
                        String dateInput = scanner.nextLine().trim();
                        Date date;
                        try {
                            date = Date.valueOf(dateInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
                            break;
                        }
                        Workout workout = new Workout(userId, type, duration, calories, date);
                        if (userDAO.addWorkout(workout)) {
                            System.out.println("Workout added successfully.");
                        } else {
                            System.out.println("Failed to add workout.");
                        }
                        break;

                    case 2:
                        List<Workout> workouts = userDAO.getWorkoutById(userId);
                        if (workouts.isEmpty()) {
                            System.out.println("No workouts found.");
                        } else {
                            workouts.forEach(System.out::println);
                        }
                        break;

                    case 3:
                        System.out.println("Enter workout ID to update:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric Workout ID.");
                            scanner.next();
                        }
                        int workoutId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new workout type:");
                        String newType = scanner.nextLine().trim();
                        System.out.println("Enter new duration in minutes:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric value.");
                            scanner.next();
                        }
                        int newDuration = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new calories burned:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric value.");
                            scanner.next();
                        }
                        int newCalories = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new date (YYYY-MM-DD):");
                        dateInput = scanner.nextLine().trim();
                        try {
                            date = Date.valueOf(dateInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid date format. Please enter in YYYY-MM-DD format.");
                            break;
                        }
                        Workout updatedWorkout = new Workout(userId, newType, newDuration, newCalories, date);
                        updatedWorkout.setId(workoutId);
                        if (userDAO.updateWorkout(updatedWorkout)) {
                            System.out.println("Workout updated successfully.");
                        } else {
                            System.out.println("Failed to update workout. Check if the Workout ID exists.");
                        }
                        break;

                    case 4:
                        System.out.println("Enter workout ID to delete:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric Workout ID.");
                            scanner.next();
                        }
                        workoutId = scanner.nextInt();
                        if (userDAO.deleteWorkout(workoutId)) {
                            System.out.println("Workout deleted successfully.");
                        } else {
                            System.out.println("Failed to delete workout. Check if the Workout ID exists.");
                        }
                        break;

                    case 5:
                        userDAO.viewProgress(userId);
                        break;

                    case 6:
                        List<Challenge> challenges = userDAO.showChallenges();
                        if (challenges.isEmpty()) {
                            System.out.println("No challenges available.");
                        } else {
                            challenges.forEach(System.out::println);
                        }
                        break;

                    case 7:
                        System.out.println("Enter Challenge ID to join:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric Challenge ID.");
                            scanner.next();
                        }
                        int challengeId = scanner.nextInt();
                        String result = userDAO.joinChallenge(userId, challengeId);
                        System.out.println(result);
                        break;

                    case 8:
                        userDAO.displayChallengeHistory(userId);
                        break;

                    case 9:
                        List<String> feedbacks = userDAO.viewReview(userId);
                        if (feedbacks.isEmpty()) {
                            System.out.println("No reviews found.");
                        } else {
                            feedbacks.forEach(System.out::println);
                        }
                        break;

                    case 10:
                        System.out.println("Exiting. Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database.");
            e.printStackTrace();
        }
    }
}
