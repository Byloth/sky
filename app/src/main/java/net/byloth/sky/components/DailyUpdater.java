package net.byloth.sky.components;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.byloth.engine.components.InexactAlarm;
import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 23/03/2016.
 */
public class DailyUpdater extends InexactAlarm implements InexactAlarm.OnAlarmExpiredListener
{
    public DailyUpdater()
    {
        setOnAlarmExpiredListener(this);
    }

    public InexactAlarm setAlarm(Context context)
    {
        super.setAlarm(AlarmManager.INTERVAL_DAY, context);

        Log.i(LiveWallpaper.APPLICATION_NAME, "DailyUpdater's alarm has been set correctly!");

        return this;
    }

    @Override
    public void onExpired(Intent intent)
    {
        SunTimesUpdater.updateSunTimes();
    }
}
