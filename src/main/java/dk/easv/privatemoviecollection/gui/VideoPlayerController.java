package dk.easv.privatemoviecollection.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

public class VideoPlayerController {


    @FXML
    private MediaView mediaView;

    @FXML
    private MediaPlayer mediaPlayer;

    @FXML
    private Button btnPlay;

    @FXML
    private Slider seekSlider;

    public void play(String filePath) {

        File file = new File(filePath);

        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setOnReady(() -> {

                    //takes the size of the video
                    double width = mediaPlayer.getMedia().getWidth();
                    double height = mediaPlayer.getMedia().getHeight();

                    //takes the size of usable part of your screen
                    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                    double maxWidth = bounds.getWidth() * 0.9;
                    double maxHeight = bounds.getHeight() * 0.9;

                    double wScale = Math.min(width, maxWidth);
                    double hScale = Math.min(height, maxHeight);


                    mediaView.setFitWidth(wScale);
                    mediaView.setFitHeight(hScale);
                    mediaView.setPreserveRatio(true);

                    Stage stage = (Stage) mediaView.getScene().getWindow();
                    stage.sizeToScene();

                    seekSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());

                });

        //the slider will move while the video is playing
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            seekSlider.setValue(newValue.toSeconds());
        });


         /* THIS PART WAS SUPPOSED TO REWIND THE VIDEO WITH A SLIDER BUT IT DOESNT WORK

seekSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
      if (!isChanging) { // released the button
          if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED)
          {mediaPlayer.play();
              mediaPlayer.pause();

      }
          mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
  }

  }); */

            mediaPlayer.setOnEndOfMedia(() -> {
                btnPlay.setText("Play");
                mediaPlayer.stop();
        });
        mediaPlayer.play();
    }

    @FXML
    public void TogglePlayPause(ActionEvent event) {
        MediaPlayer.Status status = mediaPlayer.getStatus();

        switch (status) {
            case PLAYING -> {
                mediaPlayer.pause();
                btnPlay.setText("Play");
            }
            case PAUSED -> {
                mediaPlayer.play();
                btnPlay.setText("Pause");
            }

            case STOPPED -> {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
                btnPlay.setText("Pause");
            }
        }
    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }
}
