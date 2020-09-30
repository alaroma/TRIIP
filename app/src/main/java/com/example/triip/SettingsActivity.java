package com.example.triip;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_headers);
        Preference button = findPreference(getString(R.string.dialogs));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Dialog dialog = Dialogs.getDialog(SettingsActivity.this, Dialogs.IDD_ABOUT);
                dialog.show();
                return true;
            }
        });

        setupActionBar();

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
