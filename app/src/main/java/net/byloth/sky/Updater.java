package net.byloth.sky;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Matteo on 23/10/2015.
 */
public class Updater extends BroadcastReceiver
{
 // static final private String TAG = Updater.class.toString();

    public Updater setAlarm(long repeatingInterval, Context context)
    {
        Intent intent = new Intent(context, Updater.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), repeatingInterval, pendingIntent);

        Log.i("SkyLiveWallpaper", "Updater's alarm has been set correctly!");

        return this;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
     // PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
     // PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

     // wakeLock.acquire();
     // TODO: Something!
     // wakeLock.release();

        Log.i("SkyLiveWallpaper", "Updater's onReceive method has been fired!");
        Toast.makeText(context, "L'updater è stato lanciato correttamente!", Toast.LENGTH_LONG).show();
    }
}
