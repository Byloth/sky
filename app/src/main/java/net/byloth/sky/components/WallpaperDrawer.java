package net.byloth.sky.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import net.byloth.engine.utils.DayTime;
import net.byloth.environment.Sky;
import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 10/10/2015.
 */
public class WallpaperDrawer extends WallpaperService
{
    static final private String TAG = "WallpaperDrawer";

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

            LiveWallpaper.getInstance().getSunTimesUpdater().setOnSunTimesUpdateListener(new SunTimesUpdater.OnSunTimesUpdateListener()
            {
                @Override
                public void onUpdate(SunTimesUpdater sunTimesUpdaterSender)
                {
                    sky.reinitializeColors(sunTimesUpdaterSender);

                    Log.i(TAG, "Wallpaper's colors have just been updated!");
                }
            });

            Log.i(TAG, "Wallpaper is now live!");
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
                    Log.e(TAG, "Wallpaper was NOT drawn correctly!");
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
                    Log.e(TAG, "Canvas was NOT unlocked correctly!");
                }
            }

            // Log.d(TAG, "Wallpaper was updated & drawn correctly!");

            drawingHandler.removeCallbacks(drawRunner);
            drawingHandler.postDelayed(drawRunner, FRAME_INTERVAL);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            super.onSurfaceChanged(holder, format, width, height);

            sky = new Sky(width, height, dayTime, getApplicationContext());

            Log.i(TAG, "SurfaceHolder has changed: " + width + "x" + height);
        }
        @Override
        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder)
        {
            super.onSurfaceDestroyed(surfaceHolder);

            drawingHandler.removeCallbacks(drawRunner);

            Log.i(TAG, "Wallpaper has been destroyed!");
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

            // Log.d(TAG, "Visibility has changed: " + isVisible);
        }
    }
}
