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

public class cd_entry_controller {

    Jukebox jukebox;

    @FXML
    private TextField artist_field;

    @FXML
    private Label char_length_label;

    @FXML
    private TextField title_field;

    //Clears fields and errors
    @FXML
    void clear_button(MouseEvent event) {
        artist_field.clear();
        title_field.clear();
        char_length_label.setVisible(false);
    }

    //Checks the entries and if it passes, go to the next scene, if not show the error labels
    @FXML
    void enter_button(MouseEvent event) throws IOException {
        String title = title_field.getText().trim();
        String artist = artist_field.getText().trim();

        if (entryCheck(title) && entryCheck(artist)){
            jukebox.addCD(new CD(title,artist));
            nextScene();
        }
        char_length_label.setVisible(true);
    }

    //Goes to the add song scene for CDs
    public void nextScene() throws IOException {
        Stage stage = (Stage)  char_length_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add2_cd.fxml"));
        Parent root = loader.load();
        //Gets the next scene controller and use the proper methods
        add2_cd_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setNumsFields();
        //Show the next scene
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

    public void setJukebox(Jukebox jukebox){this.jukebox = jukebox;}

}
