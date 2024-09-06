package luxsoftboard;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoadingScreenController implements Initializable {

    @FXML
    private ImageView loadingImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add a delay before transitioning to the main screen
        PauseTransition delay = new PauseTransition(Duration.seconds(3)); // 3-second delay
        delay.setOnFinished(event -> showMainApp());
        delay.play();
    }

    private void showMainApp() {
    try {
        // Ensure the ImageView is part of the scene and has a window
        if (loadingImage.getScene() != null && loadingImage.getScene().getWindow() != null) {
            // Get the current stage (window)
            Stage stage = (Stage) loadingImage.getScene().getWindow();

            // Load the main application FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("luxsoftBoardHome.fxml"));
            Scene scene = new Scene(loader.load());

            // Set the new scene (main application) on the stage
            stage.setScene(scene);
            stage.show();  // Show the main app
        } else {
            System.err.println("Scene or Window is not set yet.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
