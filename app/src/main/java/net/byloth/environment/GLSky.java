package net.byloth.environment;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import net.byloth.engine.graphics.Vector2;
import net.byloth.engine.graphics.opengl.GLView;
import net.byloth.engine.graphics.opengl.UpdatableGLView;
import net.byloth.engine.graphics.opengl.helpers.GLCompiler;
import net.byloth.engine.utils.DayTime;
import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.TimedColor;
import net.byloth.engine.graphics.TimedShader;
import net.byloth.sky.R;
import net.byloth.sky.updaters.SunTimesUpdater;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Matteo on 26/03/2016.
 */
public class GLSky extends UpdatableGLView
{
    static final private String TAG = "GLSky";

    static final private float VERTEX[] = {

            1.0f,  1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f
    };

    static final private short VERTEX_DRAW_ORDER[] = { 0, 1, 2, 0, 2, 3 };

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

    private DayTime dayTime;
    private Vector2 surfaceSize;

    private TimedShader[] timedShaders;

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

    public GLSky(SunTimesUpdater sunTimesUpdater)
    {
        dayTime = new DayTime();
        surfaceSize = new Vector2();

        initializeColors(sunTimesUpdater);
    }

    public GLSky onSurfaceCreated(Context context)
    {
        loadVertex(VERTEX, VERTEX_DRAW_ORDER);
        loadProgram(context, R.raw.sky_vertex_shader, R.raw.sky_fragment_shader);

        return this;
    }

    public GLSky onSurfaceChanged(int width, int height)
    {
        surfaceSize = new Vector2(width, height);

        return this;
    }

    public GLSky reinitializeColors(SunTimesUpdater sunTimesUpdater)
    {
        initializeColors(sunTimesUpdater);

        return this;
    }

    @Override
    public UpdatableGLView onUpdate()
    {
        for (TimedShader timedShader : timedShaders)
        {
            timedShader.updateCurrentColor(dayTime);
        }

        Log.d(TAG, "Frame updated!");

        return this;
    }

    @Override
    public GLView onDrawFrame()
    {
        super.onDrawFrame();

        int vertexArrayLocation = enableVertexArray("position");

        setUniform("screenResolution", surfaceSize);

        Log.d(TAG, "Top Color: " + timedShaders[0].getCurrentColor().toString());
        Log.d(TAG, "Middle Color: " + timedShaders[1].getCurrentColor().toString());
        Log.d(TAG, "Bottom Color: " + timedShaders[2].getCurrentColor().toString());

        setUniform("topColor", timedShaders[0].getCurrentColor());
        setUniform("middleColor", timedShaders[1].getCurrentColor());
        setUniform("bottomColor", timedShaders[2].getCurrentColor());

        drawFrame(VERTEX_DRAW_ORDER.length);

        disableVertexArray(vertexArrayLocation);

        return this;
    }
}
