package net.byloth.engine.graphics.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Matteo on 06/12/2015.
 */
abstract public class GLWallpaperService extends WallpaperService
{
    static final private String TAG = "GLWallpaperService";

    private GLEngine.WallpaperGLSurfaceView glSurfaceView;

    abstract protected GLSurfaceView.Renderer getNewRenderer();

    public GLEngine.WallpaperGLSurfaceView getSurfaceView()
    {
        return glSurfaceView;
    }

    @Override
    public Engine onCreateEngine()
    {
        return new GLEngine();
    }

    public class GLEngine extends Engine
    {
        private boolean renderHasBeenSet;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder)
        {
            super.onCreate(surfaceHolder);

            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);

            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

            if (configurationInfo.reqGlEsVersion >= 0x20000)
            {
                glSurfaceView.setEGLContextClientVersion(2);
                glSurfaceView.setPreserveEGLContextOnPause(true);

                glSurfaceView.setRenderer(getNewRenderer());
                glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

                renderHasBeenSet = true;
            }
            else
            {
                renderHasBeenSet = false;

                Log.e(TAG, "OpenGL ES 2.0 is not supported!");
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible)
        {
            super.onVisibilityChanged(visible);

            if (renderHasBeenSet == true)
            {
                if (visible == true)
                {
                    glSurfaceView.onResume();
                }
                else
                {
                    glSurfaceView.onPause();
                }
            }
        }

        @Override
        public void onDestroy()
        {
            super.onDestroy();

            glSurfaceView.onDestroy();

            renderHasBeenSet = false;
        }

        public class WallpaperGLSurfaceView extends GLSurfaceView
        {
            public WallpaperGLSurfaceView(Context context)
            {
                super(context);
            }

            public void onDestroy()
            {
                super.onDetachedFromWindow();
            }

            @Override
            public SurfaceHolder getHolder()
            {
                return getSurfaceHolder();
            }
        }
    }
}
