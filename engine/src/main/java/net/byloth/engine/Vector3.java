package net.byloth.engine;

/**
 * Created by Matteo on 05/01/2017.
 */
public class Vector3 extends Vector2
{
    private float z;

    public Vector3()
    {
        this(0, 0, 0);
    }
    public Vector3(Vector2 xyValues)
    {
        this(xyValues, 0);
    }
    public Vector3(Vector2 xyValues, double zValue)
    {
        this(xyValues.getX(), xyValues.getY(), zValue);
    }
    public Vector3(Vector3 xyzValues)
    {
        this(xyzValues.getX(), xyzValues.getY(), xyzValues.getZ());
    }
    public Vector3(double xValue, double yValue, double zValue)
    {
        super(xValue, yValue);

        z = (float) zValue;
    }

    public float getZ()
    {
        return z;
    }
    public Vector3 setZ(double zValue)
    {
        z = (float) zValue;

        return this;
    }

    @Override
    public Vector3 setX(double xValue)
    {
        super.setX(xValue);

        return this;
    }
    @Override
    public Vector3 setY(double yValue)
    {
        super.setY(yValue);

        return this;
    }

    @Override
    public float[] toFloat()
    {
        float[] xyValues = super.toFloat();

        return new float[] {

            xyValues[0],
            xyValues[1],
            z
        };
    }
}
