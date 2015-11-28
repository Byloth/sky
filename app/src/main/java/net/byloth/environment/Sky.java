package net.byloth.environment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

import net.byloth.engine.DayTime;
import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.TimedColor;
import net.byloth.engine.graphics.TimedShader;
import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.updaters.SunUpdater;

/**
 * Created by Matteo on 10/10/2015.
 */
public class Sky extends View
{
    private int canvasWidth;
    private int canvasHeight;

    private DayTime dayTime;

    private Stars stars;

    private Paint paint;

    private TimedShader[] timedShaders;

    private RectF position;

    static final public Color[] sunriseColors = new Color[]
    {
        new Color(91, 130, 194),
        new Color(214, 193, 255),
        new Color(255, 183, 185)
    };

    static final public Color[] dayColors = new Color[]
    {
        new Color(83, 153, 255),
        new Color(157, 197, 252),
        new Color(195, 219, 255)
    };

    static final public Color[] sunsetColors = new Color[]
    {
        new Color(58, 125, 206),
        new Color(217, 150, 148),
        new Color(255, 102, 0)
    };

    static final public Color[] nightColors = new Color[]
    {
        new Color(0, 0, 0),
        new Color(0, 9, 17),
        new Color(0, 13, 25)
    };

    public Sky(int canvasWidthValue, int canvasHeightValue, DayTime currentDayTime, final Context context)
    {
        super(context);

        canvasWidth = canvasWidthValue;
        canvasHeight = canvasHeightValue;

        dayTime = currentDayTime;

        stars = new Stars(canvasWidthValue, canvasHeightValue, currentDayTime, context);

        paint = new Paint();
        paint.setAntiAlias(true);

        position = new RectF(0, 0, canvasWidthValue, canvasHeightValue);

        initializeColors();
    }

    public Sky initializeColors()
    {
        int officialSunriseTime = SunUpdater.getOfficialSunriseTime();
        int astronomicalSunriseTime = SunUpdater.getAstronomicalSunriseTime();

        int officialSunsetTime = SunUpdater.getOfficialSunsetTime();
        int astronomicalSunsetTime = SunUpdater.getAstronomicalSunsetTime();

        int startDayTime = officialSunriseTime + (officialSunriseTime - astronomicalSunriseTime);
        int endDayTime = officialSunsetTime + (officialSunsetTime - astronomicalSunsetTime);

        timedShaders = new TimedShader[]
        {
            new TimedShader(new TimedColor[]
            {
                new TimedColor(astronomicalSunriseTime, nightColors[0]),
                new TimedColor(officialSunriseTime, sunriseColors[0]),
                new TimedColor(startDayTime, dayColors[0]),
                new TimedColor(endDayTime, dayColors[0]),
                new TimedColor(officialSunsetTime, sunsetColors[0]),
                new TimedColor(astronomicalSunsetTime, nightColors[0])
            }),
            new TimedShader(new TimedColor[]
            {
                new TimedColor(astronomicalSunriseTime, nightColors[1]),
                new TimedColor(officialSunriseTime, sunriseColors[1]),
                new TimedColor(startDayTime, dayColors[1]),
                new TimedColor(endDayTime, dayColors[1]),
                new TimedColor(officialSunsetTime, sunsetColors[1]),
                new TimedColor(astronomicalSunsetTime, nightColors[1])
            }),
            new TimedShader(new TimedColor[]
            {
                new TimedColor(astronomicalSunriseTime, nightColors[2]),
                new TimedColor(officialSunriseTime, sunriseColors[2]),
                new TimedColor(startDayTime, dayColors[2]),
                new TimedColor(endDayTime, dayColors[2]),
                new TimedColor(officialSunsetTime, sunsetColors[2]),
                new TimedColor(astronomicalSunsetTime, nightColors[2])
            })
        };

        return this;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int currentMilliseconds = dayTime.getMilliseconds();

        int currentColors[] = new int[]
        {
            timedShaders[0].getCurrentColor(currentMilliseconds).toInt(),
            timedShaders[1].getCurrentColor(currentMilliseconds).toInt(),
            timedShaders[2].getCurrentColor(currentMilliseconds).toInt()
        };

        LinearGradient linearGradient = new LinearGradient(0, 0, 0, canvasHeight, currentColors, new float[] { 0, 0.666f, 1 }, Shader.TileMode.CLAMP);

        paint.setShader(linearGradient);

        canvas.drawRect(position, paint);

        stars.draw(canvas);
    }
}
