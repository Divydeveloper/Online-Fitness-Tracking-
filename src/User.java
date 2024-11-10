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
                System.out.println("Enter Your Choice");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                    	int user_id=userId;
                        System.out.println("Enter workout type:");
                        String type = scanner.nextLine();
                        System.out.println("Enter duration in minutes:");
                        int duration = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter calories burned:");
                        int calories = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter date (YYYY-MM-DD):");
                        Date date = Date.valueOf(scanner.nextLine());
                        Workout workout = new Workout(user_id, type, duration, calories, date);
                        userDAO.addWorkout(workout);
                        System.out.println("Workout added.");
                        break;

                    case 2:
                    	int id=userId;
                        List<Workout> workouts=userDAO.getWorkoutById(id);
                        workouts.forEach(w -> System.out.println(w.toString()));

                        break;

                    case 3:
                        int updateId = userId;
                        System.out.println("Enter User ID");
                    	user_id=scanner.nextInt();
                    	scanner.nextLine();
                        System.out.println("Enter new workout type:");
                        String newType = scanner.nextLine();
                        System.out.println("Enter new duration:");
                        int newDuration = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new calories burned:");
                        int newCalories = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter new date (YYYY-MM-DD):");
                        Date newDate = Date.valueOf(scanner.nextLine());

                        Workout updatedWorkout = new Workout(user_id,newType, newDuration, newCalories, newDate);
                        updatedWorkout.setId(updateId);
                        userDAO.updateWorkout(updatedWorkout);
                        System.out.println("Workout updated.");
                        break;

                    case 4:
                        System.out.println("Enter workout ID to delete:");
                        int deleteId = scanner.nextInt();
                        userDAO.deleteWorkout(deleteId);
                        System.out.println("Workout deleted.");
                        break;
                    case 5:
                    	int userid=userId;
                    	userDAO.viewProgress(userid);
                        break;
                    case 6:
                    	List<Challenge> challenges = userDAO.showChallenges();
                        challenges.forEach(w -> System.out.println(w.toString()));
                        break;
                    case 7:
                    	userid=userId;
                    	System.out.println("Enter ChallengeID: ");
                    	int challengeid=scanner.nextInt();
                    	String c=userDAO.joinChallenge(userid,challengeid);
                    	System.out.println(c);
                    	break;
                    case 8:
                    	userid=userId;
                    	userDAO.displayChallengeHistory(userid);
                        break;
                    case 9:
                    	List<String> feedbacks=userDAO.viewReview(userId);
                    	feedbacks.forEach(w -> System.out.println(w));
                        break;
                    case 10:
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
