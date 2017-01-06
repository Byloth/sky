package net.byloth.engine.graphics;

/**
 * Created by Matteo on 10/10/2015.
 */
public class Color
{
    private int red;
    private int green;
    private int blue;

    private int alpha;

    static final public int MIN_VALUE = 0;
    static final public int MAX_VALUE = 255;

    static public Color White() { return new Color(255, 255, 255); }
    static public Color Red() { return new Color(255, 0, 0); }
    static public Color Yellow() { return new Color(255, 255, 0); }
    static public Color Green() { return new Color(0, 255, 0); }
    static public Color Cyan()  { return new Color(0, 255, 255); }
    static public Color Blue() { return new Color(0, 0, 255); }
    static public Color Fuchsia() { return new Color(255, 0, 255); }
    static public Color Black() { return new Color(0, 0, 0); }
    static public Color Transparent() { return new Color(0, 0, 0, 0); }

    public Color()
    {
        this(MIN_VALUE, MIN_VALUE, MIN_VALUE);
    }
    public Color(double redValue, double greenValue, double blueValue)
    {
        this(redValue, greenValue, blueValue, MAX_VALUE);
    }
    public Color(Color colorValues)
    {
        this(colorValues.getRed(), colorValues.getGreen(), colorValues.getBlue(), colorValues.getAlpha());
    }
    public Color(double redValue, double greenValue, double blueValue, double alphaValue)
    {
        setRed(redValue);
        setGreen(greenValue);
        setBlue(blueValue);

        setAlpha(alphaValue);
    }

    public int getRed()
    {
        return red;
    }
    public Color setRed(double redValue)
    {
        if ((redValue >= MIN_VALUE) && (redValue <= MAX_VALUE))
        {
            red = (int) redValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public int getGreen()
    {
        return green;
    }
    public Color setGreen(double greenValue)
    {
        if ((greenValue >= MIN_VALUE) && (greenValue <= MAX_VALUE))
        {
            green = (int) greenValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public int getBlue()
    {
        return blue;
    }
    public Color setBlue(double blueValue)
    {
        if ((blueValue >= MIN_VALUE) && (blueValue <= MAX_VALUE))
        {
            blue = (int) blueValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public int getAlpha()
    {
        return alpha;
    }
    public Color setAlpha(double alphaValue)
    {
        if ((alphaValue >= MIN_VALUE) && (alphaValue <= MAX_VALUE))
        {
            alpha = (int) alphaValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public int toInt()
    {
        return android.graphics.Color.argb(alpha, red, green, blue);
    }
    
    public float[] toFloat()
    {
        return new float[] {

            ((float) red) / MAX_VALUE,
            ((float) green) / MAX_VALUE,
            ((float) blue) / MAX_VALUE,
            ((float) alpha) / MAX_VALUE    
        };
    }

    @Override
    public boolean equals(Object otherColor)
    {
        if ((otherColor instanceof Color) == true)
        {
            Color color = (Color) otherColor;

            if (red == color.getRed())
            {
                if (green == color.getGreen())
                {
                    if (blue == color.getBlue())
                    {
                        if (alpha == color.getAlpha())
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "color(" + red + ", " + green + ", " + blue + ")";
    }
}
