package com.pokedex.app;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class BgmPlayer {

    private static MediaPlayer mediaPlayer;

    public static void playBackgroundMusic() {
        if (mediaPlayer == null) {
            try {
                URL resource = BgmPlayer.class.getResource("/pokemonbgmusic.mp3");
                if (resource != null) {
                    Media media = new Media(resource.toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop music
                    mediaPlayer.play();
                } else {
                    System.out.println("Audio file not found!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
