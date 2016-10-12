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
public class TextureGLView extends GLView
{
    static final private int TEXTURE_COORDS_PER_VERTEX = 2;
    static final private int TEXTURE_VERTEX_STRIDE = TEXTURE_COORDS_PER_VERTEX * 4;

    static final private float VERTEX[] = {

            0.5f,  0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };
    static final private float TEXTURE_VERTEX[] = {

            1f, 1f,
            0f, 1f,
            0f, 0f,
            1f, 0f
    };

    static final private short VERTEX_DRAW_ORDER[] = { 0, 1, 2, 0, 2, 3 };

    private int texture;
    private int vertexArrayLocation;

    private FloatBuffer textureVertexBuffer;

    public TextureGLView loadTexture(Context context, @DrawableRes int textureResourceId)
    {
        loadVertex(VERTEX, VERTEX_DRAW_ORDER);
        loadProgram(context, R.raw.default_vertex_shader, R.raw.default_fragment_shader);

        texture = GLCompiler.loadTexture(context, textureResourceId);

        setTextureVertex(TEXTURE_VERTEX);

        return this;
    }

    protected TextureGLView setTextureUniform(String textureUniformName, int textureUniform)
    {
        int textureLocation = getAttributeLocation(textureUniformName);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUniform);

        GLES20.glUniform1i(textureLocation, 0);

        return this;
    }

    public TextureGLView setTextureVertex(float[] textureVertex)
    {
        ByteBuffer textureCoordsByteBuffer = ByteBuffer.allocateDirect(textureVertex.length * 4);
        textureCoordsByteBuffer.order(ByteOrder.nativeOrder());

        textureVertexBuffer = textureCoordsByteBuffer.asFloatBuffer();
        textureVertexBuffer.put(textureVertex);

        return this;
    }

    @Override
    protected TextureGLView beginDraw()
    {
        super.beginDraw();

        vertexArrayLocation = enableVertexArray("position");

        return this;
    }

    @Override
    protected TextureGLView draw()
    {
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        super.draw();

        GLES20.glDisable(GLES20.GL_BLEND);

        return this;
    }

    @Override
    protected TextureGLView endDraw()
    {
        disableVertexArray(vertexArrayLocation);

        super.endDraw();

        return this;
    }

    @Override
    public TextureGLView onSurfaceCreated(GLSurfaceView glSurfaceView)
    {
        throw new UnsupportedOperationException();
    }
    public TextureGLView onSurfaceCreated(Context context, GLSurfaceView glSurfaceView)
    {
        super.onSurfaceCreated(glSurfaceView);

        loadVertex(VERTEX, VERTEX_DRAW_ORDER);
        loadProgram(context, R.raw.default_vertex_shader, R.raw.default_fragment_shader);

        return this;
    }
    
    @Override
    public TextureGLView onDraw(float[] mvpMatrix)
    {
        beginDraw();

        setUniform("color", new Color(255, 0, 255));

        setTextureUniform("texture", texture);

        // TODO: E' necessario settare nuovamente, ogni volta, il textureVertex affiché venga disegnato correttamente...
        setTextureVertex(new float[] {

                0.5f, 1.0f,
                0f, 1.0f,
                0f, 0f,
                0.5f, 0f
        });
        setVertexAttribute("textureCoords", textureVertexBuffer, TEXTURE_COORDS_PER_VERTEX, TEXTURE_VERTEX_STRIDE);

        setUniformMatrix("mvpMatrix", mvpMatrix);

        draw();
        endDraw();

        return this;
    }
}
