package com.jukeboxv3;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JukeboxMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_Page.fxml"));
            Parent root = loader.load();
            //On start, set the jukebox and the default song
            login_controller nextScene = loader.getController();
            nextScene.setJukebox(new Jukebox());
            //DEFAULT SONG IS ALWAYS RICK ASTLEY
            Song defaultSong = new Song("Never Gonna Give You Up","Rick Astley", "Whenever You Need Somebody",3,32);
            nextScene.setCurrentSong(defaultSong);
            //show the stage
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle("Griff's Jukebox");
            stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}