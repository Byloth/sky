package net.byloth.engine.helpers;

/**
 * Created by Matteo on 10/10/2015.
 */
final public class Maths
{
    static final public float MAX_DEGREES = 360;
    static final public float MAX_RADIANS = (float) (2 * Math.PI);

    static final public float PI = (float) Math.PI;

    private Maths() { }

    static public float arcSine(double value)
    {
        return (float) Math.asin(value);
    }
    static public float arcCosine(double value)
    {
        return (float) Math.acos(value);
    }
    static public float arcTangent(double value)
    {
        return (float) Math.atan(value);
    }
    static public float arcTangent(double coordinateX, double coordinateY)
    {
        return (float) Math.atan2(coordinateY, coordinateX);
    }

    static public int roundUp(double value)
    {
        return (int) Math.ceil(value);
    }
    static public int roundDown(double value)
    {
        return (int) Math.floor(value);
    }

    static public boolean isDivisible(double dividend, double divisor)
    {
        return ((dividend % divisor) == 0);
    }

    static public float sine(double radiansAngle)
    {
        return (float) Math.sin(radiansAngle);
    }
    static public float cosine(double radiansAngle)
    {
        return (float) Math.cos(radiansAngle);
    }
    static public float tangent(double radiansAngle)
    {
        return (float) Math.tan(radiansAngle);
    }

    static public float hypotenuse(double cathetus1, double cathetus2)
    {
        return (float) Math.sqrt(Math.pow(cathetus1, 2) + Math.pow(cathetus2, 2));
    }

    static public float proportion(double absoluteMaximum, double relativeMaximum, double absolutePartial)
    {
        if (absoluteMaximum != 0)
        {
            return (float) ((absolutePartial * relativeMaximum) / absoluteMaximum);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    static public float toDegrees(double radiansAngle)
    {
        return (float) Math.toDegrees(radiansAngle);
    }
    static public float toRadians(double degreesAngle)
    {
        return (float) Math.toRadians(degreesAngle);
    }
}
