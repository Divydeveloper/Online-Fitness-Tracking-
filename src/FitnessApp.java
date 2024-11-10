import java.sql.*;
import java.util.Scanner;

public class FitnessApp {
	private static int loggedInUserId = -1;
	private static boolean validateUser(String name, String password) {
        boolean isValid = false;

        try {
            // Establish database connection
            Connection connection = DatabaseConnection.getConnection();

            // Prepare the SQL query to check the user's credentials
            String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if any record was returned
            if (resultSet.next()) {
                isValid = true;
                loggedInUserId = resultSet.getInt("id");
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }
    public static void main(String[] args) {
        
    	try (Connection connection = DatabaseConnection.getConnection()){
	        AdminDAO adminService = new AdminDAOImpl(connection);
	    	Scanner scanner = new Scanner(System.in);
	
	        System.out.println("Welcome to the Fitness Tracker App!");
	        System.out.println("Press 1 for Admin functions");
	        System.out.println("Press 2 for User functions");
	
	        int choice = scanner.nextInt();
	        scanner.nextLine();
	
	        if(choice==1) {
	                // Go to Admin functionality
	            	System.out.print("Enter your username: ");
	                String inputUsername = scanner.nextLine();
	                System.out.print("Enter your password: ");
	                String inputPassword = scanner.nextLine();
	                // Check if the entered credentials match
	                if (inputUsername.equals("Admin") && inputPassword.equals("Admin1234")) {
	                    System.out.println("Login successful! Welcome!");
	                    AdminApp admin = new AdminApp();
	                    admin.main(new String[] {}); 
	
	                } else {
	                    System.out.println("Invalid username or password. Please try again."+inputUsername+inputPassword);
	                }
	        }else if(choice==2) {
	                // Go to User functionality
	            	System.out.println("Welcome to the Fitness Tracker App!");
	                System.out.println("Press 1 for Sign up");
	                System.out.println("Press 2 for Login");
	                
	                int user_choice = scanner.nextInt();
	                scanner.nextLine();
	                switch (user_choice) {
	                    case 1:
	                    	System.out.print("Enter name: ");
		                    String name = scanner.nextLine();
		                    System.out.print("Enter email: ");
		                    String email = scanner.nextLine();
		                    System.out.print("Enter password: ");
		                    String pass = scanner.nextLine();
		                    loggedInUserId=adminService.createUser(name, email, pass);
		                    
		                    User user = new User();
		                    user.main(new int[] {loggedInUserId}); 
		                    break;
	                    case 2:
			            	System.out.print("Enter your username: ");
			                String Username = scanner.nextLine();
			                System.out.print("Enter your password: ");
			                String Password = scanner.nextLine();
			                // Check if the entered credentials match
			                if (validateUser(Username, Password)) {
			                    System.out.println("Login successful! Welcome!");
			                    User users = new User();
			                    users.main(new int[] {loggedInUserId}); 
			                    break;
			
			                } else {
			                    System.out.println("Invalid username or password. Please try again.");
			                    break;
			                }
	                    default:
	    	                System.out.println("Invalid choice. Please press 1 or 2.");
	                }
	        }else {
	                System.out.println("Invalid choice. Please press 1 or 2.");
	        }
	
	        scanner.close();
        }catch(SQLException e) {
        	e.printStackTrace();
        }
    }
}
