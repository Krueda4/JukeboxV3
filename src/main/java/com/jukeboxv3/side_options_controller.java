package com.jukeboxv3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class side_options_controller {

    Jukebox jukebox;

    @FXML
    private Label guest_access_label;

    @FXML
    private Label now_playling_label;

    @FXML
    private Label no_song_q;

    @FXML
    private Button view_q;

    //Sets the next scene to the CD lsit
    @FXML
    void choose_CD_button(MouseEvent event) throws IOException {
        Stage stage;
        stage = (Stage) guest_access_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("List_View.fxml"));
        Parent root = loader.load();
        //Gets the next scene controller and uses the proper methods
        queue_view_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        nextScene.cdTable();
        //Sets the scene
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //Logout and send back to the login menu
    @FXML
    void logout_button(MouseEvent event) throws IOException {
        Stage stage;
        stage = (Stage) guest_access_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_Page.fxml"));
        Parent root = loader.load();
        //Sets the next scenes methods
        login_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        //Show the stage
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void my_playlists_button(MouseEvent event) throws IOException {
        //If the user is the guest, they can't have a playlist
        if (jukebox.getCurrentUser() == 0){
            guest_access_label.setVisible(true);
            return;
        }
        //Set the next stage to the playlist list and options
        Stage stage;
        stage = (Stage) guest_access_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("List_View.fxml"));
        Parent root = loader.load();
        //Get the next scene controller and set the proper table
        queue_view_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        nextScene.playlistTable();
        //Show the scene
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void view_q_button(MouseEvent event) throws IOException {
        //sets the stage to the queue list
        Stage stage;
        stage = (Stage) guest_access_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("List_View.fxml"));
        Parent root = loader.load();
        //Sets next controller to the queue list
        queue_view_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        nextScene.queueTable();
        //Show the scene
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

    }

    //Returns to the previous main menu
    @FXML
    void return_main_button(MouseEvent event) throws IOException {
        Stage stage;
        stage = (Stage) guest_access_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));
        Parent root = loader.load();

        main_menu_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());

        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //Sets next song in queue
    @FXML
    void next_song_button(MouseEvent event) throws IOException {
        if (!jukebox.nextInQ()){
            no_song_q.setVisible(true);
            return;
        }
        setCurrentSong(jukebox.getCurrentSong());
        no_song_q.setVisible(false);
    }

    public void setCurrentSong(Song song){
        now_playling_label.textProperty().setValue(song.songToString());
    }

    public void setJukebox(Jukebox jukebox){
        this.jukebox = jukebox;
    }

}
