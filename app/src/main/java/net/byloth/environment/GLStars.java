package net.byloth.environment;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.byloth.engine.Vector3;
import net.byloth.engine.graphics.opengl.TextureGLView;
import net.byloth.sky.R;

/**
 * Created by Matteo on 06/10/2016.
 */

// TODO: Creare una classe intermedia tra la GLStars e la TextureGLView.
//  Dovrà rappresentare una raccolta di oggetti con la medesima Texture.

public class GLStars extends TextureGLView
{
    static final private float[][] TEXTURE_VERTEXES = {

        {
            0.5f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            0.5f, 0.0f
        },
        {

            1.0f, 1.0f,
            0.5f, 1.0f,
            0.5f, 0.0f,
            1.0f, 0.0f
        }
    };

    private float rotation = 0;

    public GLStars() { }

    @Override
    public GLStars onSurfaceCreated(Context context, GLSurfaceView glSurfaceView)
    {
        super.onSurfaceCreated(context, glSurfaceView);

        loadTexture(context, R.drawable.stars);
        setTextureVertex(TEXTURE_VERTEXES[0]);

        return this;
    }

    @Override
    protected boolean onUpdate()
    {
        // TODO: Questa logica di aggiornamento fa CAGARE!!!
        //
        // Bisogna trovare una nuova logica...
        //
        // Forse, passando un'istanza di un oggetto con tutte
        //  le proprietà aggiornabili come parametro dell' onUpdate() ?
        //
            rotation += 0.1;

            setRotation(new Vector3(rotation, 0, 0));

            return true;
    }
}
