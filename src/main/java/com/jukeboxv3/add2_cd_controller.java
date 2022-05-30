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

public class add2_cd_controller {

    Jukebox jukebox;

    @FXML
    private Label char_length_label;

    @FXML
    private Label song_added_label;

    @FXML
    private Label minutes_second_label;

    @FXML
    private TextField minutes_field;

    @FXML
    private TextField seconds_field;

    @FXML
    private TextField title_field;

    //Add song to the last CD entered until they click the exit, CDs are static
    @FXML
    void add_button(MouseEvent event) {
        String title = title_field.getText().trim();
        String minutes = minutes_field.getText().trim();
        String seconds = seconds_field.getText().trim();
        //Checks the entry and if it passes clear the fields and show the success label
        if (entryCheck(title) && songLengthCheck(Integer.parseInt(minutes),Integer.parseInt(seconds))){
            jukebox.addSongToCD(jukebox.getCdList().size()-1,title,Integer.parseInt(minutes),Integer.parseInt(seconds));
            char_length_label.setVisible(false);
            minutes_second_label.setVisible(false);
            song_added_label.setVisible(true);
            title_field.clear();
            seconds_field.clear();
            minutes_field.clear();
            return;
        }
        //If the entry doesn't pass show the requirements label
        minutes_second_label.setVisible(true);
        char_length_label.setVisible(true);
        song_added_label.setVisible(false);
    }

    //Clears fields and errors
    @FXML
    void clear_button(MouseEvent event) {
        minutes_field.clear();
        seconds_field.clear();
        title_field.clear();
        char_length_label.setVisible(false);
        minutes_second_label.setVisible(false);
        song_added_label.setVisible(false);
    }

    //Returns to the CD list screen
    @FXML
    void exit_button(MouseEvent event) throws IOException {
        Stage stage;
        stage = (Stage) minutes_second_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("List_View.fxml"));
        Parent root = loader.load();
        //Get controller and use proper methods before showing
        queue_view_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        nextScene.cdTable();
        //Show the next scene
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //Checks song length
    public boolean songLengthCheck(int minutes, int seconds){
        return minutes <= 50 && seconds <= 59 && minutes > -1 && seconds > -1;
    }

    //Has to be at least 1 letter or number
    public boolean entryCheck(String string) throws PatternSyntaxException {
        String regex = "^[A-Za-z0-9À-ÿ][A-Za-z0-9À-ÿ _-]{1,25}";
        Pattern pattern = Pattern.compile(regex);
        Matcher check = pattern.matcher(string);

        return check.matches();
    }

    //Sets the number fields to only allow numbers
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

    public void setJukebox(Jukebox jukebox){this.jukebox = jukebox;}

}
