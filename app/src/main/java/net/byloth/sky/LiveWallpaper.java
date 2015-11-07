package net.byloth.sky;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import net.byloth.engine.DayTime;
import net.byloth.environment.Sky;

/**
 * Created by Matteo on 10/10/2015.
 */
public class LiveWallpaper extends WallpaperService
{
    private double latitude;
    private double longitude;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public LiveWallpaper()
    {
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Log.i("SkyLiveWallpaper", "User location has been updated: " + latitude + ", " + longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        };
    }

    @Override
    public Engine onCreateEngine()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            {
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
            }
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
        }

        return new RenderingEngine();
    }

    private class RenderingEngine extends Engine
    {
        private Handler drawingHandler;
        private Runnable drawRunner;

        private DayTime dayTime;
        private Updater updater;

        private Sky sky;

        static final public int FRAME_INTERVAL = 10000;

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

            updater = new Updater();
            updater.setAlarm(AlarmManager.INTERVAL_DAY, getApplicationContext());

            drawingHandler.post(drawRunner);

            Log.i("SkyLiveWallpaper", "Wallpaper is now live!");
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
                    Log.e("SkyLiveWallpaper", "Wallpaper was NOT drawn correctly!");
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
                    Log.e("SkyLiveWallpaper", "Canvas was NOT unlocked correctly!");
                }
            }

         // Log.i("SkyLiveWallpaper", "Wallpaper was updated & drawn correctly!");

            drawingHandler.removeCallbacks(drawRunner);
            drawingHandler.postDelayed(drawRunner, FRAME_INTERVAL);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            super.onSurfaceChanged(holder, format, width, height);

            sky = new Sky(width, height, dayTime, getApplicationContext());

            Log.i("SkyLiveWallpaper", "SurfaceHolder has changed: " + width + "x" + height);
        }
        @Override
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder)
        {
            super.onSurfaceDestroyed(surfaceHolder);

            drawingHandler.removeCallbacks(drawRunner);

            Log.i("SkyLiveWallpaper", "Wallpaper has been destroyed!");
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

            Log.i("SkyLiveWallpaper", "Visibility has changed: " + isVisible);
        }
    }
}
