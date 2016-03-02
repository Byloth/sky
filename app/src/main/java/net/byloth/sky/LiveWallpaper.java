package net.byloth.sky;

import android.app.Application;
import android.content.Intent;

import net.byloth.sky.updaters.LocationUpdater;
import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 02/03/16.
 */
public class LiveWallpaper extends Application
{
    static final public String APPLICATION_NAME = "SkyLiveWallpaper";

    private LocationUpdater locationUpdater;
    private SunTimesUpdater sunTimesUpdater;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Intent locationUpdaterIntent = new Intent(this, LocationUpdater.class);

        startService(locationUpdaterIntent);
    }
}