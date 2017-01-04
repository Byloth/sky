package net.byloth.sky.components;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import net.byloth.engine.graphics.opengl.GLWallpaperService;
import net.byloth.engine.graphics.opengl.TextureGLView;
import net.byloth.environment.GLSky;
import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.R;
import net.byloth.sky.updaters.SunTimesUpdater;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Matteo on 06/12/2015.
 */
public class GLWallpaperDrawer extends GLWallpaperService
{
    static final private String TAG = "GLWallpaperDrawer";

    private GLSky sky;
    private TextureGLView image;

    public GLWallpaperDrawer()
    {
        SunTimesUpdater sunTimesUpdater = LiveWallpaper.getInstance().getSunTimesUpdater();

        sky = new GLSky(sunTimesUpdater);
        image = new TextureGLView()
        {
            @Override
            protected boolean onUpdate()
            {
                return true;
            }
        };

        image.setUpdatingInterval(17);

        sunTimesUpdater.setOnSunTimesUpdateListener(new SunTimesUpdater.OnSunTimesUpdateListener()
        {
            @Override
            public void onUpdate(SunTimesUpdater sunTimesUpdaterSender)
            {
                sky.reinitializeColors(sunTimesUpdaterSender);

                Log.i(TAG, "Wallpaper's colors have just been updated!");
            }
        });
    }

    @Override
    protected GLSurfaceView.Renderer getNewRenderer()
    {
        return new GLWallpaperRenderer();
    }

    @Override
    public Engine onCreateEngine()
    {
        return new GLWallpaperUpdateEngine();
    }

    public class GLWallpaperUpdateEngine extends GLEngine
    {
        @Override
        public void onVisibilityChanged(boolean visible)
        {
            super.onVisibilityChanged(visible);

            sky.onVisibilityChanged(visible);
            image.onVisibilityChanged(visible);
        }
    }

    public class GLWallpaperRenderer implements GLSurfaceView.Renderer
    {
        private float[] projectionMatrix = new float[16];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            GLES20.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

            sky.onSurfaceCreated(GLWallpaperDrawer.this);

            image.onSurfaceCreated(GLWallpaperDrawer.this, getGlSurfaceView());
            image.loadTexture(GLWallpaperDrawer.this, R.drawable.stars);

            Log.d(TAG, "Surface created!");
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            float ratio = ((float) width) / height;

            GLES20.glViewport(0, 0, width, height);

            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

            sky.onSurfaceChanged(width, height);

            Log.d(TAG, "Surface changed!");
        }

        @Override
        public void onDrawFrame(GL10 gl)
        {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            sky.onDraw();
            image.onDraw(projectionMatrix);

            Log.d(TAG, "Frame drawed!");
        }
    }
}
