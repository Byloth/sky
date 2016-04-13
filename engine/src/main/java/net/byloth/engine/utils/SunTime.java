package net.byloth.engine.utils;

import android.location.Location;

import net.byloth.engine.helpers.Maths;

import java.util.Calendar;

/**
 * Created by Matteo on 08/04/2016.
 */
public class SunTime
{
    static public double OFFICIAL_ZENITH = 90.833d;

    static private double computeApparentLongitude(double julianCenturies)
    {
        double trueLongitude = computeTrueLongitude(julianCenturies);
        double omega = 125.04d - (julianCenturies * 1934.136d);

        return (trueLongitude - 0.00569d - (Maths.sine(omega, Maths.DEGREES) * 0.00478d));
    }

    static private double computeDeclination(double julianCenturies)
    {
        double epsilon = computeObliquityCorrection(julianCenturies);
        double lambda = computeApparentLongitude(julianCenturies);

        double julianCenturiesSine = Maths.sine(epsilon, Maths.DEGREES) * Maths.sine(lambda, Maths.DEGREES);

        return Maths.arcSine(julianCenturiesSine, Maths.DEGREES);
    }

    static private double computeEccentricityEarthOrbit(double julianCenturies)
    {
        return 0.016708634d - (julianCenturies * (0.000042037d + (julianCenturies * 0.0000001267d)));
    }

    static private double computeEquationOfCenter(double julianCenturies)
    {
        double geometricMeanAnomaly = computeGeometricMeanAnomaly(julianCenturies);
        double radiansGeometricMeanAnomaly = Math.toRadians(geometricMeanAnomaly);

        double sineOfGeometricMeanAnomaly = Math.sin(radiansGeometricMeanAnomaly);
        double sineOfGeometricMeanAnomaly2 = Math.sin(radiansGeometricMeanAnomaly) * 2;
        double sineOfGeometricMeanAnomaly3 = Math.sin(radiansGeometricMeanAnomaly) * 3;

        return ((sineOfGeometricMeanAnomaly * (1.914602d - (julianCenturies * (0.004817d + (julianCenturies * 0.000014d))))) +
                (sineOfGeometricMeanAnomaly2 * (0.019993d - (julianCenturies * 0.000101d))) +
                (sineOfGeometricMeanAnomaly3 * 0.000289d));
    }

    static private double computeEquationOfTime(double julianCenturies)
    {
        double epsilon = computeObliquityCorrection(julianCenturies);
        double geometricMeanLongitude = computeGeometricMeanLongitude(julianCenturies);
        double eccentricityEarthOrbit = computeEccentricityEarthOrbit(julianCenturies);
        double geometricMeanAnomaly = computeGeometricMeanAnomaly(julianCenturies);

        double y = Math.pow(Math.tan(Math.toRadians(epsilon) / 2), 2);

        double sineOfSunMeanLongitude2 = Math.sin(Math.toRadians(geometricMeanLongitude) * 2);
        double sineOfSunMeanLongitude4 = Math.sin(Math.toRadians(geometricMeanLongitude) * 4);
        double cosineOfSunMeanLongitude2 = Math.cos(Math.toRadians(geometricMeanLongitude) * 2);

        double sineOfSunMeanAnomaly = Math.sin(Math.toRadians(geometricMeanAnomaly));
        double sineOfSunMeanAnomaly2 = Math.sin(Math.toRadians(geometricMeanAnomaly) * 2);

        double equationOfTime = ((y * sineOfSunMeanLongitude2) -
                                 (eccentricityEarthOrbit * sineOfSunMeanAnomaly * 2) +
                                 (eccentricityEarthOrbit * y * cosineOfSunMeanLongitude2 * sineOfSunMeanAnomaly * 4) -
                                 (Math.pow(y, 2) * sineOfSunMeanLongitude4 * 0.5d) -
                                 (Math.pow(eccentricityEarthOrbit, 2) * sineOfSunMeanAnomaly2 * 1.25d));

        return Math.toDegrees(equationOfTime);
    }

