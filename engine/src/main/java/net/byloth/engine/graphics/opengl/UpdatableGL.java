package net.byloth.engine.graphics.opengl;

import android.os.Handler;

/**
 * Created by Matteo on 28/04/2016.
 */
abstract public class UpdatableGL
{
    private long frameInterval;

    private Handler updatingHandler;
    private Runnable updateRunner;

    public UpdatableGL()
    {
        this(60);
    }

    public UpdatableGL(int framePerSecond)
    {
        updatingHandler = new Handler();
        updateRunner = new Runnable()
        {
            @Override
            public void run()
            {
                onUpdate();

                updatingHandler.removeCallbacks(updateRunner);

                if (frameInterval > 0)
                {
                    updatingHandler.postDelayed(updateRunner, frameInterval);
                }
            }
        };

        setFramePerSecond(framePerSecond);
    }

    public void onPause()
    {
        updatingHandler.removeCallbacks(updateRunner);
    }

    public void onResume()
    {
        updatingHandler.post(updateRunner);
    }

    abstract public void onUpdate();

    public void onVisibilityChanged(boolean visible)
    {
        if (visible == true)
        {
            onResume();
        }
        else
        {
            onPause();
        }
    }

    public void setFramePerSecond(double framePerSecond)
    {
        if (framePerSecond < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            if (framePerSecond == 0)
            {
                frameInterval = 0;
            }
            else
            {
                frameInterval = (int) (1000.0d / framePerSecond);
            }
        }
    }
}
