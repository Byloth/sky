package net.byloth.sky.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.byloth.sky.R;

public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.live_wallpaper_settings);
    }
}