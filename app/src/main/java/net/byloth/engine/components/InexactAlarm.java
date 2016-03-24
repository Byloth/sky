package net.byloth.engine.components;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Matteo on 23/03/16.
 */
public class InexactAlarm
{
    private boolean isSet;

    private OnAlarmExpiredListener onAlarmExpiredListener;

    public InexactAlarm()
    {
        isSet = false;
    }

    public boolean isSet()
    {
        return isSet;
    }

    public InexactAlarm setAlarm(long repeatingInterval, Context context)
    {
        Intent intent = new Intent(context, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), repeatingInterval, pendingIntent);

        isSet = true;

        return this;
    }

    public InexactAlarm setOnAlarmExpiredListener(OnAlarmExpiredListener onAlarmExpiredListenerInstance)
    {
        onAlarmExpiredListener = onAlarmExpiredListenerInstance;

        return this;
    }

    public interface OnAlarmExpiredListener
    {
        void onExpired(Intent intent);
    }

    public class Receiver extends BroadcastReceiver
    {
        public Receiver() { }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (onAlarmExpiredListener != null)
            {
                onAlarmExpiredListener.onExpired(intent);
            }
        }
    }
}
