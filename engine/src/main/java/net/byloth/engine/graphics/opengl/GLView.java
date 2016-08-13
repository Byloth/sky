package net.byloth.engine.graphics.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.annotation.RawRes;
import android.util.Log;

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

    static final private String TAG = "GLView";

    private int program;
    private int vertexCount;
    private int vertexDrawOrderLength;

    private long updatingInterval;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private GLSurfaceView glSurfaceView;
    private UpdateThread updateThread;

    public GLView()
    {
        updatingInterval = 17;  // 60 FPS
    }

    protected GLView loadProgram(String vertexShaderSource, String fragmentShaderSource)
    {
        program = GLCompiler.linkProgram(vertexShaderSource, fragmentShaderSource);

        return this;
    }
    protected GLView loadProgram(Context context, @RawRes int vertexShaderResourceId, @RawRes int fragmentShaderResourceId)
    {
        program = GLCompiler.linkProgram(context, vertexShaderResourceId, fragmentShaderResourceId);

        return this;
    }

    protected GLView loadVertex(float[] vertex, short[] vertexDrawOrder)
    {
        vertexCount = vertex.length / COORDS_PER_VERTEX;

        ByteBuffer coordsByteBuffer = ByteBuffer.allocateDirect(vertex.length * 4);
        coordsByteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = coordsByteBuffer.asFloatBuffer();
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);

        vertexDrawOrderLength = vertexDrawOrder.length;

        ByteBuffer coordsDrawOrderByteBuffer = ByteBuffer.allocateDirect(vertexDrawOrder.length * 2);
        coordsDrawOrderByteBuffer.order(ByteOrder.nativeOrder());

        drawListBuffer = coordsDrawOrderByteBuffer.asShortBuffer();
        drawListBuffer.put(vertexDrawOrder);
        drawListBuffer.position(0);

        return this;
    }

    protected int getProgram()
    {
        return program;
    }

    protected int getAttributeLocation(String attributeName)
    {
        return GLES20.glGetAttribLocation(program, attributeName);
    }

    protected int enableVertexArray(String vertexArrayName)
    {
        int vertexArrayLocation = getAttributeLocation(vertexArrayName);

        GLES20.glEnableVertexAttribArray(vertexArrayLocation);
        GLES20.glVertexAttribPointer(vertexArrayLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        return vertexArrayLocation;
    }

    protected GLView disableVertexArray(int vertexArrayLocation)
    {
        GLES20.glDisableVertexAttribArray(vertexArrayLocation);

        return this;
    }

    protected int getUniformLocation(String uniformName)
    {
        return GLES20.glGetUniformLocation(program, uniformName);
    }

    protected GLView setUniform(String uniformName, Vector2 vector2)
    {
        int uniformLocation = getUniformLocation(uniformName);

        GLES20.glUniform2fv(uniformLocation, 1, vector2.toFloat(), 0);

        return this;
    }
    protected GLView setUniform(String uniformName, Color color)
    {
        int uniformLocation = getUniformLocation(uniformName);

        GLES20.glUniform3fv(uniformLocation, 1, color.toFloat(), 0);

        return this;
    }

    public GLView setUpdatingInterval(long updatingIntervalValue)
    {
        if (updatingIntervalValue >= 0)
        {
            updatingInterval = updatingIntervalValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }

        return this;
    }

    protected boolean onUpdate()
    {
        return false;
    }

    protected GLView requestRender()
    {
        if (glSurfaceView != null)
        {
            glSurfaceView.requestRender();
        }

        return this;
    }

    protected GLView drawElements()
    {
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, vertexDrawOrderLength, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        return this;
    }

    abstract protected GLView onDraw(float[] mvpMatrix);

    public synchronized GLView onPause()
    {
        if (updateThread.isAlive() == true)
        {
            updateThread.interrupt();
        }

        return this;
    }

    public synchronized GLView onResume()
    {
        if ((updateThread == null) || (updateThread.isAlive() == false))
        {
            updateThread = new UpdateThread();

            updateThread.start();
        }

        return this;
    }

    public GLView onSurfaceCreated(GLSurfaceView glSurfaceViewInstance)
    {
        glSurfaceView = glSurfaceViewInstance;

        return this;
    }

    public GLView onVisibilityChanged(boolean visible)
    {
        if (visible == true)
        {
            onResume();
        }
        else
        {
            onPause();
        }

        return this;
    }

    public GLView onDrawFrame(float[] mvpMatrix)
    {
        GLES20.glUseProgram(program);

        onDraw(mvpMatrix);

        Log.d(TAG, "Frame drawed!");

        return this;
    }

    public class UpdateThread extends Thread
    {
        private boolean haveToStop;

        public UpdateThread()
        {
            haveToStop = false;
        }

        @Override
        public void interrupt()
        {
            super.interrupt();

            haveToStop = true;
        }

        @Override
        public void run()
        {
            boolean continueUpdating = true;

            try
            {
                while ((continueUpdating == true) && (haveToStop == false))
                {
                    continueUpdating = onUpdate();

                    requestRender();

                    if (updatingInterval > 0)
                    {
                        Thread.sleep(updatingInterval);
                    }

                    Log.d(TAG, "Thread updated!");
                }
            }
            catch (InterruptedException e) { }
            finally
            {
                Log.d(TAG, "Updating thread has been interrupted!");
            }
        }
    }
}
