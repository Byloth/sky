package net.byloth.environment;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.byloth.engine.graphics.opengl.SquareGLView;
import net.byloth.sky.R;

/**
 * Created by Matteo on 06/10/2016.
 */

public class GLStars extends SquareGLView
{
    private static final int UPDATING_INTERVAL = (1000 / 60);

    public GLStars()
    {
        setUpdatingInterval(UPDATING_INTERVAL);
    }

    @Override
    public GLStars onSurfaceCreated(Context context, GLSurfaceView glSurfaceView)
    {
        super.onSurfaceCreated(context, glSurfaceView);

        loadTexture(context, R.drawable.stars);

        return this;
    }

    @Override
    protected boolean onUpdate()
    {
        return true;
    }
}
