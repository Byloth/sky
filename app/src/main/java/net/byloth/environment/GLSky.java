package net.byloth.environment;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import net.byloth.engine.graphics.opengl.helpers.GLES2Compiler;

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

    final private String VERTEX_SHADER_CODE =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +

        "void main()" +
        "{" +
            "gl_Position = uMVPMatrix * vPosition;" +
        "}";
    final private String FRAGMENT_SHADER_CODE =
        "precision mediump float;" +
        "uniform vec4 vColor;" +

        "void main()" +
        "{" +
            "gl_FragColor = vColor;" +
        "}";

    private int program;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    public GLSky(GLSurfaceView.Renderer renderer)
    {
        ByteBuffer coordsByteBuffer = ByteBuffer.allocateDirect(COORDS.length * 4);
        coordsByteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = coordsByteBuffer.asFloatBuffer();
        vertexBuffer.put(COORDS);
        vertexBuffer.position(0);

        ByteBuffer coordsDrawOrderByteBuffer = ByteBuffer.allocate(COORDS_DRAW_ORDER.length * 2);
        coordsDrawOrderByteBuffer.order(ByteOrder.nativeOrder());

        drawListBuffer = coordsDrawOrderByteBuffer.asShortBuffer();
        drawListBuffer.put(COORDS_DRAW_ORDER);
        drawListBuffer.position(0);

        int vertexShader = GLES2Compiler.compileShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = GLES2Compiler.compileShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix)
    {
        GLES20.glUseProgram(program);

        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        int colorHandle = GLES20.glGetUniformLocation(program, "vColor");

        int mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES2Compiler.checkOperationError("glGetUniformLocation");

        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES20.glUniform4fv(colorHandle, 1, new float[] { 0.2f, 0.709803922f, 0.898039216f, 1.0f }, 0);

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
        GLES2Compiler.checkOperationError("glUniformMatrix4fv");

        GLES20.glDrawElements(GLES10.GL_TRIANGLES, COORDS_DRAW_ORDER.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
