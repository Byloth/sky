package net.byloth.engine.graphics.opengl.helpers;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import net.byloth.engine.io.helpers.FileLoader;

/**
 * Created by Matteo on 26/03/2016.
 */
final public class GLES2Compiler
{
    static final private String TAG = "GLES2Compiler";

    private GLES2Compiler() { }

    static public int compileShader(int shaderType, String shaderSource)
    {
        int shader = GLES20.glCreateShader(shaderType);

        if (shader != 0)
        {
            GLES20.glShaderSource(shader, shaderSource);
            GLES20.glCompileShader(shader);

            int[] compileStatus = new int[1];

            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] != 0)
            {
                return shader;
            }
            else
            {
                GLES20.glDeleteShader(shader);

                Log.e(TAG, "Could not compile shader:  "+ GLES20.glGetShaderInfoLog(shader) + " | " + shaderSource);
            }
        }

        throw new RuntimeException();
    }
    static public int compileShader(Context context, int shaderType, int shaderResourceId)
    {
        String shaderSource = FileLoader.loadTextResource(context, shaderResourceId);

        return compileShader(shaderType, shaderSource);
    }

    static public int linkProgram(String vertexShaderSource, String fragmentShaderSource)
    {
        int vertexShader = GLES2Compiler.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = GLES2Compiler.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);

        int program = GLES20.glCreateProgram();

        if (program != 0)
        {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);

            GLES20.glLinkProgram(program);

            int[] linkStatus = new int[1];

            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == GLES20.GL_TRUE)
            {
                return program;
            }
            else
            {
                GLES20.glDeleteProgram(program);

                Log.e(TAG, "Could not link program:  " + GLES20.glGetProgramInfoLog(program));
            }
        }

        throw new RuntimeException();
    }

    static public int linkProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId)
    {
        String vertexShaderSource = FileLoader.loadTextResource(context, vertexShaderResourceId);
        String fragmentShaderSource = FileLoader.loadTextResource(context, fragmentShaderResourceId);

        return linkProgram(vertexShaderSource, fragmentShaderSource);
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