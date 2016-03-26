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

         0.5f, -0.5f, 0.0f,
         0.5f,  0.5f, 0.0f,
        -0.5f,  0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f
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

        int vertexShader = GLES2Compiler.compileShader(GLES20.GL_VERTEX_SHADER, context, R.raw.sky_vertex_shader);
        int fragmentShader = GLES2Compiler.compileShader(GLES20.GL_FRAGMENT_SHADER, context, R.raw.sky_fragment_shader);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix)
    {
        GLES20.glUseProgram(program);
        GLES2Compiler.checkOperationError("glUseProgram");


        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES2Compiler.checkOperationError("glGetAttribLocation");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES2Compiler.checkOperationError("glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES2Compiler.checkOperationError("glVertexAttribPointer");


        int colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES2Compiler.checkOperationError("glGetUniformLocation");

        GLES20.glUniform4fv(colorHandle, 1, new float[] { 0.2f, 0.709803922f, 0.898039216f, 1.0f }, 0);
        GLES2Compiler.checkOperationError("glUniform4fv");


        int mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES2Compiler.checkOperationError("glGetUniformLocation");

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
        GLES2Compiler.checkOperationError("glUniformMatrix4fv");

        GLES20.glDrawElements(GLES10.GL_TRIANGLES, COORDS_DRAW_ORDER.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES2Compiler.checkOperationError("glDrawElements");

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES2Compiler.checkOperationError("glDisableVertexAttribArray");
    }
}
