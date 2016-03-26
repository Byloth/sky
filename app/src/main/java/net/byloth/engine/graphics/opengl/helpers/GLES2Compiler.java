package net.byloth.engine.graphics.opengl.helpers;

import android.content.Context;
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

        int[] compileStatus = new int[1];

        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (compileStatus[0] != 0)
        {
            return shader;
        }
        else
        {
            throw new RuntimeException();
        }
    }
    static public int compileShader(int shaderType, Context context, int shaderResourceId)
    {
        String shaderCode = GLES2Loader.loadTextResource(context, shaderResourceId);

        return compileShader(shaderType, shaderCode);
    }

    static public void checkOperationError(String operationName)
    {
        int errorCode = GLES20.glGetError();

        if (errorCode != GLES20.GL_NO_ERROR)
        {
            throw new RuntimeException("OpenGL ES 2.0 operation \"" + operationName + "\" returned error code: " + errorCode);
        }
    }
}