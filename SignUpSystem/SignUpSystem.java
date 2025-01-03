<<<<<<< HEAD
import com.fasterxml.jackson.databind.ObjectMapper;
=======
import com.fasterxml.jackson.databind.ObjectMapper;
>>>>>>> f992fa4d627cfdced7f4eee4a3b0c0de2efb31f9
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpSystem {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Display options
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.print("Choose an option (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        if (choice == 1) {
            signUp(scanner);
        } else if (choice == 2) {
            logIn(scanner);
        } else {
            System.out.println("Invalid option. Exiting...");
        }
    }

    // Sign-up method (same as before)
    public static void signUp(Scanner scanner) throws IOException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        // Email validation
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine();

        // Check if password matches confirm password
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        // Create user data object
        User user = new User(username, email, password);

        // Write user data to a JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("user_data.json"), user);

        System.out.println("Sign up successful!");
    }

    // Login method
    public static void logIn(Scanner scanner) throws IOException {
        System.out.print("Enter username/email: ");
        String usernameOrEmail = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Read user data from the JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        User storedUser = objectMapper.readValue(new File("user_data.json"), User.class);

        // Check if entered username/email and password match the stored ones
        if ((usernameOrEmail.equals(storedUser.getUsername()) || usernameOrEmail.equals(storedUser.getEmail()))
                && password.equals(storedUser.getPassword())) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    // Email validation using regular expression
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // User class to store the user data
    // User class to store the user data
static class User {
    private String username;
    private String email;
    private String password;

    // Default constructor (required for Jackson)
    public User() {
    }

    // Constructor with parameters
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

<<<<<<< HEAD
}
=======
}
>>>>>>> f992fa4d627cfdced7f4eee4a3b0c0de2efb31f9
