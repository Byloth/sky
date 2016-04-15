package net.byloth.engine.utils;

import android.location.Location;

import net.byloth.engine.helpers.Maths;

import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * Created by Matteo on 08/04/2016.
 */
public class SunTime
{
    static final private int SUNRISE = 1;
    static final private int SUNSET = -1;

    static final public double OFFICIAL_ZENITH = 90.833d;

    static private double meanObliquity(double julianCenturies)
    {
        double seconds = 21.448d - (julianCenturies * (46.8150d + (julianCenturies * (0.00059d - (julianCenturies * 0.001813d)))));

        return (23 + ((26 + (seconds / 60)) / 60));
    }

    static private double obliquityCorrection(double julianCenturies)
    {
        double meanObliquity = meanObliquity(julianCenturies);
        double omega = 125.04d - (1934.136d * julianCenturies);

        return (meanObliquity + (0.00256d * Maths.cosine(omega, Maths.DEGREES)));
    }

    static private double geometricMeanLongitude(double julianCenturies)
    {
        return Maths.adjustInRange(280.46646d + (julianCenturies * (36000.76983d + (julianCenturies * 0.0003032d))), 360);
    }

    static private double eccentricityEarthOrbit(double julianCenturies)
    {
        return 0.016708634d - (julianCenturies * (0.000042037d + (julianCenturies * 0.0000001267d)));
    }

    static private double geometricMeanAnomaly(double julianCenturies)
    {
        return 357.52911d + (julianCenturies * (35999.05029d - (julianCenturies * 0.0001537d)));
    }

    static private double equationOfTime(double julianCenturies)
    {
        double epsilon = obliquityCorrection(julianCenturies);
        double meanLong = geometricMeanLongitude(julianCenturies);
        double eccentricity = eccentricityEarthOrbit(julianCenturies);
        double meanAnomaly = geometricMeanAnomaly(julianCenturies);

        double y = Math.pow(Maths.tangent(epsilon / 2, Maths.DEGREES), 2);

        double meanLongSine2 = Maths.sine(meanLong * 2, Maths.DEGREES);
        double meanLongSine4 = Maths.sine(meanLong * 4, Maths.DEGREES);
        double meanLongCosine2 = Maths.cosine(meanLong * 2, Maths.DEGREES);

        double meanAnomalySine = Maths.sine(meanAnomaly, Maths.DEGREES);
        double meanAnomalySine2 = Maths.sine(meanAnomaly * 2, Maths.DEGREES);

        double equationOfTime = ((y * meanLongSine2) -
                                 (eccentricity * meanAnomalySine * 2) +
                                 (eccentricity * y * meanLongCosine2 * meanAnomalySine * 4) -
                                 (Math.pow(y, 2) * meanLongSine4 * 0.5d) -
                                 (Math.pow(eccentricity, 2) * meanAnomalySine2 * 1.25d));

        return (Math.toDegrees(equationOfTime) * 4);
    }

    static private double equationOfCenter(double julianCenturies)
    {
        double meanAnomaly = geometricMeanAnomaly(julianCenturies);

        meanAnomaly = Math.toRadians(meanAnomaly);

        double meanAnomalySine = Math.sin(meanAnomaly);
        double meanAnomalySine2 = Math.sin(meanAnomaly * 2);
        double meanAnomalySine3 = Math.sin(meanAnomaly * 3);

        return ((meanAnomalySine * (1.914602d - (julianCenturies * (0.004817d + (julianCenturies * 0.000014d))))) +
                (meanAnomalySine2 * (0.019993d - (julianCenturies * 0.000101d))) +
                (meanAnomalySine3 * 0.000289d));
    }

    static private double trueLongitude(double julianCenturies)
    {
        double meanLong = geometricMeanLongitude(julianCenturies);
        double equationOfCenter = equationOfCenter(julianCenturies);

        return (meanLong + equationOfCenter);
    }

