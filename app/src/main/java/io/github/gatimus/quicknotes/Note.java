package io.github.gatimus.quicknotes;

import android.util.Log;

public class Note {

    private static final String TAG = "Note";

    public Integer ID;
    public String title;
    public String body;

    public Note(){
        Log.v(TAG, "construct");
        this.title = "";
        this.body = "";
    } //constructor

    public Note(int ID, String title, String body){
        Log.v(TAG, "construct " + title);
        this.ID = ID;
        this.title = title;
        this.body = body;
    } //constructor

} //class
