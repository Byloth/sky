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

    static final public Color WHITE = new Color(255, 255, 255, 255);
    static final public Color RED = new Color(255, 0, 0, 255);
    static final public Color YELLOW = new Color(255, 255, 0, 255);
    static final public Color GREEN = new Color(0, 255, 0, 255);
    static final public Color CYAN = new Color(0, 255, 255, 255);
    static final public Color BLUE = new Color(0, 0, 255, 255);
    static final public Color FUCHSIA = new Color(255, 0, 255, 255);
    static final public Color BLACK = new Color(0, 0, 0, 255);
    static final public Color TRANSPARENT = new Color(0, 0, 0, 0);

    public Color()
    {
        this.red = MIN_VALUE;
        this.green = MIN_VALUE;
        this.blue = MIN_VALUE;

        this.alpha = MAX_VALUE;
    }
    public Color(int redValue, int greenValue, int blueValue)
    {
        setRed(redValue);
        setGreen(greenValue);
        setBlue(blueValue);

        this.alpha = MAX_VALUE;
    }
    public Color(int redValue, int greenValue, int blueValue, int alphaValue)
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
    public Color setRed(int redValue)
    {
        if ((redValue >= MIN_VALUE) && (redValue <= MAX_VALUE))
        {
            red = redValue;
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
    public Color setGreen(int greenValue)
    {
        if ((greenValue >= MIN_VALUE) && (greenValue <= MAX_VALUE))
        {
            green = greenValue;
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
    public Color setBlue(int blueValue)
    {
        if ((blueValue >= MIN_VALUE) && (blueValue <= MAX_VALUE))
        {
            blue = blueValue;
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
    public Color setAlpha(int alphaValue)
    {
        if ((alphaValue >= MIN_VALUE) && (alphaValue <= MAX_VALUE))
        {
            alpha = alphaValue;
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

    @Override
    public boolean equals(Object otherColor)
    {
        if (otherColor instanceof Color)
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
}
