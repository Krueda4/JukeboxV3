package com.jukeboxv3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class main_menu_controller {

    Jukebox jukebox;

    @FXML
    private Label now_playling_label;

    @FXML
    private Label no_song_q;

    @FXML
    void menu_open_button(MouseEvent event) throws IOException {
        Stage stage;
        stage = (Stage) now_playling_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Options_Menu.fxml"));
        Parent root = loader.load();
        //Gets next scene's controller and sets jukebox
        side_options_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        //Set the scene
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //Sets the next song
    @FXML
    void next_song_button(MouseEvent event) {
        if (!jukebox.nextInQ()){
            no_song_q.setVisible(true);
            return;
        }
        setCurrentSong(jukebox.getCurrentSong());
        no_song_q.setVisible(false);
    }

    public void setCurrentSong(Song song){
        now_playling_label.textProperty().set(song.songToString());
    }

    public void setJukebox(Jukebox jukebox){
        this.jukebox = jukebox;
    }



}
