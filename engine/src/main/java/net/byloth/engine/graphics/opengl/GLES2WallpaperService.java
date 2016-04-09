package net.byloth.engine.graphics.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Matteo on 06/12/2015.
 */
abstract public class GLES2WallpaperService extends GLWallpaperService
{
    static final private String TAG = "GLES2WallpaperService";

    abstract protected GLSurfaceView.Renderer getNewRenderer();

    @Override
    public Engine onCreateEngine()
    {
        return new GLES2Engine();
    }

    public class GLES2Engine extends GLEngine
    {
        @Override
        public void onCreate(SurfaceHolder surfaceHolder)
        {
            super.onCreate(surfaceHolder);

            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

            boolean supportsGLES2 = (configurationInfo.reqGlEsVersion >= 0x20000);

            if (supportsGLES2 == true)
            {
                setEGLContextClientVersion(2);
                setPreserveEGLContextOnPause(true);
                setRenderer(getNewRenderer());

                setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            }
            else
            {
                Log.e(TAG, "OpenGL ES 2.0 is not supported!");

                // TODO: Create here an OpenGL ES 1.X compatible renderer version.
            }
        }
    }
}
