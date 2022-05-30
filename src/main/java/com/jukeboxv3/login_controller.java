package com.jukeboxv3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class login_controller {

    Jukebox jukebox;

    @FXML
    private Label empty_field_label;

    @FXML
    private Label now_playling_label;

    @FXML
    private Label password_requirements_label;

    @FXML
    private Label username_requirements_label;

    @FXML
    private Label user_added_label;

    @FXML
    private Label no_song_q;

    @FXML
    private Label username_exists_label;

    @FXML
    private Label login_error_label;

    @FXML
    private PasswordField password_field;

    @FXML
    private TextField username_field;

    //Check if the account created is properly formatted
    @FXML
    void create_acc_pressed(MouseEvent event) {
        //When you create the account add it to the list of users
        if (userLoginCheck() && jukebox.createUser(username_field.getText(),password_field.getText())){
            user_added_label.setVisible(true);
            empty_field_label.setVisible(false);
            password_requirements_label.setVisible(false);
            username_requirements_label.setVisible(false);
            login_error_label.setVisible(false);
            username_exists_label.setVisible(false);
            return;
        }
        else if (!userLoginCheck()){
            return;
        }
        username_exists_label.setVisible(true);
        username_requirements_label.setVisible(false);
        password_requirements_label.setVisible(false);
        user_added_label.setVisible(false);
        empty_field_label.setVisible(false);
        login_error_label.setVisible(false);
    }

    //Sets the current user to guest
    @FXML
    void guest_button_press(MouseEvent event) throws IOException {
        jukebox.logGuest();
        toMainView();
    }

    //If the checks pass, set the current user to the logged user
    @FXML
    void login_button_press(MouseEvent event) throws IOException {
        if (setCurrentUser()){
            toMainView();
        }
    }

    //Sets the next song
    @FXML
    void next_song_button(MouseEvent event){
        if (!jukebox.nextInQ()){
            no_song_q.setVisible(true);
            return;
        }
        setCurrentSong(jukebox.getCurrentSong());
        no_song_q.setVisible(false);
    }

    //Checks when the user logs in and sets the proper label depending on the situation
    public boolean userLoginCheck(){
        String passwd = password_field.getText();
        String username = username_field.getText();
        //Checks if field is empty
        if (password_field.getText().isEmpty() || username_field.getText().isEmpty()){
            empty_field_label.setVisible(true);
            password_requirements_label.setVisible(false);
            username_requirements_label.setVisible(false);
            user_added_label.setVisible(false);
            login_error_label.setVisible(false);
            username_exists_label.setVisible(false);
            return false;
        }
        //Checks if password passes check
        else if (!paswdCheck(passwd)){
            password_requirements_label.setVisible(true);
            username_requirements_label.setVisible(false);
            empty_field_label.setVisible(false);
            user_added_label.setVisible(false);
            login_error_label.setVisible(false);
            username_exists_label.setVisible(false);
            return false;
        }
        //Checks if username passes check
        else if(!usernameCheck(username)){
            username_requirements_label.setVisible(true);
            password_requirements_label.setVisible(false);
            empty_field_label.setVisible(false);
            user_added_label.setVisible(false);
            login_error_label.setVisible(false);
            username_exists_label.setVisible(false);
            return false;
        }
        return true;
    }

    //Sets the current user to the logged in user, if the login is correct
    public boolean setCurrentUser(){
        if(userLoginCheck() && jukebox.loginUser(username_field.getText(), password_field.getText())
        ){
            return true;
        }
        //If the login isn't correct show the proper label
        login_error_label.setVisible(true);
        password_requirements_label.setVisible(false);
        username_requirements_label.setVisible(false);
        user_added_label.setVisible(false);
        empty_field_label.setVisible(false);
        username_exists_label.setVisible(false);
        return false;
    }

    //Method to send to the main view that just shows the song
    public void toMainView() throws IOException {
        Stage stage;
        stage = (Stage) now_playling_label.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));
        Parent root = loader.load();
        //Gets the next scene's controller and use the proper method
        main_menu_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        //Sets the next scene
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public boolean usernameCheck(String username) throws PatternSyntaxException {
        /*
        Username Requirements
        -Min 4 Characters
        -Max 20 Characters
        -Underscore and Hyphen Allowed
        -No Spaces
        -No Special Characters
         */
        String regex = "^[A-Za-z0-9_-]{4,20}";
        Pattern pattern = Pattern.compile(regex);
        Matcher check = pattern.matcher(username);

        return check.matches();
    }

    public boolean paswdCheck(String password) throws PatternSyntaxException{
        /*
        Password Requirements
        - 8-20 Characters
        -Must contain a number
        -Must contain an upper and lowercase letter
        -Must contain a special character
        -Can't contain spaces
         */
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!*_;:'?.,<>{}`|/~@#$%^&-+=()])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void setCurrentSong(Song song){
        now_playling_label.textProperty().setValue(song.songToString());
    }

    public void setJukebox(Jukebox jukebox){
        this.jukebox = jukebox;
    }


}
