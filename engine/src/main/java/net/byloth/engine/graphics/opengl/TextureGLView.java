package net.byloth.engine.graphics.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.annotation.DrawableRes;
import android.util.Log;

import net.byloth.engine.ISpaceable;
import net.byloth.engine.R;
import net.byloth.engine.Vector2;
import net.byloth.engine.Vector3;
import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.opengl.helpers.GLCompiler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Matteo on 19/05/2016.
 */
public class TextureGLView extends GLView implements ISpaceable
{
    static final private int TEXTURE_COORDS_PER_VERTEX = 2;
    static final private int TEXTURE_VERTEX_STRIDE = TEXTURE_COORDS_PER_VERTEX * 4;

    static final private float[] VERTEX = {

        0.5f,  0.5f, 0.0f,
        -0.5f,  0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    };

    static final public float[] DEFAULT_TEXTURE_VERTEX = {

        1.0f, 1.0f,
        0.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 0.0f
    };
    static final public short[] DEFAULT_VERTEX_DRAW_ORDER = { 0, 1, 2, 0, 2, 3 };

    private int texture;
    private int vertexArrayLocation;

    private float scale;

    private float[] modelMatrix = new float[16];
    private float[] modelViewMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    private float[] textureVertexArray;

    private Color tintColor;

    private Vector3 position;
    private Vector3 rotation;

    private FloatBuffer textureVertexBuffer;

    public TextureGLView()
    {
        scale = 1;

        tintColor = Color.White();

        position = new Vector3();
        rotation = new Vector3();
    }

    protected TextureGLView computeModelMatrix()
    {
        // TODO: Remove this...
            position = new Vector3(new Vector2(0, 0));
            scale = 1.0f;

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, position.getX(), position.getY(), position.getZ());

        if (rotation.getX() != 0)
        {
            float[] xAxisRotationMatrix = new float[16];

            Matrix.setRotateM(xAxisRotationMatrix, 0, rotation.getX(), 1.0f, 0.0f, 0.0f);
            Matrix.multiplyMM(modelMatrix, 0, xAxisRotationMatrix, 0, modelMatrix, 0);
        }

        if (rotation.getY() != 0)
        {
            float[] yAxisRotationMatrix = new float[16];

            Matrix.setRotateM(yAxisRotationMatrix, 0, rotation.getY(), 0.0f, 1.0f, 0.0f);
            Matrix.multiplyMM(modelMatrix, 0, yAxisRotationMatrix, 0, modelMatrix, 0);
        }

        if (rotation.getZ() != 0)
        {
            float[] zAxisRotationMatrix = new float[16];

            Matrix.setRotateM(zAxisRotationMatrix, 0, rotation.getZ(), 0.0f, 0.0f, 1.0f);
            Matrix.multiplyMM(modelMatrix, 0, zAxisRotationMatrix, 0, modelMatrix, 0);
        }

        Matrix.scaleM(modelMatrix, 0, scale, scale, scale);

        return this;
    }

    protected TextureGLView loadTextureVertexBuffer()
    {
        ByteBuffer textureCoordsByteBuffer = ByteBuffer.allocateDirect(textureVertexArray.length * 4);
        textureCoordsByteBuffer.order(ByteOrder.nativeOrder());

        textureVertexBuffer = textureCoordsByteBuffer.asFloatBuffer();
        textureVertexBuffer.put(textureVertexArray);

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

    public TextureGLView loadTexture(Context context, @DrawableRes int textureResourceId)
    {
        loadVertex(VERTEX, DEFAULT_VERTEX_DRAW_ORDER);
        loadProgram(context, R.raw.default_vertex_shader, R.raw.default_fragment_shader);

        texture = GLCompiler.loadTexture(context, textureResourceId);
        textureVertexArray = DEFAULT_TEXTURE_VERTEX;

        return this;
    }

    public TextureGLView setTextureVertex(float[] textureVertex)
    {
        textureVertexArray = textureVertex;

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

        loadVertex(VERTEX, DEFAULT_VERTEX_DRAW_ORDER);
        loadProgram(context, R.raw.default_vertex_shader, R.raw.default_fragment_shader);

        return this;
    }

    @Override
    public TextureGLView onDraw(float[] viewMatrix, float[] projectionMatrix)
    {
        beginDraw();

        setUniform("color", tintColor);
        setTextureUniform("texture", texture);

        loadTextureVertexBuffer();
        setVertexAttribute("textureCoords", textureVertexBuffer, TEXTURE_COORDS_PER_VERTEX, TEXTURE_VERTEX_STRIDE);

        computeModelMatrix();

        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

        setUniformMatrix("mvpMatrix", mvpMatrix);

        draw();
        endDraw();

        return this;
    }

    @Override
    public float getXAxisPosition()
    {
        return position.getX();
    }
    @Override
    public TextureGLView setXAxisPosition(double xAxisPositionValue)
    {
        position.setX(xAxisPositionValue);

        return this;
    }

    @Override
    public float getYAxisPosition()
    {
        return position.getY();
    }
    @Override
    public TextureGLView setYAxisPosition(double yAxisPositionValue)
    {
        position.setY(yAxisPositionValue);

        return this;
    }

    @Override
    public float getZAxisPosition()
    {
        return position.getZ();
    }
    @Override
    public TextureGLView setZAxisPosition(double zAxisPositionValue)
    {
        position.setZ(zAxisPositionValue);

        return this;
    }

    @Override
    public Vector3 getPosition()
    {
        return new Vector3(position);
    }
    @Override
    public TextureGLView setPosition(Vector2 positionValues)
    {
        position = new Vector3(positionValues);

        return this;
    }
    @Override
    public TextureGLView setPosition(Vector3 positionValues)
    {
        position = new Vector3(positionValues);

        return this;
    }

    @Override
    public float getXAxisRotation()
    {
        return rotation.getX();
    }
    @Override
    public TextureGLView setXAxisRotation(double xAxisRotationValue)
    {
        rotation.setX(xAxisRotationValue);

        return this;
    }

    @Override
    public float getYAxisRotation()
    {
        return rotation.getY();
    }
    @Override
    public TextureGLView setYAxisRotation(double yAxisRotationValue)
    {
        rotation.setY(yAxisRotationValue);

        return this;
    }

    @Override
    public float getZAxisRotation()
    {
        return rotation.getZ();
    }
    @Override
    public TextureGLView setZAxisRotation(double zAxisRotationValue)
    {
        rotation.setZ(zAxisRotationValue);

        return this;
    }

    @Override
    public Vector3 getRotation()
    {
        return new Vector3(rotation);
    }
    @Override
    public TextureGLView setRotation(Vector3 rotationValues)
    {
        rotation = new Vector3(rotationValues);

        return this;
    }

    @Override
    public float getScale()
    {
        return scale;
    }
    @Override
    public TextureGLView setScale(double scaleValue)
    {
        scale = (float) scaleValue;

        return this;
    }
}
