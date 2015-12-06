package net.byloth.sky;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import net.byloth.engine.opengl.GLES2WallpaperService;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Matteo on 06/12/2015.
 */
public class GLiveWallpaper extends GLES2WallpaperService
{
    @Override
    protected GLSurfaceView.Renderer getNewRenderer()
    {
        return new Renderer();
    }

    public class Renderer implements GLSurfaceView.Renderer
    {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            GLES20.glClearColor(1, 0, 1, 1);

            Log.d(LiveWallpaper.APPLICATION_NAME, "Surface created!");
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            GLES20.glViewport(0, 0, width, height);

            Log.d(LiveWallpaper.APPLICATION_NAME, "Surface changed!");
        }

        @Override
        public void onDrawFrame(GL10 gl)
        {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            Log.d(LiveWallpaper.APPLICATION_NAME, "Frame drawed!");
        }
    }
}
