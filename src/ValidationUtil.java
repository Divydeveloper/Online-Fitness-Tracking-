/**
 * ValidationUtil provides utility methods for validating user inputs.
 */
public class ValidationUtil {

    /**
     * Validates a name. Name should not be empty or null.
     * @param name Name string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Validates an email address using a regex pattern.
     * @param email Email string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Validates a password. Password must be at least 6 characters long.
     * @param password Password string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
