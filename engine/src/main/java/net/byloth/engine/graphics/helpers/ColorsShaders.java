package net.byloth.engine.graphics.helpers;

import net.byloth.engine.graphics.Color;

/**
 * Created by Matteo on 10/10/2015.
 */
final public class ColorsShaders
{
    static final public int MIN_SHADING_RATIO = 0;
    static final public int MAX_SHADING_RATIO = 1;

    private ColorsShaders() { }

    static public Color alphaBlend(Color startingColor, double shadingRatio, Color finalColor)
    {
        if ((shadingRatio >= MIN_SHADING_RATIO) && (shadingRatio <= MAX_SHADING_RATIO))
        {
            int[] colorUnits = new int[]
            {
                finalColor.getRed() - startingColor.getRed(),
                finalColor.getGreen() - startingColor.getGreen(),
                finalColor.getBlue() - startingColor.getBlue(),
                finalColor.getAlpha() - startingColor.getAlpha()
            };

            Color color = new Color();

            color.setRed((int) (startingColor.getRed() + (colorUnits[0] * shadingRatio)));
            color.setGreen((int) (startingColor.getGreen() + (colorUnits[1] * shadingRatio)));
            color.setBlue((int) (startingColor.getBlue() + (colorUnits[2] * shadingRatio)));
            color.setAlpha((int) (startingColor.getAlpha() + (colorUnits[3] * shadingRatio)));

            return color;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
