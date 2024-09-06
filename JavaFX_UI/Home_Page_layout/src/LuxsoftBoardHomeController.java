package luxsoftboard;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.ModernSkin;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.util.Duration;
// for sound play
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;

public class LuxsoftBoardHomeController {

    private volatile boolean running = true;
    private MediaPlayer soundPlayer;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView spotifyIcon, mapsIcon, homeIcon, youtubeIcon, mediaIcon;

    private WebView sharedWebView;

    @FXML
    private GridPane innerGridPane;

    @FXML
    private Label timeLabel;

    // for Gauge
    private Gauge speedGauge;

    @FXML
    private AnchorPane mediaPane;

    @FXML
    public void initialize() {
        // Get the static WebView instance from the SharedWebController
        sharedWebView = SharedWebController.getSharedWebView();
        loadGauge();

        // Initialize the time display
        setTime();

    }

    @FXML
    private void mapsClk() {
        loadURL("http://127.0.0.1:5000");
    }

    @FXML
    private void youtubeClk() {
        loadURL("https://www.youtube.com");
    }

    @FXML
    private void homeClk() {
        sharedWebView.getEngine().load(null);
        gridPane.getChildren().remove(sharedWebView);
    }

    @FXML
    private void spotifyClk() {
        loadURL("https://www.spotify.com");
    }

    @FXML
    private void mediaClk() {
        loadMedia();
    }

    private void loadMedia() {
        if (mediaPane == null) {
        System.err.println("mediaPane is not initialized");
        return;
    }

    // Remove mediaPane if it's already added to the GridPane
    gridPane.getChildren().remove(mediaPane);

    // Add mediaPane to the GridPane at the specified position
    gridPane.add(mediaPane, 1, 0);

    }

    private void loadURL(String url) {
        // Remove the WebView if it's already added to the GridPane
        gridPane.getChildren().remove(sharedWebView);

        // Load the new URL
        sharedWebView.getEngine().load(url);

        // Add the WebView to the GridPane at the specified position
        gridPane.add(sharedWebView, 1, 0);
    }

    public void stop() {
        running = false; // This will stop the thread
    }

    private void loadGauge() {
        // Initialize the gauge
        speedGauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.MODERN)
                .title("Speed")
                .unit("km/h")
                .minValue(0)
                .maxValue(240)
                .build();

        speedGauge.setSkin(new ModernSkin(speedGauge));
        speedGauge.setPrefWidth(133);
        speedGauge.setPrefHeight(200);

        // Add the gauge to the innerGridPane at row 1, column 0
        innerGridPane.add(speedGauge, 0, 1);

        // Initialize sound player
        initializeSound();

        // Simulate speed updates in a separate thread
        new Thread(() -> {
            int speed = 0;
            boolean increasing = true;
            while (running) {
                final int currentSpeed = speed;
                Platform.runLater(() -> {
                    speedGauge.setValue(currentSpeed);

                    if (soundPlayer != null) {
                        if (currentSpeed >= 60) {
                            if (soundPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                                soundPlayer.play();
                            }
                        } else {
                            if (soundPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                                soundPlayer.stop();
                            }
                        }
                    }
                });

                if (increasing) {
                    speed += 5;
                    if (speed >= 100) {
                        increasing = false;
                    }
                } else {
                    speed -= 5;
                    if (speed <= 0) {
                        increasing = true;
                    }
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void setTime() {
        // Initialize the clock
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Set initial time
        timeLabel.setText(LocalTime.now().format(timeFormat));

        // Create a timeline to update the clock every second
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    Platform.runLater(() -> timeLabel.setText(LocalTime.now().format(timeFormat)));
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void initializeSound() {
        URL resourceUrl = getClass().getResource("img/beepSound.mp3");
        if (resourceUrl == null) {
            System.err.println("Sound file not found.");
            return;
        }
        String soundFilePath = resourceUrl.toExternalForm();
        System.out.println("Sound file path: " + soundFilePath); // Print path for debugging
        Media sound = new Media(soundFilePath);
        soundPlayer = new MediaPlayer(sound);
    }

}
