import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class FitnessApp {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nFitness Tracker Menu:");
                System.out.println("1. Add Workout");
                System.out.println("2. View All Workouts");
                System.out.println("3. Update Workout");
                System.out.println("4. Delete Workout");
                System.out.println("5. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Enter workout type:");
                        String type = scanner.nextLine();
                        System.out.println("Enter duration in minutes:");
                        int duration = scanner.nextInt();
                        System.out.println("Enter calories burned:");
                        int calories = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter date (YYYY-MM-DD):");
                        Date date = Date.valueOf(scanner.nextLine());

                        Workout workout = new Workout(type, duration, calories, date);
                        workoutDAO.addWorkout(workout);
                        System.out.println("Workout added.");
                        break;

                    case 2:
                        List<Workout> workouts = workoutDAO.getAllWorkouts();
                        workouts.forEach(w -> System.out.println(w.toString()));
                        break;

                    case 3:
                        System.out.println("Enter workout ID to update:");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new workout type:");
                        String newType = scanner.nextLine();
                        System.out.println("Enter new duration:");
                        int newDuration = scanner.nextInt();
                        System.out.println("Enter new calories burned:");
                        int newCalories = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new date (YYYY-MM-DD):");
                        Date newDate = Date.valueOf(scanner.nextLine());

                        Workout updatedWorkout = new Workout(newType, newDuration, newCalories, newDate);
                        updatedWorkout.setId(updateId);
                        workoutDAO.updateWorkout(updatedWorkout);
                        System.out.println("Workout updated.");
                        break;

                    case 4:
                        System.out.println("Enter workout ID to delete:");
                        int deleteId = scanner.nextInt();
                        workoutDAO.deleteWorkout(deleteId);
                        System.out.println("Workout deleted.");
                        break;

                    case 5:
                        System.out.println("Exiting.");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
