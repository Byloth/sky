package net.byloth.sky;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import net.byloth.engine.DayTime;
import net.byloth.environment.Sky;
import net.byloth.sky.updaters.LocationUpdater;
import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 10/10/2015.
 */
public class WallpaperDrawer extends WallpaperService
{
    private boolean locationUpdaterBound;
    private ServiceConnection locationUpdaterConnection;

    private LocationUpdater locationUpdater;
    private SunTimesUpdater sunTimesUpdater;

    public WallpaperDrawer()
    {
        locationUpdaterBound = false;

        locationUpdaterConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                locationUpdater = ((LocationUpdater.ServiceBinder) service).getLocationUpdater();
                locationUpdaterBound = true;

                Log.i(LiveWallpaper.APPLICATION_NAME, "Bound!");
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                locationUpdater = null;
                locationUpdaterBound = false;

                Log.i(LiveWallpaper.APPLICATION_NAME, "Unbound!");
            }
        };
    }

    @Override
    public void onCreate()
    {
        Intent bindingIntent = new Intent(this, LocationUpdater.class);

        bindService(bindingIntent, locationUpdaterConnection, Context.BIND_AUTO_CREATE);
        /*
            locationUpdater = new LocationUpdater((LocationManager) getSystemService(Context.LOCATION_SERVICE), this);
            locationUpdater.setOnLocationUpdate(new LocationUpdater.OnLocationUpdate()
            {
                /* TODO: Valutare se optare per un callback che viene chiamato una sola volta, all'avvio (es. onFirstUpdate). */
                /*
                @Override
                public void onUpdate(double locationLatitude, double locationLongitude)
                {
                    if (sunTimesUpdater.isAlarmSet() == false)
                    {
                        sunTimesUpdater.setAlarm(AlarmManager.INTERVAL_DAY, getApplicationContext());
                    }
                }
            });
        */

        sunTimesUpdater = new SunTimesUpdater();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (locationUpdaterBound == true)
        {
            unbindService(locationUpdaterConnection);
        }
    }

    @Override
    public Engine onCreateEngine()
    {
        return new RenderingEngine();
    }

    private class RenderingEngine extends Engine
    {
        private Handler drawingHandler;
        private Runnable drawRunner;

        private DayTime dayTime;

        private Sky sky;

        static final public int FRAME_INTERVAL = 1000;

        public RenderingEngine()
        {
            drawingHandler = new Handler();
            drawRunner = new Runnable()
            {
                @Override
                public void run()
                {
                    updateWallpaper();
                }
            };

            dayTime = new DayTime();

            drawingHandler.post(drawRunner);

            sunTimesUpdater.setOnSunTimesUpdate(new SunTimesUpdater.OnSunTimesUpdate()
            {
                @Override
                public void onUpdate(Bundle sunTimesUpdatedValues)
                {
                    sky.reinitializeColors();

                    Log.i(LiveWallpaper.APPLICATION_NAME, "Wallpaper's colors have just been updated!");
                }
            });

            Log.i(LiveWallpaper.APPLICATION_NAME, "Wallpaper is now live!");
        }

        private void draw(Canvas canvas)
        {
            canvas.drawColor(Color.MAGENTA);

            sky.draw(canvas);
        }

        private void updateWallpaper()
        {
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas canvas = null;

            try
            {
                canvas = surfaceHolder.lockCanvas();

                if (canvas != null)
                {
                    draw(canvas);
                }
                else
                {
                    Log.e(LiveWallpaper.APPLICATION_NAME, "Wallpaper was NOT drawn correctly!");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                else
                {
                    Log.e(LiveWallpaper.APPLICATION_NAME, "Canvas was NOT unlocked correctly!");
                }
            }

         // Log.i(LiveWallpaper.APPLICATION_NAME, "Wallpaper was updated & drawn correctly!");

            drawingHandler.removeCallbacks(drawRunner);
            drawingHandler.postDelayed(drawRunner, FRAME_INTERVAL);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            super.onSurfaceChanged(holder, format, width, height);

            sky = new Sky(width, height, dayTime, getApplicationContext());

            Log.i(LiveWallpaper.APPLICATION_NAME, "SurfaceHolder has changed: " + width + "x" + height);
        }
        @Override
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder)
        {
            super.onSurfaceDestroyed(surfaceHolder);

            drawingHandler.removeCallbacks(drawRunner);

            Log.i(LiveWallpaper.APPLICATION_NAME, "Wallpaper has been destroyed!");
        }

        @Override
        public void onTouchEvent(MotionEvent motionEvent)
        {
            super.onTouchEvent(motionEvent);
        }

        @Override
        public void onVisibilityChanged(boolean isVisible)
        {
            super.onVisibilityChanged(isVisible);

            if (isVisible == true)
            {
                drawingHandler.post(drawRunner);
            }
            else
            {
                drawingHandler.removeCallbacks(drawRunner);
            }

         // Log.i(LiveWallpaper.APPLICATION_NAME, "Visibility has changed: " + isVisible);
        }
    }
}
