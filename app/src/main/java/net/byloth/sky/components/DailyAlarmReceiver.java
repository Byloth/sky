package net.byloth.sky.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.updaters.LocationUpdater;
import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 24/03/16.
 */
public class DailyAlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String intentAction = intent.getAction();

        if (intentAction != null)
        {
            LocationUpdater locationUpdater = LiveWallpaper.getInstance().getLocationUpdater();
            SunTimesUpdater sunTimesUpdater = LiveWallpaper.getInstance().getSunTimesUpdater();

            boolean sunTimesUpdated = sunTimesUpdater.updateSunTimes(locationUpdater.getCurrentLocation());

            if (sunTimesUpdated == true)
            {
                if (intentAction.equals("net.byloth.sky.activities.FORCE_UPDATE") == true)
                {
                    Toast.makeText(context, "Aggiornamento completato", Toast.LENGTH_LONG)
                         .show();
                }
            }
            else
            {
                if (intentAction.equals("net.byloth.sky.activities.FORCE_UPDATE") == true)
                {
                    Toast.makeText(context, "Aggiornamento fallito", Toast.LENGTH_LONG)
                         .show();
                }
            }
        }
    }
}
