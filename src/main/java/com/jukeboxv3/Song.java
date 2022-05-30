package com.jukeboxv3;

//Song holds title, artist, album and length of the song

public class Song{
    private String songName;
    private String songArtist;
    private String songAlbum;
    private String songLength;

    //To create a song all fields are needed
    public Song(String songName, String songArtist, String songAlbum, int minutes, int seconds){
        this.songName = songName;
        this.songArtist = songArtist;
        this.songAlbum = songAlbum;
        setSongLength(minutes,seconds);
    }

    //Format the integers to a string depending on the input
    public void setSongLength(int minutes, int seconds){
        if (seconds < 10){
            this.songLength = minutes + ":0" + seconds;
        }
        else {
            this.songLength = minutes + ":" + seconds;
        }
    }

    public String getSongName(){return songName;}
    public String getSongArtist(){return songArtist;}
    public String getSongAlbum(){return songAlbum;}
    public String getSongLength(){return songLength;}

    //Will show in main UI
    public String songToString(){
        return "Song: " + songName +"\nArtist: " + songArtist + "\nAlbum: " + songAlbum + "\n---" + songLength;
    }

    //Comparison
    public boolean equals(Song song){
        return this.getSongName().equalsIgnoreCase(song.getSongName()) && this.songArtist.equalsIgnoreCase(song.getSongArtist());
    }
}