    static private double apparentLongitude(double julianCenturies)
    {
        double trueLong = trueLongitude(julianCenturies);

        double omega = 125.04d - (julianCenturies * 1934.136d);

        return ((trueLong - 0.00569d) - (Maths.sine(omega, Maths.DEGREES) * 0.00478d));
    }

    static private double declination(double julianCenturies)
    {
        double epsilon = obliquityCorrection(julianCenturies);
        double lambda = apparentLongitude(julianCenturies);

        double julianCenturiesSine = Maths.sine(epsilon, Maths.DEGREES) * Maths.sine(lambda, Maths.DEGREES);

        return Math.asin(julianCenturiesSine);
    }

    static private double hourAngle(double julianCenturies, double latitude, int hourAngleType)
    {
        if ((hourAngleType == SUNRISE) || (hourAngleType == SUNSET))
        {
            double declination = declination(julianCenturies);

            latitude = Math.toRadians(latitude);

            double sunriseHourAngle = ((Maths.cosine(OFFICIAL_ZENITH, Maths.DEGREES) / (Math.cos(latitude) * Math.cos(declination))) -
                    (Math.tan(latitude) * Math.tan(declination)));

            return (Maths.arcCosine(sunriseHourAngle, Maths.DEGREES) * hourAngleType);
        }
        else
        {
            throw new InvalidParameterException();
        }
    }

    private double noonTime;
    private double sunriseTime;
    private double sunsetTime;

    private JulianDate julianDate;

    private void initializeNoonTime(double longitude)
    {
        double julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay() + (longitude / 360));
        double equationOfTime = equationOfTime(julianCenturies);

        julianCenturies = (720 - (longitude * 4)) - equationOfTime;

        julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay() + (julianCenturies / 1440));
        equationOfTime = equationOfTime(julianCenturies);

        noonTime = Maths.adjustInRange((720 - (longitude * 4)) - equationOfTime, 1440);
    }

    private void initializeSunriseTime(double julianCenturies, double equationOfTime, double latitude, double longitude)
    {
        double hourAngle = hourAngle(julianCenturies, latitude, SUNRISE);

        double delta = longitude + hourAngle;

        julianCenturies = (720 - (delta * 4)) - equationOfTime;
        julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay() + (julianCenturies / 1440));

        equationOfTime = equationOfTime(julianCenturies);
        hourAngle = hourAngle(julianCenturies, latitude, SUNRISE);

        delta = longitude + hourAngle;

        sunriseTime = Maths.adjustInRange((720 - (delta * 4)) - equationOfTime, 1440);
    }

    private void initializeSunsetTime(double julianCenturies, double equationOfTime, double latitude, double longitude)
    {
        double hourAngle = hourAngle(julianCenturies, latitude, SUNSET);

        double delta = longitude + hourAngle;

        julianCenturies = (720 - (delta * 4)) - equationOfTime;
        julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay() + (julianCenturies / 1440));

        equationOfTime = equationOfTime(julianCenturies);
        hourAngle = hourAngle(julianCenturies, latitude, SUNSET);

        delta = longitude + hourAngle;

        sunsetTime = Maths.adjustInRange((720 - (delta * 4)) - equationOfTime, 1440);
    }

    public SunTime(Location location)
    {
        this(Calendar.getInstance(), location);
    }
    public SunTime(Calendar calendar, Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        julianDate = new JulianDate(calendar);

        initializeNoonTime(longitude);

        double julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay());

        double equationOfTime = equationOfTime(julianCenturies);

        initializeSunriseTime(julianCenturies, equationOfTime, latitude, longitude);
        initializeSunsetTime(julianCenturies, equationOfTime, latitude, longitude);
    }

    public double getNoonTime()
    {
        return noonTime;
    }

    public double getSunriseTime()
    {
        return sunriseTime;
    }

    public double getSunsetTime()
    {
        return sunsetTime;
    }
}
