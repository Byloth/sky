package net.byloth.engine.graphics.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.DrawableRes;

import net.byloth.engine.R;
import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.opengl.helpers.GLCompiler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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

    static final private float TEXTURE_VERTEX[] = {

            0.5f, 0.5f,
            -0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f
    };

    private int texture;

    private FloatBuffer textureVertexBuffer;

    public SquareGLView loadTexture(Context context, @DrawableRes int textureResourceId)
    {
        super.loadVertex(VERTEX, VERTEX_DRAW_ORDER);

        ByteBuffer textureCoordsByteBuffer = ByteBuffer.allocateDirect(TEXTURE_VERTEX.length * 4);
        textureCoordsByteBuffer.order(ByteOrder.nativeOrder());

        textureVertexBuffer = textureCoordsByteBuffer.asFloatBuffer();
        textureVertexBuffer.put(TEXTURE_VERTEX);
        textureVertexBuffer.position(0);

        loadProgram(context, R.raw.default_vertex_shader, R.raw.default_fragment_shader);

        texture = GLCompiler.loadTexture(context, textureResourceId);

        return this;
    }
    
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
    protected SquareGLView onDraw(float[] mvpMatrix)
    {
        int vertexArrayLocation = enableVertexArray("position");

        // TODO: Terminare l'implementazione di questo mentodo.
        // INFO: http://stackoverflow.com/questions/12793341/draw-a-2d-image-using-opengl-es-2-0

        setUniform("color", new Color(255, 0, 255));

        drawElements();

        disableVertexArray(vertexArrayLocation);

        return this;
    }
}
