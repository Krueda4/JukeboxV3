package com.jukeboxv3;

import java.util.LinkedList;

public class Playlist {
    //Add song to playlist, delete song from playlist, clear playlist

    LinkedList<Song> playlistSongs = new LinkedList<>();
    private Integer songCount = 0;
    private String playlistName;


    public Playlist(String playlistName){
        this.playlistName = playlistName;
    }

    //Adds song to playlist if it passes all the checks
    public void addSong(Song song){

        playlistSongs.add(song);
        songCount++;
        System.out.println("Song added successfully.");

    }

    //Delete song at index
    public boolean deleteSongAt(int index){
        if (index <= playlistSongs.size() - 1 && index > -1) {
            playlistSongs.remove(index);
            songCount--;
            System.out.println("Song deleted successfully.");
            return true;
        }
        return false;
    }

    //Clears the list
    public void clearPlaylist(){
        playlistSongs.clear();
    }

    public boolean equals(Playlist playlist){return playlist.getPlaylistName().equals(this.playlistName);}

    //Getters
    public String getSongCount(){return songCount.toString();}
    public String getPlaylistName(){return playlistName;}
    public LinkedList<Song> getPlaylist(){return playlistSongs;}
}
