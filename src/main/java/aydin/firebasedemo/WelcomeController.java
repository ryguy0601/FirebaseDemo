package aydin.firebasedemo;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class WelcomeController {
    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label statusLabel;

    @FXML
    void signInButtonClicked(ActionEvent event) {
        String email = emailTextField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both email and password");
            return;
        }

        try {
            // Get user by email
            UserRecord userRecord = DemoApp.fauth.getUserByEmail(email);

            // In a real app, you'd verify the password here
            // Firebase Admin SDK doesn't support direct password sign-in
            // This is a simplified implementation for demonstration

            statusLabel.setText("Sign in successful!");

            // Store the authenticated user ID for later use
            DemoApp.currentUserId = userRecord.getUid();

            // Navigate to the data access view
            DemoApp.setRoot("primary");

        } catch (FirebaseAuthException e) {
            statusLabel.setText("Authentication failed: " + e.getMessage());
            System.out.println("Authentication error: " + e.getMessage());
        } catch (IOException e) {
            statusLabel.setText("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void registerButtonClicked(ActionEvent event) {
        String email = emailTextField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter email & password");
            return;
        }

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisabled(false);

        try {
            UserRecord userRecord = DemoApp.fauth.createUser(request);
            statusLabel.setText("User registered successfully! UID: " + userRecord.getUid());
            System.out.println("Successfully created new user with Firebase UID: " + userRecord.getUid());

            DemoApp.currentUserId = userRecord.getUid();
            DemoApp.setRoot("primary");

        } catch (FirebaseAuthException e) {
            statusLabel.setText("Registration failed: " + e.getMessage());
            System.out.println("Error creating user: " + e.getMessage());
        } catch (IOException e) {
            statusLabel.setText("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}