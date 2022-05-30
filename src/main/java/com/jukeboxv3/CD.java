package com.jukeboxv3;

//CD is just a playlist, but you can't delete songs from it because a CD is static

public class CD extends Playlist{
    private String cdName;
    private String cdArtist;

    //CD needs a title and artist
    public CD (String cdName,String cdArtist){
        super(cdName);
        this.cdName = cdName;
        this.cdArtist = cdArtist;
    }

    //The cd title is the album name and artist was already chosen so we just need the song title and time
    public void addSongToCD(String title, int minutes, int seconds){
        super.addSong(new Song(title,cdArtist,cdName,minutes,seconds));
    }

    //Comparison
    public boolean equals(CD cd){return cd.getCdName().equals(this.cdName) && cd.getCdArtist().equals(this.cdArtist);}

    public String getCdName(){return cdName;}
    public String getCdArtist(){return cdArtist;}

}
