package net.byloth.sky;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.byloth.engine.helpers.Maths;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Matteo on 23/03/2016.
 */
public class DailyUpdater
{
    private boolean isAlarmSet;

    private Receiver receiver;

    public DailyUpdater()
    {
        isAlarmSet = false;
    }

    public DailyUpdater forceUpdate()
    {
        /* TODO: rendere il metodo statico? */
        /* TODO: è possibile inviare un intent ad un broadcast receiver tramite un'istruzione? */

        return this;
    }

    public DailyUpdater setAlarm(long repeatingInterval, Context context)
    {
        /* TODO: rendere il metodo privato ed "automatico" al momento dell'inizializzazione della classe? */

        Intent intent = new Intent(context, Receiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), repeatingInterval, pendingIntent);

        isAlarmSet = true;

        Log.i(LiveWallpaper.APPLICATION_NAME, "SunTimesUpdater's alarm has been set correctly!");

        return this;
    }

    public boolean isAlarmSet()
    {
        return isAlarmSet;
    }

    private class Receiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }
}
