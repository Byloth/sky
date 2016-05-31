package net.byloth.engine.graphics.opengl.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.util.Log;

import net.byloth.engine.io.helpers.FileLoader;

/**
 * Created by Matteo on 26/03/2016.
 */
final public class GLCompiler
{
    static final private String TAG = "GLCompiler";

    private GLCompiler() { }

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
        int vertexShader = GLCompiler.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = GLCompiler.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);

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

    static public int linkProgram(Context context, @RawRes int vertexShaderResourceId, @RawRes int fragmentShaderResourceId)
    {
        String vertexShaderSource = FileLoader.loadTextResource(context, vertexShaderResourceId);
        String fragmentShaderSource = FileLoader.loadTextResource(context, fragmentShaderResourceId);

        return linkProgram(vertexShaderSource, fragmentShaderSource);
    }

    static public int loadTexture(Context context, @DrawableRes int textureResourceId)
    {
        int[] textureLocation = new int[1];

        GLES20.glGenTextures(1, textureLocation, 0);

        if (textureLocation[0] != 0)
        {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inScaled = false;

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureResourceId, bitmapOptions);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureLocation[0]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();
        }

        if (textureLocation[0] == 0)
        {
            Log.e(TAG, "Could not loading required texture!");
        }

        return textureLocation[0];
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