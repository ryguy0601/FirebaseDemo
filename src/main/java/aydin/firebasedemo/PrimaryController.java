package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML
    private TextField ageTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Button readButton;

    @FXML
    private Button writeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label welcomeLabel;

    private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;

    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    @FXML
    public void initialize() {
        // Display welcome message with user ID if available
        if (DemoApp.currentUserId != null && !DemoApp.currentUserId.isEmpty()) {
            welcomeLabel.setText("Welcome! User ID: " + DemoApp.currentUserId);
        } else {
            welcomeLabel.setText("Welcome! Please sign in to continue.");
        }

        // Validate inputs for write operation
        writeButton.disableProperty().bind(
                nameTextField.textProperty().isEmpty()
                        .or(ageTextField.textProperty().isEmpty())
                        .or(phoneTextField.textProperty().isEmpty())
        );
    }

    @FXML
    void readButtonClicked(ActionEvent event) {
        readFirebase();
    }

    @FXML
    void writeButtonClicked(ActionEvent event) {
        addData();
    }

    @FXML
    void logoutButtonClicked(ActionEvent event) {
        try {
            // Clear current user ID
            DemoApp.currentUserId = null;
            // Navigate back to welcome screen
            DemoApp.setRoot("welcome");
        } catch (IOException e) {
            System.out.println("Error navigating to welcome screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean readFirebase() {
        key = false;
        outputTextArea.clear();

        // Asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = DemoApp.fstore.collection("Persons").get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (documents.size() > 0) {
                System.out.println("Reading data from Firebase database...");
                listOfUsers.clear();

                for (QueryDocumentSnapshot document : documents) {
                    // Get data from document
                    String name = String.valueOf(document.getData().get("Name"));
                    int age = Integer.parseInt(document.getData().get("Age").toString());
                    String phone = String.valueOf(document.getData().get("PhoneNumber"));

                    // Display in text area
                    outputTextArea.appendText("Name: " + name +
                            ", Age: " + age +
                            ", Phone: " + phone + "\n");

                    // Add to list
                    person = new Person(name, age, phone);
                    listOfUsers.add(person);
                }
            } else {
                outputTextArea.setText("No data found in the database.");
                System.out.println("No data");
            }
            key = true;
        } catch (InterruptedException | ExecutionException ex) {
            outputTextArea.setText("Error reading from database: " + ex.getMessage());
            ex.printStackTrace();
        }

        return key;
    }

    public void addData() {
        try {

            int age = Integer.parseInt(ageTextField.getText().trim());
            String name = nameTextField.getText().trim();
            String phone = phoneTextField.getText().trim();

            // Create document reference
            DocumentReference docRef = DemoApp.fstore.collection("Persons").document(UUID.randomUUID().toString());

            // Preps data
            Map<String, Object> data = new HashMap<>();
            data.put("Name", name);
            data.put("Age", age);
            data.put("PhoneNumber", phone);

            // Asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);

            // Show success message
            result.get(); // Wait for completion
            outputTextArea.setText("Data successfully added to database!\n" +
                    "Name: " + name + "\n" +
                    "Age: " + age + "\n" +
                    "Phone: " + phone);

            // Clear input fields
            nameTextField.clear();
            ageTextField.clear();
            phoneTextField.clear();

        } catch (NumberFormatException e) {
            outputTextArea.setText("Error: Age must be a valid number.");
        } catch (InterruptedException | ExecutionException e) {
            outputTextArea.setText("Error writing to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}