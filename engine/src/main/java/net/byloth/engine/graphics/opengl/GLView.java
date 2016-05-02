package net.byloth.engine.graphics.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.RawRes;

import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.Vector2;
import net.byloth.engine.graphics.opengl.helpers.GLCompiler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Matteo on 30/04/2016.
 */
abstract public class GLView
{
    static final private int COORDS_PER_VERTEX = 3;
    static final private int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private int program;

    private int vertexCount;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    public GLView loadProgram(String vertexShaderSource, String fragmentShaderSource)
    {
        program = GLCompiler.linkProgram(vertexShaderSource, fragmentShaderSource);

        return this;
    }
    public GLView loadProgram(Context context, @RawRes int vertexShaderResourceId, @RawRes int fragmentShaderResourceId)
    {
        program = GLCompiler.linkProgram(context, vertexShaderResourceId, fragmentShaderResourceId);

        return this;
    }

    public GLView loadVertex(float[] vertex, short[] vertexDrawOrder)
    {
        vertexCount = vertex.length / COORDS_PER_VERTEX;

        ByteBuffer coordsByteBuffer = ByteBuffer.allocateDirect(vertex.length * 4);
        coordsByteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = coordsByteBuffer.asFloatBuffer();
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);

        ByteBuffer coordsDrawOrderByteBuffer = ByteBuffer.allocateDirect(vertexDrawOrder.length * 2);
        coordsDrawOrderByteBuffer.order(ByteOrder.nativeOrder());

        drawListBuffer = coordsDrawOrderByteBuffer.asShortBuffer();
        drawListBuffer.put(vertexDrawOrder);
        drawListBuffer.position(0);

        return this;
    }

    public int getAttributeLocation(String attributeName)
    {
        return GLES20.glGetAttribLocation(program, attributeName);
    }

    public int enableVertexArray(String vertexArrayName)
    {
        int vertexArrayLocation = getAttributeLocation(vertexArrayName);

        GLES20.glEnableVertexAttribArray(vertexArrayLocation);
        GLES20.glVertexAttribPointer(vertexArrayLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        return vertexArrayLocation;
    }

    public GLView disableVertexArray(int vertexArrayLocation)
    {
        GLES20.glDisableVertexAttribArray(vertexArrayLocation);

        return this;
    }

    public int getUniformLocation(String uniformName)
    {
        return GLES20.glGetUniformLocation(program, uniformName);
    }

    public GLView setUniform(String uniformName, Vector2 vector2)
    {
        int uniformLocation = getUniformLocation(uniformName);

        GLES20.glUniform2fv(uniformLocation, 1, vector2.toFloat(), 0);

        return this;
    }
    public GLView setUniform(String uniformName, Color color)
    {
        int uniformLocation = getUniformLocation(uniformName);

        GLES20.glUniform3fv(uniformLocation, 1, color.toFloat(), 0);

        return this;
    }

    public GLView drawFrame(int vertexDrawOrderLength)
    {
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, vertexDrawOrderLength, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        return this;
    }

    public GLView onDrawFrame()
    {
        GLES20.glUseProgram(program);

        return this;
    }
}
