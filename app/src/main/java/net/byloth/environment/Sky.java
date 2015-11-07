package net.byloth.environment;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.byloth.engine.DayTime;
import net.byloth.sky.Updater;
import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.TimedColor;
import net.byloth.engine.graphics.TimedShader;

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

    static final public int SUNRISE_VALUE = 27000000;
    static final public int START_DAY_TIME = 32400000;
    static final public int END_DAY_TIME = 64800000;
    static final public int SUNSET_VALUE = 70200000;
    static final public int START_NIGHT_TIME = 75600000;
    static final public int END_NIGHT_TIME = 21600000;

    static final public Color[] SunriseColors = new Color[]
    {
        new Color(91, 130, 194),
        new Color(214, 193, 255),
        new Color(255, 183, 185)
    };

    static final public Color[] DayColors = new Color[]
    {
        new Color(83, 153, 255),
        new Color(157, 197, 252),
        new Color(195, 219, 255)
    };

    static final public Color[] SunsetColors = new Color[]
    {
        new Color(58, 125, 206),
        new Color(217, 150, 148),
        new Color(255, 102, 0)
    };

    static final public Color[] NightColors = new Color[]
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

        timedShaders = new TimedShader[]
        {
            new TimedShader(new TimedColor[]
            {
                new TimedColor(END_NIGHT_TIME, NightColors[0]),
                new TimedColor(SUNRISE_VALUE, SunriseColors[0]),
                new TimedColor(START_DAY_TIME, DayColors[0]),
                new TimedColor(END_DAY_TIME, DayColors[0]),
                new TimedColor(SUNSET_VALUE, SunsetColors[0]),
                new TimedColor(START_NIGHT_TIME, NightColors[0])
            }),
            new TimedShader(new TimedColor[]
            {
                new TimedColor(END_NIGHT_TIME, NightColors[1]),
                new TimedColor(SUNRISE_VALUE, SunriseColors[1]),
                new TimedColor(START_DAY_TIME, DayColors[1]),
                new TimedColor(END_DAY_TIME, DayColors[1]),
                new TimedColor(SUNSET_VALUE, SunsetColors[1]),
                new TimedColor(START_NIGHT_TIME, NightColors[1])
            }),
            new TimedShader(new TimedColor[]
            {
                new TimedColor(END_NIGHT_TIME, NightColors[2]),
                new TimedColor(SUNRISE_VALUE, SunriseColors[2]),
                new TimedColor(START_DAY_TIME, DayColors[2]),
                new TimedColor(END_DAY_TIME, DayColors[2]),
                new TimedColor(SUNSET_VALUE, SunsetColors[2]),
                new TimedColor(START_NIGHT_TIME, NightColors[2])
            })
        };
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

        canvas.drawRect(new RectF(0, 0, canvasWidth, canvasHeight), paint);

        stars.draw(canvas);
    }
}
