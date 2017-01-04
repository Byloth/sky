package net.byloth.environment;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.byloth.engine.graphics.opengl.TextureGLView;
import net.byloth.sky.R;

/**
 * Created by Matteo on 06/10/2016.
 */

// TODO: Creare una classe intermedia tra la GLStars e la TextureGLView.
//  Dovrà rappresentare una raccolta di oggetti con la medesima Texture.

public class GLStars extends TextureGLView
{
    private static final int UPDATING_INTERVAL = (1000 / 60);

    // Immagine 1
    static final private float TEXTURE_VERTEX[] = {

            0f, 0f,
            0f, 1.0f,
            0.5f, 1.0f,
            0.5f, 0f
    };
    // Immagine 2
    /*
    static final private float TEXTURE_VERTEX[] = {

            0.5f, 0f,
            0.5f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0f
    };
    */

    public GLStars()
    {
        setUpdatingInterval(UPDATING_INTERVAL);
    }

    @Override
    public GLStars onSurfaceCreated(Context context, GLSurfaceView glSurfaceView)
    {
        super.onSurfaceCreated(context, glSurfaceView);

        loadTexture(context, R.drawable.stars);
        setTextureVertex(TEXTURE_VERTEX);

        return this;
    }

    @Override
    protected boolean onUpdate()
    {
        return true;
    }
}
