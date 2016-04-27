package net.byloth.engine.utils;

import android.location.Location;

import java.util.Calendar;

import net.byloth.engine.helpers.Maths;

/**
 * Created by Matteo on 08/04/2016.
 */
public class SunTimes
{
    static final private int DAWN = 1;
    static final private int SUNSET = -1;

    static final private double OFFICIAL = 90.8333d;
    static final private double CIVIL = 96;
    static final private double NAUTICAL = 102;
    static final private double ASTRONOMICAL = 108;

    static private double getMeanObliquity(double julianCenturies)
    {
        double seconds = Maths.polynomialFunction(new double[] { -0.001813, 0.00059, -46.815, 21.448 }, julianCenturies);

        return (23 + ((26 + (seconds / 60)) / 60));
    }

    static private double getObliquityCorrection(double julianCenturies)
    {
        double meanObliquity = getMeanObliquity(julianCenturies);
        double omega = 125.04d - (1934.136d * julianCenturies);

        return (meanObliquity + (0.00256d * Maths.cosine(omega, Maths.DEGREES)));
    }

    static private double getMeanLongitude(double julianCenturies)
    {
        double meanLongitude = Maths.polynomialFunction(new double[] { 0.0003032d, 36000.76983d , 280.46646d }, julianCenturies);

        return Maths.adjustInRange(meanLongitude, Maths.MAX_DEGREES);
    }

    static private double getEccentricityEarthOrbit(double julianCenturies)
    {
        return Maths.polynomialFunction(new double[] { 0.0000001267d, -0.000042037d, 0.016708634d }, julianCenturies);
    }

    static private double getMeanAnomaly(double julianCenturies)
    {
        return Maths.polynomialFunction(new double[] { -(1.0d / 300000), -0.0001603d, 35999.05034d, 357.52772d }, julianCenturies);
    }

    static private double getEquationOfTime(double julianCenturies)
    {
        double epsilon = getObliquityCorrection(julianCenturies);
        double meanLong = getMeanLongitude(julianCenturies);
        double eccentricity = getEccentricityEarthOrbit(julianCenturies);
        double meanAnomaly = getMeanAnomaly(julianCenturies);

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

    static private double getEquationOfCenter(double julianCenturies)
    {
        double meanAnomaly = getMeanAnomaly(julianCenturies);

        meanAnomaly = Math.toRadians(meanAnomaly);

        double meanAnomalySine = Math.sin(meanAnomaly);
        double meanAnomalySine2 = Math.sin(meanAnomaly * 2);
        double meanAnomalySine3 = Math.sin(meanAnomaly * 3);

        return ((meanAnomalySine * (1.914602d - (julianCenturies * (0.004817d + (julianCenturies * 0.000014d))))) +
                (meanAnomalySine2 * (0.019993d - (julianCenturies * 0.000101d))) +
                (meanAnomalySine3 * 0.000289d));
    }

    static private double getTrueLongitude(double julianCenturies)
    {
        double meanLong = getMeanLongitude(julianCenturies);
        double equationOfCenter = getEquationOfCenter(julianCenturies);

        return (meanLong + equationOfCenter);
    }

    static private double getApparentLongitude(double julianCenturies)
    {
        double trueLong = getTrueLongitude(julianCenturies);

        double omega = 125.04d - (julianCenturies * 1934.136d);

        return ((trueLong - 0.00569d) - (Maths.sine(omega, Maths.DEGREES) * 0.00478d));
    }

    static private double getDeclination(double julianCenturies)
    {
        double epsilon = getObliquityCorrection(julianCenturies);
        double lambda = getApparentLongitude(julianCenturies);

        double julianCenturiesSine = Maths.sine(epsilon, Maths.DEGREES) * Maths.sine(lambda, Maths.DEGREES);

        return Math.asin(julianCenturiesSine);
    }

    static private double getHourAngle(double julianCenturies, double latitude, int hourAngleType, double twilightType)
    {
        double declination = getDeclination(julianCenturies);

        latitude = Math.toRadians(latitude);

        double sunriseHourAngle = ((Maths.cosine(twilightType, Maths.DEGREES) / (Math.cos(latitude) * Math.cos(declination))) -
                                   (Math.tan(latitude) * Math.tan(declination)));

        return (Maths.arcCosine(sunriseHourAngle, Maths.DEGREES) * hourAngleType);
    }

    static private double getTime(double julianDay, double julianCenturies, double equationOfTime, double latitude, double longitude, double timeZoneOffset, int hourAngleType, double twilightType)
    {
        double hourAngle = getHourAngle(julianCenturies, latitude, hourAngleType, twilightType);

        double delta = longitude + hourAngle;

        julianCenturies = (720 - (delta * 4)) - equationOfTime;
        julianCenturies = JulianDate.toJulianCenturies(julianDay + (julianCenturies / 1440));

        equationOfTime = getEquationOfTime(julianCenturies);
        hourAngle = getHourAngle(julianCenturies, latitude, hourAngleType, twilightType);

        delta = longitude + hourAngle;

        return Maths.adjustInRange(((720 - (delta * 4)) - equationOfTime) + timeZoneOffset, 1440);
    }

    private double noonTime;

    private double officialDawnTime;
    private double civilDawnTime;
    private double nauticalDawnTime;
    private double astronomicalDawnTime;

    private double officialSunsetTime;
    private double civilSunsetTime;
    private double nauticalSunsetTime;
    private double astronomicalSunsetTime;

    private JulianDate julianDate;

    private void initializeNoonTime(double longitude, double timeZoneOffset)
    {
        double julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay() + (longitude / Maths.MAX_DEGREES));
        double equationOfTime = getEquationOfTime(julianCenturies);

        julianCenturies = (720 - (longitude * 4)) - equationOfTime;

        julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay() + (julianCenturies / 1440));
        equationOfTime = getEquationOfTime(julianCenturies);

