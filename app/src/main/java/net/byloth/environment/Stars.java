package net.byloth.environment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import net.byloth.engine.DayTime;
import net.byloth.engine.helpers.Randomize;
import net.byloth.engine.graphics.Color;
import net.byloth.engine.graphics.TimedColor;
import net.byloth.engine.graphics.TimedShader;
import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.R;
import net.byloth.sky.updaters.SunUpdater;

/**
 * Created by Matteo on 11/10/2015.
 */
public class Stars extends View
{
    private int textureWidth;
    private int textureHeight;

    private DayTime dayTime;

    private Paint paint;
    private Bitmap starsTexture;

    private TimedShader timedShader;

    private Star[] stars;

    static final public int MAX_STARS = 250;

    public Stars(int canvasWidthValue, int canvasHeightValue, DayTime currentDayTime, Context context)
    {
        super(context);

        dayTime = currentDayTime;

        paint = new Paint();
        paint.setAntiAlias(true);

        starsTexture = BitmapFactory.decodeResource(getResources(), R.drawable.stars);

        textureWidth = starsTexture.getWidth() / 2;
        textureHeight = starsTexture.getHeight();

        stars = new Star[MAX_STARS];

        for (int index = 0; index < MAX_STARS; index += 1)
        {
            stars[index] = new Star(canvasWidthValue, canvasHeightValue);
        }

        initializeColors();
    }

    public Stars initializeColors()
    {
        int nauticalSunriseTime = SunUpdater.getNauticalSunriseTime();
        int astronomicalSunriseTime = SunUpdater.getAstronomicalSunriseTime();

        int nauticalSunsetTime = SunUpdater.getNauticalSunsetTime();
        int astronomicalSunsetTime = SunUpdater.getAstronomicalSunsetTime();

        timedShader = new TimedShader(new TimedColor[]
        {
            new TimedColor(astronomicalSunriseTime, Color.WHITE),
            new TimedColor(nauticalSunriseTime, Color.TRANSPARENT),
            new TimedColor(nauticalSunsetTime, Color.TRANSPARENT),
            new TimedColor(astronomicalSunsetTime, Color.WHITE)
        });

        return this;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int currentAlpha = timedShader.getCurrentColor(dayTime.getMilliseconds()).getAlpha();

        paint.setAlpha(currentAlpha);

     // TODO: paint.setColorFilter(new PorterDuffColorFilter(timedShader.getCurrentColor(dayTime.getMilliseconds()).toInt(), PorterDuff.Mode.MULTIPLY));

        for (Star star : stars)
        {
            canvas.save();
            canvas.rotate(star.rotation, star.position.left + (star.width / 2), star.position.top + (star.height / 2));
            canvas.drawBitmap(starsTexture, star.size, star.position, paint);
            canvas.restore();
        }
    }

    private class Star
    {
        public float rotation;

        public float width;
        public float height;

        public Rect size;
        public RectF position;

        final public float MIN_SCALE = 0.25f;
        final public float MAX_SCALE = 0.75f;

        public Star(int canvasWidthValue, int canvasHeightValue)
        {
            float x;
            float y;

            float scale = Randomize.Float(MIN_SCALE, MAX_SCALE);

            width = textureWidth * scale;
            height = textureHeight * scale;

            x = Randomize.Float(-width, canvasWidthValue);
            y = Randomize.Float(-height, canvasHeightValue);

            position = new RectF(x, y, width + x, height + y);

            rotation = Randomize.DegreesAngle();

            if (Randomize.Boolean() == true)
            {
                size = new Rect(0, 0, textureWidth, textureHeight);
            }
            else
            {
                size = new Rect(textureWidth, 0, textureWidth * 2, textureHeight);
            }
        }
    }
}
