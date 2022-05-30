package com.jukeboxv3;

import java.util.LinkedList;

public class User {

    //In main check that the username is not already taken
    //Add playlist, delete playlist, add song to playlist, delete song from playlist, get a song from a playlist

    private String username;
    private String password;
    private LinkedList<Playlist> userPlaylists = new LinkedList<>();

    //User constructor requires username and password, current build only uses 1 playlist to create it on construction
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        addPlaylist("Default Playlist");
    }

    //Add a playlist requires playlist name
    public void addPlaylist(String playlistName){
        userPlaylists.add(new Playlist(playlistName));
    }

    //Delete playlist at this index
    public boolean deletePlaylistAt(int index){
        if (playlistIndexCheck(index)){
            userPlaylists.remove(index);
            System.out.println("Playlist removed successfully!");
            return true;
        }
        return false;
    }

    //Add a song to a playlist at this index
    public boolean addSongToPlaylist(int playlistIndex, Song song){
        if (playlistIndexCheck(playlistIndex)) {
            userPlaylists.get(playlistIndex).addSong(song);
            System.out.println(userPlaylists.get(playlistIndex).getPlaylistName() + ": Song added successfully!");
            return true;
        }
        return false;
    }

    //Delete a song from a playlist
    public boolean deleteSongFrom(int playlistIndex, Song song){
        for (int i = 0; i < userPlaylists.get(playlistIndex).getPlaylist().size(); ++i){
            if (userPlaylists.get(playlistIndex).getPlaylist().get(i).equals(song)){
                userPlaylists.get(playlistIndex).deleteSongAt(i);
                System.out.println("Song Deleted");
                return true;
            }
        }
        System.out.println("nope");
        return false;
    }

    //Getters
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public LinkedList<Playlist> getUserPlaylists() {return userPlaylists;}

    //Checks if index is in range of the playlist
    public boolean playlistIndexCheck(int index){
        return index > -1 && index < userPlaylists.size();
    }

    public boolean playlistSongCheck(int playlistIndex, int songIndex){
        return songIndex < userPlaylists.get(playlistIndex).getPlaylist().size() && songIndex > -1;
    }

    //////Unused functions

    public void deletePlaylist(int playlistIndex){
        userPlaylists.get(playlistIndex).clearPlaylist();
    }

    //Return song for queue if it passes the index check
    public Song getSongFrom(int playlistIndex, int songIndex){
        if (playlistIndexCheck(playlistIndex) && playlistSongCheck(playlistIndex,songIndex)){
            return userPlaylists.get(playlistIndex).getPlaylist().get(songIndex);
        }
        return null;
    }

}
