package com.jukeboxv3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class enter_song_controller {

    Jukebox jukebox;

    int sendToList = 0;
    /*
    1 - Sends to playlist
    2 - Sends to queue
     */

    @FXML
    private TextField album_field;

    @FXML
    private TextField artist_field;

    @FXML
    private TextField minutes_field;

    @FXML
    private TextField seconds_field;

    @FXML
    private TextField title_field;

    @FXML
    private Label empty_label;

    @FXML
    private Label minmax_label;

    @FXML
    private Label time_label;

    //Clears the fields and clears the error messages
    @FXML
    void clear_button(MouseEvent event) {
        album_field.clear();
        artist_field.clear();
        minutes_field.clear();
        seconds_field.clear();
        title_field.clear();
        empty_label.setVisible(false);
        minmax_label.setVisible(false);
        time_label.setVisible(false);
    }

    //Checks if the song entered passes the checks, if not show the proper error messages
    @FXML
    void enter_button(MouseEvent event) throws IOException {
        String album = album_field.getText().trim();
        String title = title_field.getText().trim();
        String artist = artist_field.getText().trim();
        String minutes = minutes_field.getText().trim();
        String seconds = seconds_field.getText().trim();

        if (title.isEmpty() || artist.isEmpty() || minutes.isEmpty() || seconds.isEmpty()){
            empty_label.setVisible(true);
            minmax_label.setVisible(false);
            time_label.setVisible(false);
            return;
        }
        else if (entryCheck(title) || entryCheck(artist) || songLengthCheck(Integer.parseInt(minutes),Integer.parseInt(seconds))){
            if (album.isEmpty()){
                //set the album automatically to be a single
                send(new Song(title,artist,"Single",Integer.parseInt(minutes),Integer.parseInt(seconds)));
            }
            else if (entryCheck(album)){
                send(new Song(title,artist,album,Integer.parseInt(minutes),Integer.parseInt(seconds)));
            }
            //if the entry doesn't pass the check show the labels
        }
        minmax_label.setVisible(true);
        time_label.setVisible(true);
        empty_label.setVisible(false);
    }

    //Depending on if the enter song window popped up for the queue or the playlist, send it respective option
    public void send(Song song) throws IOException {
        if (sendToList == 1){
            sendtoPlaylist(song);
            return;
        }
        sendToQueue(song);
    }

    //Sends to the queue list and switches the scene back to the queue list
    public void sendToQueue(Song song) throws IOException {
        jukebox.addToQ(song);
        //Set the next stage
        Stage stage;
        stage = (Stage) seconds_field.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("List_View.fxml"));
        Parent root = loader.load();
        //Set the list to the queue list
        queue_view_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        nextScene.queueTable();
        //Show the next scene
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

    }

    //Sends to the current user's playlist
    public void sendtoPlaylist(Song song) throws IOException {
        jukebox.addSong2Playlist(0,song);
        //Set the stage
        Stage stage;
        stage = (Stage) seconds_field.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("List_View.fxml"));
        Parent root = loader.load();
        //Get the next controller and use the methods to populate the list
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

    //Has to be at least 1 letter or number
    public boolean entryCheck(String string) throws PatternSyntaxException {
        String regex = "^[A-Za-z0-9À-ÿ][A-Za-z0-9À-ÿ _-]{1,25}";
        Pattern pattern = Pattern.compile(regex);
        Matcher check = pattern.matcher(string);

        return check.matches();
    }

    //Checks song length
    public boolean songLengthCheck(int minutes, int seconds){
        return minutes <= 50 && seconds <= 59 && minutes > -1 && seconds > -1;
    }

    //Sets the number fields to only allow numbers in the text box
    public void setNumsFields(){
        minutes_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minutes_field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        seconds_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                seconds_field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setJukebox(Jukebox jukebox){this.jukebox=jukebox;}

}
