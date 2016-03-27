package net.byloth.environment;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import net.byloth.engine.graphics.opengl.helpers.GLES2Compiler;
import net.byloth.sky.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Matteo on 26/03/2016.
 */
public class GLSky
{
    final private int COORDS_PER_VERTEX = 3;

    final private float COORDS[] = {

         1.0f,  1.0f, 0.0f,
        -1.0f,  1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
         1.0f, -1.0f, 0.0f
    };
    final private short COORDS_DRAW_ORDER[] = { 0, 1, 2, 0, 2, 3 };

    final private int VERTEX_COUNT = COORDS.length / COORDS_PER_VERTEX;
    final private int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private int program;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    public GLSky(Context context)
    {
        ByteBuffer coordsByteBuffer = ByteBuffer.allocateDirect(COORDS.length * 4);
        coordsByteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = coordsByteBuffer.asFloatBuffer();
        vertexBuffer.put(COORDS);
        vertexBuffer.position(0);

        ByteBuffer coordsDrawOrderByteBuffer = ByteBuffer.allocateDirect(COORDS_DRAW_ORDER.length * 2);
        coordsDrawOrderByteBuffer.order(ByteOrder.nativeOrder());

        drawListBuffer = coordsDrawOrderByteBuffer.asShortBuffer();
        drawListBuffer.put(COORDS_DRAW_ORDER);
        drawListBuffer.position(0);

        program = GLES2Compiler.linkProgram(context, R.raw.sky_vertex_shader, R.raw.sky_fragment_shader);
    }

    public void draw(float[] mvpMatrix)
    {
        GLES20.glUseProgram(program);

        int positionHandle = GLES20.glGetAttribLocation(program, "position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        int screenResolutionHandle = GLES20.glGetUniformLocation(program, "screenResolution");
        GLES20.glUniform2fv(screenResolutionHandle, 1, new float[] { 1080f, 1920f }, 0);

        int topColorHandle = GLES20.glGetUniformLocation(program, "topColor");
        GLES20.glUniform3fv(topColorHandle, 1, new float[] { 1.0f, 0.0f, 0.0f }, 0);
        int middleColorHandle = GLES20.glGetUniformLocation(program, "middleColor");
        GLES20.glUniform3fv(middleColorHandle, 1, new float[] { 0.0f, 1.0f, 0.0f }, 0);
        int bottomColorHandle = GLES20.glGetUniformLocation(program, "bottomColor");
        GLES20.glUniform3fv(bottomColorHandle, 1, new float[] { 0.0f, 0.0f, 1.0f }, 0);

        GLES20.glDrawElements(GLES10.GL_TRIANGLE_FAN, COORDS_DRAW_ORDER.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
