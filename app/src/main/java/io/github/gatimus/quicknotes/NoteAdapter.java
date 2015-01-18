package io.github.gatimus.quicknotes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class NoteAdapter extends CursorAdapter{

    private static final String TAG = "NoteAdapter";

    public NoteAdapter(Context context, Cursor cursor){
        super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        Log.v(TAG, "construct");

    } //constructor

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v(TAG, "newView");
        return null;
    } //newView

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v(TAG, "bindView");
    } //bindView

} //NoteAdapter
