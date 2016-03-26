package net.byloth.engine.graphics.opengl.helpers;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by Matteo on 26/03/2016.
 */
final public class GLES2Compiler
{
    static final private String TAG = "GLES2Compiler";
    private GLES2Compiler() { }

    static public int compileShader(int shaderType, String shaderCode)
    {
        int shader = GLES20.glCreateShader(shaderType);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    static public void checkOperationError(String operationName)
    {
        int errorCode = GLES20.glGetError();

        if (errorCode != GLES20.GL_NO_ERROR)
        {
            Log.e(TAG, "The OpenGL ES 2.0 operation called \"" + operationName + "\" has returned error code: " + errorCode);

            throw new RuntimeException();
        }
    }
}