        noonTime = Maths.adjustInRange(((720 - (longitude * 4)) - equationOfTime) + timeZoneOffset, 1440);
    }

    private void initializeSunriseTimes(double julianCenturies, double equationOfTime, double latitude, double longitude, double timeZoneOffset)
    {
        officialDawnTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, DAWN, OFFICIAL);
        civilDawnTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, DAWN, CIVIL);
        nauticalDawnTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, DAWN, NAUTICAL);
        astronomicalDawnTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, DAWN, ASTRONOMICAL);
    }

    private void initializeSunsetTimes(double julianCenturies, double equationOfTime, double latitude, double longitude, double timeZoneOffset)
    {
        officialSunsetTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, SUNSET, OFFICIAL);
        civilSunsetTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, SUNSET, CIVIL);
        nauticalSunsetTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, SUNSET, NAUTICAL);
        astronomicalSunsetTime = getTime(julianDate.getJulianDay(), julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset, SUNSET, ASTRONOMICAL);
    }

    public SunTimes(Location location)
    {
        this(Calendar.getInstance(), location);
    }
    public SunTimes(Calendar calendar, Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        double timeZoneOffset = ((double) calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 60000;

        julianDate = new JulianDate(calendar);

        initializeNoonTime(longitude, timeZoneOffset);

        double julianCenturies = JulianDate.toJulianCenturies(julianDate.getJulianDay());
        double equationOfTime = getEquationOfTime(julianCenturies);

        initializeSunriseTimes(julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset);
        initializeSunsetTimes(julianCenturies, equationOfTime, latitude, longitude, timeZoneOffset);
    }

    public double getNoonTime()
    {
        return noonTime;
    }

    public double getOfficialDawnTime()
    {
        return officialDawnTime;
    }
    public double getCivilDawnTime()
    {
        return civilDawnTime;
    }
    public double getNauticalDawnTime()
    {
        return nauticalDawnTime;
    }
    public double getAstronomicalDawnTime()
    {
        return astronomicalDawnTime;
    }

    public double getOfficialSunsetTime()
    {
        return officialSunsetTime;
    }
    public double getCivilSunsetTime()
    {
        return civilSunsetTime;
    }
    public double getNauticalSunsetTime()
    {
        return nauticalSunsetTime;
    }
    public double getAstronomicalSunsetTime()
    {
        return astronomicalSunsetTime;
    }
}
