package com.jukeboxv3;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class queue_view_controller {

    Jukebox jukebox;

    int currentList = 0;
    /*
    1 - CD List
    2 - Queue List
    3 - My Playlist
     */
    @FXML
    private Label now_playling_label;

    @FXML
    private Label list_label;

    @FXML
    private Label no_song_q;

    @FXML
    private Button next_song_button;

    @FXML
    private Button select_button;

    @FXML
    private Button return_button;

    @FXML
    private ComboBox<String> option_picker;

    @FXML
    private TableView<Song> songs_table;

    @FXML
    private TableView<CD> cd_table;

    //Skips to the next song, and sets the song label to the correct song
    @FXML
    void next_song_button(MouseEvent event) {
        if (!jukebox.nextInQ()){
            no_song_q.setVisible(true);
            return;
        }
        if (currentList == 2){
            queueTable();
        }
        setCurrentSong(jukebox.getCurrentSong());
        no_song_q.setVisible(false);
    }

    //Depending on the current list that is shown, it will send the option to the correct method
    @FXML
    void select_option_button(MouseEvent event) throws IOException {
        String option = option_picker.getSelectionModel().getSelectedItem();
        if (option == null){
            return;
        }
        else if (currentList == 1){
            cdOptions(option);
        }
        else if (currentList == 2){
            queueOptions(option);
        }
        else if (currentList == 3){
            playlistOptions(option);
        }
    }

    //Decides the option chosen for the queue table
    public void queueOptions(String option) throws IOException {
//        Options - "Add Song","Delete Song","Clear Queue"
        if (option.equals("Add Song")){
            //Switches scene to the add song window
            Stage stage;
            stage = (Stage) now_playling_label.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter_song_menu.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            //Gets add song controller and use the proper methods to set it up
            enter_song_controller nextScene = fxmlLoader.getController();
            nextScene.sendToList = 2;
            nextScene.setJukebox(this.jukebox);
            nextScene.setNumsFields();
            //Show the new scene
            stage.setScene(new Scene(root1));
            stage.show();
        }
        //Clear queue option
        else if (option.equals("Clear Queue")){
            jukebox.getSongQueue().clear();
            queueTable();
        }
    }

    //Chooses the option for the playlist table scene
    public void playlistOptions(String options) throws IOException {
        try {
//        Options - "Add Song","Delete Song","Clear Playlist","Add to Queue" OPTIONS
            if (options.equals("Add Song")) {
                Stage stage;
                stage = (Stage) now_playling_label.getScene().getWindow();
                stage.close();
                //Loads the next Scene
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("enter_song_menu.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                //Set the next scene's int to send it back the the playlist
                enter_song_controller nextScene = fxmlLoader.getController();
                nextScene.sendToList = 1;
                nextScene.setNumsFields();
                nextScene.setJukebox(this.jukebox);
                //show the enter song window
                stage.setScene(new Scene(root1));
                stage.show();

             //Deletes selected song
            } else if (options.equals("Delete Song")) {
                //Get the selected song item from the table
                Song song = songs_table.getSelectionModel().getSelectedItem();
                //Delete the song
                jukebox.deleteSonginPlay(0, song);
                //Update the table
                playlistTable();
                no_song_q.setVisible(false);
                return;

            //Clear playlist option
            } else if (options.equals("Clear Playlist")) {
                jukebox.clearPlaylist(0);
                //updates playlist table
                playlistTable();
                no_song_q.setVisible(false);
                return;

            //Adds the selected song to queue
            } else if (options.equals("Add to Queue")) {
                jukebox.addToQ(songs_table.getSelectionModel().getSelectedItem());
                no_song_q.setVisible(false);
                return;
            }
        }
        catch (Exception e){
            System.out.println("None selected");
        }
    }

    //Goes through the options for the CD table
    public void cdOptions(String options) throws IOException {
//        Options - "Enter CD", "Eject CD","Play Now"
        try {
            if (options.equals("Enter CD")) {
                Stage stage;
                stage = (Stage) now_playling_label.getScene().getWindow();
                stage.close();
                //Loads the next Scene
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cd_entry.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                //Set the next scene's int to send it back the the playlist
                cd_entry_controller nextScene = fxmlLoader.getController();
                nextScene.setJukebox(this.jukebox);
                //show the enter song window
                stage.setScene(new Scene(root1));
                stage.show();
            } else if (options.equals("Eject CD")) {
                CD cd = cd_table.getSelectionModel().getSelectedItem();
                for (int i = 0; i < jukebox.getCdList().size(); ++i) {
                    if (jukebox.getCdList().get(i).equals(cd)) {
                        jukebox.getCdList().remove(i);
                        cdTable();
                        no_song_q.setVisible(false);
                        return;
                    }
                }
            } else if (options.equals("Play Now")) {
                CD cd = cd_table.getSelectionModel().getSelectedItem();
                for (int i = 0; i < jukebox.getCdList().size(); ++i) {
                    if (jukebox.getCdList().get(i).equals(cd)) {
                        jukebox.chooseCD(i);
                        setCurrentSong(jukebox.getCurrentSong());
                        no_song_q.setVisible(false);
                        return;
                    }
                }
            }
        }catch (Exception e){
            System.out.println("None Chosen");
        }

    }

    //These three methods update and populate the table for CDs and Songs
    public void playlistTable(){
        //3 sets current list to the playlist, also sets the page title and options for the combo box
        currentList = 3;
        list_label.textProperty().setValue("My Playlist");
        setplaylistOptions();
        //send the current user's playlist to an observable list
        ObservableList<Song> songs = FXCollections.observableList(jukebox.getUserPlaylist());

        songs_table.setItems(songs);
        //Create the columns for the song
        TableColumn<Song, String> titlecol = new TableColumn<>("Song Title");
        TableColumn<Song, String> artistcol = new TableColumn<>("Song Artist");
        TableColumn<Song, String> lengthcol = new TableColumn<>("Length");
        //Iterate through the list and add the items to the columns
        for (int i = 0; i < jukebox.getUserPlaylist().size(); ++i){
            titlecol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSongName()));
            artistcol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSongArtist()));
            lengthcol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSongLength()));
        }
        //Set the columns into the tableview
        songs_table.getColumns().setAll(titlecol,artistcol,lengthcol);

    }
    /////////////////////////////////////////////////////////////
    public void queueTable(){
        //Sets current list to the queue, label to queue, and populates the combobox
        currentList = 2;
        list_label.textProperty().setValue("Queue");
        setqueueOptions();
        //Has to be a list to use ObservableList, covert it from the priority queue
        LinkedList<Song> newQueue = new LinkedList<>();
        newQueue.addAll(jukebox.getSongQueue());
        ObservableList<Song> songs = FXCollections.observableList(newQueue);

        songs_table.setItems(songs);
        //Set the titles of each column
        TableColumn<Song, String> titlecol = new TableColumn<>("Song Title");
        TableColumn<Song, String> artistcol = new TableColumn<>("Song Artist");
        TableColumn<Song, String> lengthcol = new TableColumn<>("Length");

        //for the length of the list, get all the items
        for (int i = 0; i < newQueue.size(); ++i){
            titlecol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSongName()));
            artistcol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSongArtist()));
            lengthcol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSongLength()));
        }
        //Set the columns into the tableview
        songs_table.getColumns().setAll(titlecol,artistcol,lengthcol);
    }
    /////////////////////////////////////////////////////////////////////////
    public void cdTable(){
        //Set current list to the cd, set the cd options for combobox, set the label, set the song table to not visible and the CD table visible
        currentList = 1;
        setcdOptions();
        list_label.textProperty().setValue("CD's");
        songs_table.setVisible(false);
        cd_table.setVisible(true);
        //List of CDs to an observable list
        ObservableList<CD> cds = FXCollections.observableList(jukebox.getCdList());
        cd_table.setItems(cds);
        //Set table columns names
        TableColumn<CD,String> titleCol = new TableColumn<>("CD Title");
        TableColumn<CD,String> artistCol = new TableColumn<>("Artist");
        TableColumn<CD,String> songNumCol = new TableColumn<>("#Songs");
        //For every CD, set it's values
        for(int i = 0; i < jukebox.getCdList().size(); ++i){
            titleCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCdName()));
            artistCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCdArtist()));
            songNumCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getSongCount()));
        }
        //Set the columns to the table
        cd_table.getColumns().setAll(titleCol,artistCol,songNumCol);

    }

    //For the hamburger button, returns to the main view of just the song
    @FXML
    void return_main_button(MouseEvent event) throws IOException {
        Stage stage;
        stage = (Stage) now_playling_label.getScene().getWindow();
        //Loads the next scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));
        Parent root = loader.load();
        //Gets the next scene's controller and uses the appropriate methods set the scene
        main_menu_controller nextScene = loader.getController();
        nextScene.setJukebox(this.jukebox);
        nextScene.setCurrentSong(jukebox.getCurrentSong());
        //Set the new scene without closing the current window
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //These set options for the combo box by their respective table
    public void setqueueOptions(){
        LinkedList<String> queueOptions = new LinkedList<>(Arrays.asList("Add Song","Clear Queue"));
        ObservableList<String> options = (ObservableList<String>) FXCollections.observableList(queueOptions);

        option_picker.setItems(options);
    }
    ////////////////////////
    public void setplaylistOptions(){
        LinkedList<String> playlistOptions = new LinkedList<>(Arrays.asList("Add Song","Delete Song","Clear Playlist","Add to Queue"));
        ObservableList<String> options = (ObservableList<String>) FXCollections.observableList(playlistOptions);

        option_picker.setItems(options);
    }
    ///////////////////////
    public void setcdOptions(){
        LinkedList<String> cdOptions = new LinkedList<>(Arrays.asList("Enter CD", "Eject CD","Play Now"));
        ObservableList<String> options = (ObservableList<String>) FXCollections.observableList(cdOptions);

        option_picker.setItems(options);
    }

    public void setCurrentSong(Song song){
        now_playling_label.textProperty().setValue(song.songToString());
    }

    public void setJukebox(Jukebox jukebox){
        this.jukebox = jukebox;
    }
}
