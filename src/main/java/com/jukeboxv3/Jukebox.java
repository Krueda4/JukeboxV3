package com.jukeboxv3;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class Jukebox {
    /*

        -Holds Users in a Linked List
        -Holds CDs in a Linked list
        -Holds the queue in a deque linked list
        -Current song is displayed on the UI label
        -Current user is the index of the logged in user

        -User's password stored as a md5 hash and not plaintext
        -Allows login of user, creation of user or loggin in as guest
        -Dependant on current user, you can add a Song to their playlist, delete the song, delete the playlist or create the playlist
                (Current UI build only allows one playlist to be used as the user's playlist but implementation for more playlists is easy)
        -Allows adding song to queue, skipping to the next song in queue or clearing the queue
        -When creating a CD it's songs are static so they can't be deleted
        -CDs can be inserted, ejected or added to the queue in the front

     */
    private LinkedList<User> users = new LinkedList<>();
    private LinkedList<CD> cdList = new LinkedList<>();
    private Deque<Song> songQueue = new LinkedList<>();
    private Song currentSong = new Song("Never Gonna Give You Up","Rick Astley", "Whenever You Need Somebody",3,32);
    private int currentUser = 0;

    //When creating a Jukebox by deafult creates a guest user
    public Jukebox(){
        users.add(new User("Guest","password"));
    }

    //Checks passed in login_controller creates a new user, password hashed
    public boolean createUser(String username, String password){
        if (usernameCheck(username)){
            users.add(new User(username,md5Hash(password)));
            System.out.println("User added successfully");
            return true;
        }
        return false;
    }

    //Logs in user. If no user matches given username, default current user is guest
    public boolean loginUser(String username, String password){
        //Gets index of a user
        int userindex = findUser(username);

        if(userindex == -1){
            System.out.println("user not found with that name");
            return false;
        }
        else if (username.equals("Guest")){
            System.out.println("Cannot log as guest");
            return false;
        }
        else if (users.get(userindex).getPassword().equals(md5Hash(password))){
            System.out.println("User logged in successfully");
            currentUser = userindex;
            return true;
        }
        return false;
    }

    //Default user is guest user at index 0
    public void logGuest(){
        currentUser = 0;
        System.out.println("guest account logged in");
    }

    public boolean addSong2Playlist(int playlistIndex, Song song){
        if (currentUser == 0){
            return false;
        }

        users.get(currentUser).addSongToPlaylist(playlistIndex,song);
        System.out.println("Song added successfully");
        return true;
    }

    public void deleteSonginPlay(int playlistIndex, Song song){
        users.get(currentUser).deleteSongFrom(playlistIndex,song);
    }

    public void clearPlaylist(int playlistIndex){
        if (currentUser == 0){
            return;
        }
        users.get(currentUser).getUserPlaylists().get(playlistIndex).clearPlaylist();
    }

    //Add a song to the queue
    public void addToQ(Song song){
        songQueue.add(song);
    }

    //Takes the current song out of the queue and gets the next one, if it's empty play rick astley
    public boolean nextInQ(){
        Song defaultSong = new Song("Never Gonna Give You Up","Rick Astley", "Whenever You Need Somebody",3,32);
        //Queue is empty, set current song equal to the default anthem
        if (songQueue.isEmpty()){
            currentSong = defaultSong;
            return false;
        }
        // Queue isn't empty but the current song is still the default, then get the first song in the queue to be current
        else if (currentSong.equals(defaultSong)){
            currentSong = songQueue.peek();
            return true;
        }
        //Current song isn't default and the queue has a song so go to the next in queue
        songQueue.poll();
        //Now check if the queue is empty, if so, set current to the default anthem
        if (songQueue.isEmpty()){
            currentSong = defaultSong;
            return true;
        }
        //Queue isn't empty still so get the first song as the current
        currentSong = songQueue.peek();
        return true;
    }

    //When you choose a CD, shift the current queue back behind the CD tracklist,
    public boolean chooseCD(int cdIndex){
        if (cdIndexCheck(cdIndex)){
            Deque<Song> tempQueue = new ArrayDeque<>();
            tempQueue.addAll(songQueue);
            songQueue.clear();
            songQueue.addAll(cdList.get(cdIndex).getPlaylist());
            songQueue.addAll(tempQueue);
            //Delete V this line to just add the CD to the queue
            currentSong = songQueue.peek();
            System.out.println("CD Playing Successfully");
            return true;
        }
        System.out.println("CD Index Doesn't Exist");
        return false;
    }

    //UI checks the parameters to be correct
    public void addCD(CD cd){
        cdList.add(cd);
    }

    public void addSongToCD(int cdIndex, String title, int minutes, int seconds){
        cdList.get(cdIndex).addSongToCD(title,minutes,seconds);
    }

    //Returns the index of the username needed, if none found or if users only has guest account, return -1
    public int findUser(String username){
        //Checks if guest is the only user in the list
        if (users.size() == 1){
            System.out.println("Guest is only account");
            return -1;
        }
        //Linear search through the list to find index of proper user
        for (int i = 0; i < users.size(); ++i){
            if (users.get(i).getUsername().equals(username)){
                System.out.println("User found.");
                return i;
            }
        }
        //If the user is not found, return -1
        return -1;
    }

    //Hashes passwords so it's not stored in plaintext
    public String md5Hash(String passwordToHash){
        String newPassword = null;

        try{
            //Choose the hashing algorithm
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            //Get password bytes to digest
            messageDigest.update(passwordToHash.getBytes());

            //Get the hash bytes
            byte[] bytes = messageDigest.digest();

            //Convert bytes to hexadecimal
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < bytes.length; ++i){
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            newPassword = stringBuilder.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        System.out.println(newPassword);
        return newPassword;
    }

    public boolean cdIndexCheck(int index){
        return index > -1 && index < cdList.size();
    }

    //Checks if the username is already in use
    public boolean usernameCheck(String username){
        for (int i = 0; i < users.size(); ++i){
            if (users.get(i).getUsername().equals(username)){
                System.out.println("User already exists");
                return false;
            }
        }
        return true;
    }

    //Getters
    public int getCurrentUser(){return currentUser;}
    public LinkedList<Song> getUserPlaylist(){return users.get(currentUser).getUserPlaylists().get(0).getPlaylist();}
    public Song getCurrentSong(){return currentSong;}
    public Deque<Song> getSongQueue(){return songQueue;}
    public LinkedList<CD> getCdList(){return cdList;}
    public LinkedList<User> getUsers(){return users;}

    ///Unused functions for further implementation
    public boolean addPlaylist(String playlistName){
        if (currentUser == 0){
            System.out.println("Guest playlists can't be edited");
            return false;
        }

        users.get(currentUser).addPlaylist(playlistName);
        System.out.println("Playlist added successfully");
        return true;
    }

    public boolean deletePlaylist(int playlistIndex){
        if (currentUser == 0){
            return false;
        }

        users.get(currentUser).deletePlaylistAt(playlistIndex);
        return true;
    }
}
