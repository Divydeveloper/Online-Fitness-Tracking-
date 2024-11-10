import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminApp {

    public static void main(String[] args) {
    	try (Connection connection = DatabaseConnection.getConnection()){
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
	            choice = scanner.nextInt();
	            scanner.nextLine();  // Consume newline
	
	            switch (choice) {
	                case 1:
	                    System.out.print("Enter name: ");
	                    String name = scanner.nextLine();
	                    System.out.print("Enter email: ");
	                    String email = scanner.nextLine();
	                    System.out.print("Enter password: ");
	                    String pass = scanner.nextLine();
	                    int userid=adminService.createUser(name, email, pass);
	                    if(userid != -1) {
	                    	System.out.print("User Registered");
	                    }else {
	                    	System.out.print("User Not Registed");
	                    }
	                    break;
	
	                case 2:
	                    System.out.print("Enter user ID to update: ");
	                    int userId = scanner.nextInt();
	                    scanner.nextLine();  // Consume newline
	                    System.out.print("Enter new name: ");
	                    name = scanner.nextLine();
	                    System.out.print("Enter new email: ");
	                    email = scanner.nextLine();
	                    System.out.print("Enter new password: ");
	                    pass = scanner.nextLine();
	                    System.out.println(adminService.updateUser(userId, name, email, pass));
	                    break;
	
	                case 3:
	                    System.out.print("Enter user ID to delete: ");
	                    userId = scanner.nextInt();
	                    System.out.println(adminService.deleteUser(userId));
	                    break;
	                
	                case 4:
	                	System.out.print("Enter Challenge Name: ");
	                	String cName=scanner.nextLine();
	                	System.out.print("Enter challenge Description: ");
	                	String description=scanner.nextLine();
	                	Challenge challenge= new Challenge(cName, description);
                        adminService.addChallenge(challenge);
                        System.out.println("Challenge added.");
                        break;
                        
	                case 5:
	                	List<Workout> workouts = adminService.getAllWorkouts();
                        workouts.forEach(w -> System.out.println(w.toString()));
                        break;
                        
	                case 6:
	                	System.out.print("Enter user ID: ");
	                    userId = scanner.nextInt();
	                    scanner.nextLine();  // Consume newline
	                    System.out.print("Enter Feedback: ");
	                    String Feedback = scanner.nextLine();
	                    System.out.println(adminService.userInteraction(userId, Feedback));
	                    break;
	                    
	                case 7:
	                    System.out.print("Enter setting value: ");
	                    String settingName = scanner.nextLine();
	                    System.out.print("Enter setting name: ");
	                    String settingValue = scanner.nextLine();
	                    System.out.println(adminService.updateSetting(settingName, settingValue));
	                    break;
	
	                case 8:
	                    System.out.println("Exiting...");
	                    break;
	
	                default:
	                    System.out.println("Invalid choice. Try again.");
	            }
	        } while (choice != 8);
	
	        scanner.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
}
