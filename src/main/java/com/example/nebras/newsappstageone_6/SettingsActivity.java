package com.example.nebras.newsappstageone_6;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// I got help from the earthquake app project-Udacity nanodegree to achieve this project
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference section = findPreference(getString(R.string.section_key));
            bindPreferenceToValue(section);
            Preference specificWord = findPreference(getString(R.string.specific_word_key));
            bindPreferenceToValue(specificWord);

        }

        public void bindPreferenceToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            onPreferenceChange(preference, sharedPreferences.getString(preference.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                CharSequence[] preferenceLabels = listPreference.getEntries();
                preference.setSummary(preferenceLabels[listPreference.findIndexOfValue(newValue.toString())]);
            } else {
                preference.setSummary(newValue.toString());
            }
            return true;
        }
    }
}
