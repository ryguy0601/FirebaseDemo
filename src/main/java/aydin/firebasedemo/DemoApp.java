package aydin.firebasedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;

public class DemoApp extends Application {
    public static Scene scene;
    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static String currentUserId; // To store the current user's ID

    private final FirestoreContext contextFirebase = new FirestoreContext();

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize Firebase services
        fstore = contextFirebase.firebase();
        fauth = FirebaseAuth.getInstance();

        // Set initial scene to welcome screen
        scene = new Scene(loadFXML("welcome"), 600, 450);
        stage.setTitle("Firebase Demo Application");
        stage.setScene(scene);
        stage.show();
    }

    // Change the root of the scene to load a different view
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Load FXML file and return the root node
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DemoApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}