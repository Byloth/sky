package net.byloth.engine.graphics.opengl;

import android.content.Context;
import android.opengl.GLES20;
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
    static final private int TEXTURE_COORDS_PER_VERTEX = 2;
    static final private int TEXTURE_VERTEX_STRIDE = TEXTURE_COORDS_PER_VERTEX * 4;

    static final private float VERTEX[] = {

            0.5f,  0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    static final private short VERTEX_DRAW_ORDER[] = { 0, 1, 2, 0, 2, 3 };

    // Immagine 1
    /*
        static final private float TEXTURE_VERTEX[] = {

                0.5f, 1.0f,
                0f, 1.0f,
                0f, 0f,
                0.5f, 0f
        };
    */
    // Immagine 2
    static final private float TEXTURE_VERTEX[] = {

            0.5f, 1.0f,
            0f, 1.0f,
            0f, 0f,
            0.5f, 0f
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
        int program = getProgram();
        int vertexArrayLocation = enableVertexArray("position");

        setUniform("color", new Color(255, 0, 255));

        int textureLocation = GLES20.glGetAttribLocation(program, "texture");
        int textureCoordsLocation = GLES20.glGetAttribLocation(program, "textureCoords");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        GLES20.glUniform1i(textureLocation, 0);

        textureVertexBuffer.position(0);

        GLES20.glVertexAttribPointer(textureCoordsLocation, TEXTURE_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEXTURE_VERTEX_STRIDE, textureVertexBuffer);
        GLES20.glEnableVertexAttribArray(textureCoordsLocation);

        int mvpMatrixLocation = GLES20.glGetUniformLocation(program, "mvpMatrix");

        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        drawElements();

        GLES20.glDisable(GLES20.GL_BLEND);

        disableVertexArray(vertexArrayLocation);

        return this;
    }
}
