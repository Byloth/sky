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
        String intentAction = intent.getAction();

        SunTimesUpdater.updateSunTimes();

        if (intentAction.equals("net.byloth.sky.components.ALERT_EXPIRED") == true)
        {
            Toast.makeText(context, "\t\tLe ore di alba e tramonto\nodierne sono state aggiornate!", Toast.LENGTH_LONG).show();
        }
        else if (intentAction.equals("net.byloth.sky.activities.FORCE_UPDATE") == true)
        {
            Toast.makeText(context, "L'aggiornamento delle ore di alba e tramonto\n\t\todierne è stato eseguito correttamente!", Toast.LENGTH_LONG).show();
        }
    }
}
