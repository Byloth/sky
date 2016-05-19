package net.byloth.engine.graphics.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.byloth.engine.R;
import net.byloth.engine.graphics.Color;

/**
 * Created by Matteo on 19/05/2016.
 */
public class SquareGLView extends GLView
{
    static final private float VERTEX[] = {

            0.5f,  0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    static final private short VERTEX_DRAW_ORDER[] = { 0, 1, 2, 0, 2, 3 };
    
    @Override
    public SquareGLView onSurfaceCreated(GLSurfaceView glSurfaceView)
    {
        throw new UnsupportedOperationException();
    }
    public SquareGLView onSurfaceCreated(Context context, GLSurfaceView glSurfaceView)
    {
        super.onSurfaceCreated(glSurfaceView);

        loadVertex(VERTEX, VERTEX_DRAW_ORDER);
        loadProgram(context, R.raw.default_vertex_shader, R.raw.default_fragment_shader);

        return this;
    }
    
    @Override
    protected SquareGLView onDraw()
    {
        int vertexArrayLocation = enableVertexArray("position");

        setUniform("color", new Color(255, 0, 255));

        drawElements();

        disableVertexArray(vertexArrayLocation);

        return this;
    }
}
