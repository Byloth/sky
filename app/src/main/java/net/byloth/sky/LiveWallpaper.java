package net.byloth.sky;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import net.byloth.engine.DayTime;
import net.byloth.environment.Sky;
import net.byloth.sky.updaters.LocationUpdater;
import net.byloth.sky.updaters.SunUpdater;

/**
 * Created by Matteo on 10/10/2015.
 */
public class LiveWallpaper extends WallpaperService
{
    static private SharedPreferences sharedPreferences;

    static final public String APPLICATION_NAME = "SkyLiveWallpaper";

    private SunUpdater sunUpdater;
    private LocationUpdater locationUpdater;

    static public SharedPreferences getSharedPreferences()
    {
        return sharedPreferences;
    }

    @Override
    public void onCreate()
    {
        sunUpdater = new SunUpdater();
        locationUpdater = new LocationUpdater((LocationManager) getSystemService(Context.LOCATION_SERVICE), this);

        locationUpdater.setOnLocationUpdate(new LocationUpdater.OnLocationUpdate()
        {
            @Override
            public void onUpdate(double latitude, double longitude)
            {
                if (sunUpdater.isAlarmSet() == false)
                {
                    sunUpdater.setAlarm(AlarmManager.INTERVAL_DAY, getApplicationContext());
                }

            }
        });

        sharedPreferences = getApplicationContext().getSharedPreferences(APPLICATION_NAME, Context.MODE_PRIVATE);
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

            Log.i(APPLICATION_NAME, "Wallpaper is now live!");
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
                    Log.e(APPLICATION_NAME, "Wallpaper was NOT drawn correctly!");
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
                    Log.e(APPLICATION_NAME, "Canvas was NOT unlocked correctly!");
                }
            }

         // Log.i(APPLICATION_NAME, "Wallpaper was updated & drawn correctly!");

            drawingHandler.removeCallbacks(drawRunner);
            drawingHandler.postDelayed(drawRunner, FRAME_INTERVAL);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            super.onSurfaceChanged(holder, format, width, height);

            sky = new Sky(width, height, dayTime, getApplicationContext());

            Log.i(APPLICATION_NAME, "SurfaceHolder has changed: " + width + "x" + height);
        }
        @Override
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder)
        {
            super.onSurfaceDestroyed(surfaceHolder);

            drawingHandler.removeCallbacks(drawRunner);

            Log.i(APPLICATION_NAME, "Wallpaper has been destroyed!");
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

            Log.i(APPLICATION_NAME, "Visibility has changed: " + isVisible);
        }
    }
}
