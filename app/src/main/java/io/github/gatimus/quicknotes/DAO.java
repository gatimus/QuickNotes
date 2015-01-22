package io.github.gatimus.quicknotes;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    private static final String TAG = "DAO";
    private static final String dbName = "noteDB.db";
    private Resources resources;

    public DAO(Context context, Resources resources){
        super(context, dbName, null, BuildConfig.VERSION_CODE, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase dbObj) {
                Log.w(TAG, "Corruption");
                //TODO
            } //onCorruption
        });
        this.resources = resources;
        Log.v(TAG, "construct");
    } //constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "Create");
        try {
            db.execSQL(resources.getString(R.string.create_table));
        } catch (SQLiteException e){
            Log.e(TAG, e.toString());
        }
    } //onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrade database from " + String.valueOf(oldVersion) + " to " + String.valueOf(newVersion));
        //TODO
    } //onUpgrade

    public Cursor list(){
        Log.v(TAG, "list");
        List<Note> result = new ArrayList<Note>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        String[] columns = {"_id", "Title", "Body"};
        try {
            cursor = db.query("Note", columns, null, null, null, null, null);
        } catch (SQLiteException e){
            Log.e(TAG, e.toString());
            cursor = null;
        }
        /*
        while (cursor.moveToNext()){
            result.add(new Note(cursor.getInt(0),cursor.getString(1), cursor.getString(2)));
        }
        */
        //db.close();
        return cursor;
    } //list (all)

    public Cursor search(String searchString){
        Log.v(TAG, "search for " + searchString);
        List<Note> result = new ArrayList<Note>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        String[] columns = {"_id", "Title", "Body"};
        String[] search = {searchString};
        try{
            cursor = db.query("Note", columns, "WHERE Title '%?%' IN(Title, Body)", search, null, null, null);
        } catch (SQLiteException e){
            Log.e(TAG, e.toString());
            cursor = null;
        }
        /*
        while (cursor.moveToNext()){
            result.add(new Note(cursor.getInt(0),cursor.getString(1), cursor.getString(2)));
        }
        */
        db.close();
        return cursor;
    } //search

    public boolean save(Note note){
        Log.v(TAG, "save " + note.title);
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", note.title);
        values.put("Body", note.body);
        try{
            if(note.ID == null){
                if(db.insert("Note", null, values) != -1){
                    success = true;
                }
            } else {
                String[] id = {String.valueOf(note.ID)};
                if(db.update("Note", values, "WHERE _id = ?", id) == 1){
                    success = true;
                }
            }
        } catch (SQLiteException e){
            Log.e(TAG, e.toString());
        }
        db.close();
        return success;
    } //save

    public boolean delete(Note note){
        Log.v(TAG, "delete " + note.title);
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] id = {String.valueOf(note.ID)};
        try{
            if(db.delete("Note", "WHERE _id = ?", id) == 1){
                success = true;
            }
        } catch (SQLiteException e){
            Log.e(TAG, e.toString());
        }
        db.close();
        return success;
    } //delete

} //class
