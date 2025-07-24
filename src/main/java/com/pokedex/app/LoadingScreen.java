package com.pokedex.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.pokedex.app.BgmPlayer;

public class LoadingScreen extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BgmPlayer.playBackgroundMusic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScreen.fxml"));
        Parent root = loader.load();

        // Resize background manually
        ImageView background = (ImageView) root.lookup("#backgroundImage");
        background.fitWidthProperty().bind(root.layoutBoundsProperty().map(b -> b.getWidth()));
        background.fitHeightProperty().bind(root.layoutBoundsProperty().map(b -> b.getHeight()));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Enhanced Pokédex by Ella and Kyle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
