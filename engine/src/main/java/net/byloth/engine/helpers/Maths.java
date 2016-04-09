package net.byloth.engine.helpers;

/**
 * Created by Matteo on 10/10/2015.
 */
final public class Maths
{
    static final public float MAX_DEGREES = 360;
    static final public float MAX_RADIANS = (float) (HighPrecisionMaths.MAX_RADIANS);

    static final public float PI = (float) Math.PI;

    private Maths() { }

    static public float adjustInRange(double value, double maximumValue)
    {
        return (float) HighPrecisionMaths.adjustInRange(value, 0, maximumValue);
    }
    static public float adjustInRange(double value, double minimumValue, double maximumValue)
    {
        return (float) HighPrecisionMaths.adjustInRange(value, minimumValue, maximumValue);
    }

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
        return (float) HighPrecisionMaths.hypotenuse(cathetus1, cathetus2);
    }

    static public float proportion(double absoluteMaximum, double relativeMaximum, double absolutePartial)
    {
        return (float) HighPrecisionMaths.proportion(absoluteMaximum, relativeMaximum, absolutePartial);
    }

    static public float squareRoot(double value)
    {
        return (float) Math.sqrt(value);
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
