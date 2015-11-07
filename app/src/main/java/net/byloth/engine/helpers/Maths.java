package net.byloth.engine.helpers;

/**
 * Created by Matteo on 10/10/2015.
 */
final public class Maths
{
    static final public int MAX_DEGREES = 360;
    static final public float MAX_RADIANS = (float) (2 * Math.PI);

    static final public float PI = (float) Math.PI;

    private Maths() { }

    static public float arcTan(float coordinateX, float coordinateY)
    {
        return (float) Math.atan2(coordinateY, coordinateX);
    }

    static public int roundUp(float value)
    {
        return (int) Math.ceil(value);
    }
    static public int roundDown(float value)
    {
        return (int) Math.floor(value);
    }

    static public boolean isDivisible(float dividend, float divisor)
    {
        return ((dividend % divisor) == 0);
    }

    static public float sine(float radiansAngle)
    {
        return (float) Math.sin(radiansAngle);
    }
    static public float cosine(float radiansAngle)
    {
        return (float) Math.cos(radiansAngle);
    }

    static public float hypotenuse(float cathetus1, float cathetus2)
    {
        return (float) Math.sqrt(Math.pow(cathetus1, 2) + Math.pow(cathetus2, 2));
    }

    static public float proportion(float absoluteMaximum, float relativeMaximum, float absolutePartial)
    {
        if (absoluteMaximum != 0)
        {
            return (absolutePartial * relativeMaximum) / absoluteMaximum;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
