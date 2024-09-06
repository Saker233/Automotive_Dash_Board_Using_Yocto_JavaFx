/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luxsoftboard;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author Saker
 */
public class MediaController implements Initializable {
    private String path;
    private MediaPlayer mediaPlayer;
    private Label label;
    @FXML
    private ImageView openBtn;
    @FXML
    private ImageView playBtn;
    @FXML
    private ImageView backBtn;
    @FXML
    private ImageView stopBtn;
    @FXML
    private ImageView forwardBtn;
    @FXML
    private Slider volumeBar;

    @FXML
    private Slider progressBar;
    @FXML
    private MediaView mediaView;
    @FXML
    private ImageView pauseBtn;

    
//    private void handleButtonAction(ActionEvent event) {
//        System.out.println("You clicked me!");
//        label.setText("Hello World!");
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        animateVolumeBarAppearance();
//        animateProgressBarAppearance();
    }    
    
//    private void animateVolumeBarAppearance() {
//        // Create a Timeline to animate the glow effect on the volume bar
//        Timeline volumeAppearanceTimeline = new Timeline(
//            new KeyFrame(Duration.ZERO, new KeyValue(volumeBar.effectProperty(), new Glow(0.0))),
//            new KeyFrame(Duration.seconds(1), new KeyValue(volumeBar.effectProperty(), new Glow(0.8)))
//        );
//        volumeAppearanceTimeline.setCycleCount(Timeline.INDEFINITE);
//        volumeAppearanceTimeline.setAutoReverse(true);
//        volumeAppearanceTimeline.play();
//    }
    
//      private void animateProgressBarAppearance() {
//        // Create a Timeline to animate the glow effect on the progress bar
//        Timeline progressAppearanceTimeline = new Timeline(
//            new KeyFrame(Duration.ZERO, new KeyValue(progressBar.effectProperty(), new Glow(0.0))),
//            new KeyFrame(Duration.seconds(1), new KeyValue(progressBar.effectProperty(), new Glow(0.8)))
//        );
//        progressAppearanceTimeline.setCycleCount(Timeline.INDEFINITE);
//        progressAppearanceTimeline.setAutoReverse(true);
//        progressAppearanceTimeline.play();
//    }
    
//    private void OpenFileMethod(ActionEvent event) {
//        
//    }
    @FXML
    private void clickOpen(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a .mp4 file", ".mp4");
//        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        if(path != null){
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            
            //creating bindings
            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();
            
            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            
            volumeBar.setValue(mediaPlayer.getVolume()*100);
            volumeBar.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeBar.getValue()/100);
                }
            });
            
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>() {
                @Override
                public void changed(ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                    progressBar.setValue(newValue.toSeconds());
                }
            }
            );
            
            progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
                }
            });
            
            progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
                }
            });
            
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    javafx.util.Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                }
            });
            
            mediaPlayer.play();
        }
    }

    @FXML
    private void clickPlay(MouseEvent event) {
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }

    @FXML
    private void clickBack(MouseEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-5)));
        
    }

    @FXML
    private void clickStop(MouseEvent event) {
        mediaPlayer.stop();
    }

    @FXML
    private void clickForward(MouseEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(5)));
        
    }

    @FXML
    private void clickPause(MouseEvent event) {
        mediaPlayer.pause();
    }
    
}
