package net.byloth.environment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

import net.byloth.engine.utils.DayTime;
import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.TimedColor;
import net.byloth.engine.graphics.TimedShader;
import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 10/10/2015.
 */
public class Sky extends View
{
    static final public Color[] SUNRISE_COLORS = new Color[] {

        new Color(91, 130, 194),
        new Color(214, 193, 255),
        new Color(255, 183, 185)
    };

    static final public Color[] DAY_COLORS = new Color[] {

        new Color(83, 153, 255),
        new Color(157, 197, 252),
        new Color(195, 219, 255)
    };

    static final public Color[] SUNSET_COLORS = new Color[] {

        new Color(58, 125, 206),
        new Color(217, 150, 148),
        new Color(255, 102, 0)
    };

    static final public Color[] NIGHT_COLORS = new Color[] {

        new Color(0, 0, 0),
        new Color(0, 9, 17),
        new Color(0, 13, 25)
    };

    private int canvasWidth;
    private int canvasHeight;

    private DayTime dayTime;

    private Stars stars;

    private Paint paint;

    private TimedShader[] timedShaders;

    private RectF position;

    private void initializeColors(SunTimesUpdater sunTimesUpdater)
    {
        int officialSunriseTime = sunTimesUpdater.getOfficialDawnTime();
        int astronomicalSunriseTime = sunTimesUpdater.getAstronomicalDawnTime();

        int officialSunsetTime = sunTimesUpdater.getOfficialSunsetTime();
        int astronomicalSunsetTime = sunTimesUpdater.getAstronomicalSunsetTime();

        int startDayTime = officialSunriseTime + (officialSunriseTime - astronomicalSunriseTime);
        int endDayTime = officialSunsetTime + (officialSunsetTime - astronomicalSunsetTime);

        timedShaders = new TimedShader[] {

            new TimedShader(new TimedColor[] {

                new TimedColor(astronomicalSunriseTime, NIGHT_COLORS[0]),
                new TimedColor(officialSunriseTime, SUNRISE_COLORS[0]),
                new TimedColor(startDayTime, DAY_COLORS[0]),
                new TimedColor(endDayTime, DAY_COLORS[0]),
                new TimedColor(officialSunsetTime, SUNSET_COLORS[0]),
                new TimedColor(astronomicalSunsetTime, NIGHT_COLORS[0])
            }),
            new TimedShader(new TimedColor[] {

                new TimedColor(astronomicalSunriseTime, NIGHT_COLORS[1]),
                new TimedColor(officialSunriseTime, SUNRISE_COLORS[1]),
                new TimedColor(startDayTime, DAY_COLORS[1]),
                new TimedColor(endDayTime, DAY_COLORS[1]),
                new TimedColor(officialSunsetTime, SUNSET_COLORS[1]),
                new TimedColor(astronomicalSunsetTime, NIGHT_COLORS[1])
            }),
            new TimedShader(new TimedColor[] {

                new TimedColor(astronomicalSunriseTime, NIGHT_COLORS[2]),
                new TimedColor(officialSunriseTime, SUNRISE_COLORS[2]),
                new TimedColor(startDayTime, DAY_COLORS[2]),
                new TimedColor(endDayTime, DAY_COLORS[2]),
                new TimedColor(officialSunsetTime, SUNSET_COLORS[2]),
                new TimedColor(astronomicalSunsetTime, NIGHT_COLORS[2])
            })
        };
    }

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

        initializeColors(LiveWallpaper.getInstance().getSunTimesUpdater());
    }

    public Sky reinitializeColors(SunTimesUpdater sunTimesUpdater)
    {
        initializeColors(sunTimesUpdater);

        stars.reinitializeColors(sunTimesUpdater);

        return this;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int currentMilliseconds = dayTime.getMilliseconds();

        int currentColors[] = new int[]
        {
            timedShaders[0].updateCurrentColor(currentMilliseconds).toInt(),
            timedShaders[1].updateCurrentColor(currentMilliseconds).toInt(),
            timedShaders[2].updateCurrentColor(currentMilliseconds).toInt()
        };

        LinearGradient linearGradient = new LinearGradient(0, 0, 0, canvasHeight, currentColors, new float[] { 0, 0.666f, 1 }, Shader.TileMode.CLAMP);

        paint.setShader(linearGradient);

        canvas.drawRect(position, paint);

        stars.draw(canvas);
    }
}
