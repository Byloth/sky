package net.byloth.sky;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import net.byloth.engine.graphics.opengl.GLES2WallpaperService;
import net.byloth.environment.GLSky;

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
        final private float[] mvpMatrix = new float[16];
        final private float[] projectionMatrix = new float[16];
        final private float[] viewMatrix = new float[16];

        private GLSky sky;

        public Renderer()
        {
            sky = new GLSky(GLiveWallpaper.this);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            GLES20.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

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
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

            sky.draw(mvpMatrix);

            Log.d(LiveWallpaper.APPLICATION_NAME, "Frame drawed!");
        }
    }
}
