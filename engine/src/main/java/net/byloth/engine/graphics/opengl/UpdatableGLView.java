package net.byloth.engine.graphics.opengl;

import android.opengl.GLSurfaceView;
import android.os.Handler;

/**
 * Created by Matteo on 28/04/2016.
 */
abstract public class UpdatableGLView extends GLView
{
    private GLSurfaceView glSurfaceView;

    private long frameInterval;

    private Handler updatingHandler;
    private Runnable updateRunner;

    public UpdatableGLView()
    {
        frameInterval = 0;

        updatingHandler = new Handler();
        updateRunner = new Runnable()
        {
            @Override
            public void run()
            {
                onUpdate();

                glSurfaceView.requestRender();
                updatingHandler.removeCallbacks(updateRunner);

                if (frameInterval > 0)
                {
                    updatingHandler.postDelayed(updateRunner, frameInterval);
                }
            }
        };
    }

    public UpdatableGLView onSurfaceCreated(GLWallpaperService glWallpaperService)
    {
        glSurfaceView = glWallpaperService.getSurfaceView();

        return this;
    }

    public UpdatableGLView onPause()
    {
        updatingHandler.removeCallbacks(updateRunner);

        return this;
    }

    public UpdatableGLView onResume()
    {
        updatingHandler.post(updateRunner);

        return this;
    }

    abstract public UpdatableGLView onUpdate();

    public UpdatableGLView onVisibilityChanged(boolean visible)
    {
        if (visible == true)
        {
            onResume();
        }
        else
        {
            onPause();
        }

        return this;
    }

    public UpdatableGLView setFrameInterval(double frameIntervalValue)
    {
        if (frameInterval < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            frameInterval = (int) frameIntervalValue;
        }

        return this;
    }
    public UpdatableGLView setFramePerSecond(double framePerSecondValue)
    {
        if (framePerSecondValue < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            if (framePerSecondValue == 0)
            {
                frameInterval = 0;
            }
            else
            {
                frameInterval = (int) (1000.0d / framePerSecondValue);
            }
        }

        return this;
    }
}
