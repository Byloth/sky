package net.byloth.engine.graphics;

/**
 * Created by Matteo on 30/04/2016.
 */
public class Vector2
{
    private float x;
    private float y;

    public Vector2()
    {
        x = 0;
        y = 0;
    }
    public Vector2(double xValue, double yValue)
    {
        x = (int) xValue;
        y = (int) yValue;
    }

    public float getX()
    {
        return x;
    }
    public Vector2 setX(double xValue)
    {
        x = (float) xValue;

        return this;
    }

    public float getY()
    {
        return y;
    }
    public Vector2 setY(double yValue)
    {
        y = (float) yValue;

        return this;
    }

    public float[] toFloat()
    {
        return new float[] {

            x,
            y
        };
    }
}
