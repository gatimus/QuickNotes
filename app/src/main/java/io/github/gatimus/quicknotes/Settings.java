package io.github.gatimus.quicknotes;

import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class Settings extends PreferenceActivity {

    private static final String TAG = "Settings";
    private FragmentManager fragMan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Create");
        super.onCreate(savedInstanceState);
        fragMan = this.getFragmentManager();
        fragMan.beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    } //onCreate

    public static class SettingsFragment extends PreferenceFragment {

        private static final String TAG = "SettingsFragment";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            Log.v(TAG, "Create");
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        } //onCreate

    } //class SettingsFragment

} //class
