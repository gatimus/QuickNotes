package io.github.gatimus.quicknotes;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class Main extends ActionBarActivity {

    private static final String TAG = "Main";
    private Resources resources;
    private FragmentManager fragmentManager;
    private DialogFragment about;
    private DialogFragment help;
    private DialogFragment save;
    private DialogFragment delete;
    private DialogFragment newDialog;
    private EditText etTitle;
    private EditText etBody;
    private ActionBar actionBar;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SearchManager searchManager;
    private Note currentNote;
    private DAO dao;
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Create");
        setContentView(R.layout.activity_main);

        resources = getApplicationContext().getResources();
        fragmentManager = this.getFragmentManager();
        about = new About();
        help = new Help();
        save = new Save();
        delete = new Delete();
        newDialog = new NewDialog();

        etTitle = (EditText) findViewById(R.id.et_title);
        etBody = (EditText) findViewById(R.id.et_body);
        actionBar = getSupportActionBar();
        listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor onClickCursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
                onClickCursor.moveToPosition(position);
                openNote(onClickCursor);
                searchManager.stopSearch();
                drawerLayout.closeDrawers();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                actionBar.setTitle("notes");
            }
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                actionBar.setTitle(R.string.app_name);
                update();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchManager.setOnDismissListener(new SearchManager.OnDismissListener(){
            @Override
            public void onDismiss() {
                Log.v("SearchManager", "Dismiss");
                drawerLayout.closeDrawers();
            }
        });

        currentNote = new Note();
        dao = new DAO(getApplicationContext(), resources);
        cursor = dao.list();
        cursorAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {"Title", "Body"},
                new int[] {android.R.id.text1, android.R.id.text2},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        listView.setAdapter(cursorAdapter);

        handleIntent(getIntent());
    } //onCreate

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    } //onPostCreate

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    } //onNewIntent

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    } //onConfigurationChanged

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "CreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if(searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        } //if search is action view
        return true;
    } //onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } //if navigation drawer is the option selected
        Log.i(TAG, item.getTitle().toString() + " Selected");
        int id = item.getItemId();
        switch(id){
            case R.id.action_search :
                if(item.getActionView() == null){
                    onSearchRequested();
                } //if search is not action view
                break;
            case R.id.action_save :
                save.show(fragmentManager, resources.getString(R.string.action_save));
                break;
            case R.id.action_delete :
                delete.show(fragmentManager, resources.getString(R.string.action_delete));
                break;
            case R.id.action_new :
                newDialog.show(fragmentManager, resources.getString(R.string.action_new));
                break;
            case R.id.action_about :
                about.show(fragmentManager, resources.getString(R.string.action_about));
                break;
            case R.id.action_help :
                help.show(fragmentManager, resources.getString(R.string.action_help));
                break;
            case R.id.action_quit : System.exit(0);
                break;
            default :
                break;
        } //switch case
        return super.onOptionsItemSelected(item);
    } //onOptionsItemSelected

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            cursorAdapter.swapCursor(dao.search(query));
            cursorAdapter.notifyDataSetChanged();
            drawerLayout.openDrawer(Gravity.LEFT);
        } //if
    } //handleIntent

    public void openNote(Cursor cursor){
        currentNote = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        etTitle.setText(currentNote.title);
        etBody.setText(currentNote.body);
    }

    public void saveNote(){
        currentNote.title = etTitle.getText().toString();
        currentNote.body = etBody.getText().toString();
        if(dao.save(currentNote)){
            Toast.makeText(getApplicationContext(), "Saved " + currentNote.title, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to save " + currentNote.title, Toast.LENGTH_SHORT).show();
        }
        update();
    }

    public void deleteNote(){
        if(dao.delete(currentNote)){
            Toast.makeText(getApplicationContext(), "Deleted " + currentNote.title, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to delete " + currentNote.title, Toast.LENGTH_SHORT).show();
        }
        newNote();
        update();
    }

    public void newNote(){
        currentNote = new Note();
        etTitle.setText("");
        etBody.setText("");
    }

    public void update(){
        cursor = dao.list();
        cursorAdapter.changeCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
    }

} //class