    static private double computeGeometricMeanAnomaly(double julianCenturies)
    {
        return 357.52911d + (julianCenturies * (35999.05029d - (julianCenturies * 0.0001537d)));
    }

    static private double computeGeometricMeanLongitude(double julianCenturies)
    {
        return Maths.adjustInRange(280.46646d + (julianCenturies * (36000.76983d + (julianCenturies * 0.0003032d))), 360);
    }

    static private double computeObliquityCorrection(double julianCenturies)
    {
        double seconds = 21.448d - (julianCenturies * (46.8150d + (julianCenturies * (0.00059d - (julianCenturies * 0.001813d)))));
        double meanObliquity = 23 + ((26 + (seconds / 60)) / 60);

        double omega = 125.04d - (1934.136d * julianCenturies);

        return meanObliquity + (0.00256d * Math.toRadians(omega));
    }

    static private double computeSunriseHourAngle(double latitude, double declination)
    {
        double radiansLatitude = Math.toRadians(latitude);
        double radiansDeclination = Math.toRadians(declination);

        double sunriseHourAngle = ((Maths.cosine(OFFICIAL_ZENITH, Maths.DEGREES) / (Math.cos(radiansLatitude) * Math.cos(radiansDeclination))) -
                                   (Math.tan(radiansLatitude) * Math.tan(radiansDeclination)));

        return Maths.arcCosine(sunriseHourAngle, Maths.DEGREES); // IN DEGREES!!!
    }

    static private double computeTrueLongitude(double julianCenturies)
    {
        double geometricMeanLongitude = computeGeometricMeanLongitude(julianCenturies);
        double equationOfCenter = computeEquationOfCenter(julianCenturies);

        return (geometricMeanLongitude + equationOfCenter);
    }

    private double noonTime;
    private double sunriseTime;

    private JulianDate julianDate;

    private void initializeNoonTime(double longitude)
    {
        // TODO: Capire quale metodo, tra 'julianDate.getJulianDay();' e 'julianDate.getJulianDayTime();' è più adatto.
        double noonJulianTime = JulianDate.computeJulianCentury(julianDate.getJulianDay() + (longitude / 360));
        double equationOfTime = computeEquationOfTime(noonJulianTime);

        noonTime = (720 + (longitude * 4)) - equationOfTime;

        // TODO: Capire quale metodo, tra 'julianDate.getJulianDay();' e 'julianDate.getJulianDayTime();' è più adatto.
        noonJulianTime = JulianDate.computeJulianCentury((julianDate.getJulianDay() - 0.5d) + (noonTime / 1440));
        equationOfTime = computeEquationOfTime(noonJulianTime);

        noonTime = (720 + (longitude * 4)) - equationOfTime;
    }

    private void initializeSunriseTime(double latitude, double longitude)
    {
        double noonJulianTime = JulianDate.computeJulianCentury(julianDate.getJulianDay() + (noonTime / 1440));

        double equationOfTime = computeEquationOfTime(noonJulianTime);
        double declination = computeDeclination(noonJulianTime);

        double hourAngle = computeSunriseHourAngle(latitude, declination); // IN DEGREES!!!

        double delta = longitude - hourAngle;

        sunriseTime = (720 + (delta * 4)) - equationOfTime;

        noonJulianTime = JulianDate.computeJulianCentury(julianDate.getJulianDay() + (noonTime / 1440));

        // TODO 1: Terminare questo metodo!
        // TODO 2: Ricontrollare i metodi precedenti!

        /* TODO 1 e TODO 2:
         * Prestare particolare attenzione ai metodi chiamati nel file JavaScript di esempio!
         * Probabilmente vengono chiamati due metodi diversi ma con nomi molto simili:
         *  - calcTimeJulianCent();
         *  - calcJDFromJulianCent();
         */

        // TODO 3: Reformat del codice necessario; sintetizzare, al massimo, i nomi delle variabili utilizzate.
    }

    public SunTime(Calendar calendar, Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        julianDate = new JulianDate(calendar);

        initializeNoonTime(longitude);
    }

    public double getNoonTime()
    {
        return noonTime;
    }
}
