package net.byloth.engine.graphics.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/**
 * Created by Matteo on 05/12/2015.
 */
abstract public class GLWallpaperService extends WallpaperService
{
    public class GLEngine extends Engine
    {
        private boolean renderHasBeenSet;

        private WallpaperGLSurfaceView glSurfaceView;

        protected void setEGLContextClientVersion(int version)
        {
            glSurfaceView.setEGLContextClientVersion(version);
        }

        protected void setPreserveEGLContextOnPause(boolean preserveOnPause)
        {
            glSurfaceView.setPreserveEGLContextOnPause(preserveOnPause);
        }

        protected void setRenderer(GLSurfaceView.Renderer renderer)
        {
            glSurfaceView.setRenderer(renderer);

            renderHasBeenSet = true;
        }

        protected void setRenderMode(int renderMode)
        {
            glSurfaceView.setRenderMode(renderMode);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder)
        {
            super.onCreate(surfaceHolder);

            renderHasBeenSet = false;

            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
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
        }

        public class WallpaperGLSurfaceView extends GLSurfaceView
        {
            public WallpaperGLSurfaceView(Context context)
            {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder()
            {
                return getSurfaceHolder();
            }

            public void onDestroy()
            {
                super.onDetachedFromWindow();
            }
        }
    }
}
