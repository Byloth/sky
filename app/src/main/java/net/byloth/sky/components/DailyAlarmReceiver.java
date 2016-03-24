package net.byloth.sky.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 24/03/16.
 */
public class DailyAlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SunTimesUpdater.updateSunTimes();

        Toast.makeText(context, "\t\tLe ore di alba e tramonto\nodierne sono state aggiornate!", Toast.LENGTH_LONG).show();
    }
}
