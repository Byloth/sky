package net.byloth.engine.utils;

import android.location.Location;

import net.byloth.engine.helpers.Maths;

import java.util.Calendar;

/**
 * Created by Matteo on 08/04/2016.
 */
public class SunTime
{
    static private double computeEccentricityEarthOrbit(double julianDayTime)
    {
        return 0.016708634d - (julianDayTime * (0.000042037d + (julianDayTime * 0.0000001267d)));
    }

    static private double computeEquationOfTime(double julianDayTime)
    {
        double epsilon = computeObliquityCorrection(julianDayTime);
        double sunMeanLongitude = computeGeometricSunMeanLongitude(julianDayTime);
        double eccentricityEarthOrbit = computeEccentricityEarthOrbit(julianDayTime);
        double sunMeanAnomaly = computeGeometricSunMeanAnomaly(julianDayTime);

        double y = Math.pow(Math.tan(Math.toRadians(epsilon) / 2), 2);

        double sineOfSunMeanLongitude2 = Math.sin(Math.toRadians(sunMeanLongitude) * 2);
        double sineOfSunMeanLongitude4 = Math.sin(Math.toRadians(sunMeanLongitude) * 4);
        double cosineOfSunMeanLongitude2 = Math.cos(Math.toRadians(sunMeanLongitude) * 2);

        double sineOfSunMeanAnomaly = Math.sin(Math.toRadians(sunMeanAnomaly));
        double sineOfSunMeanAnomaly2 = Math.sin(Math.toRadians(sunMeanAnomaly) * 2);

        double equationOfTime = (y * sineOfSunMeanLongitude2) -
                                (eccentricityEarthOrbit * sineOfSunMeanAnomaly * 2) +
                                (eccentricityEarthOrbit * y * cosineOfSunMeanLongitude2 * sineOfSunMeanAnomaly * 4) -
                                (Math.pow(y, 2) * sineOfSunMeanLongitude4 * 0.5d) -
                                (Math.pow(eccentricityEarthOrbit, 2) * sineOfSunMeanAnomaly2 * 1.25d);

        return Math.toDegrees(equationOfTime);
    }

    static private double computeGeometricSunMeanAnomaly(double julianDayTime)
    {
        return 357.52911d + (julianDayTime * (35999.05029d - (julianDayTime * 0.0001537d)));
    }

    static private double computeGeometricSunMeanLongitude(double julianDayTime)
    {
        return Maths.adjustInRange(280.46646d + (julianDayTime * (36000.76983d + (julianDayTime * 0.0003032d))), 360);
    }

    static private double computeObliquityCorrection(double julianDayTime)
    {
        double seconds = 21.448d - (julianDayTime * (46.8150d + (julianDayTime * (0.00059d - (julianDayTime * 0.001813d)))));
        double meanObliquity = 23 + ((26 + (seconds / 60)) / 60);

        double omega = 125.04d - (1934.136d * julianDayTime);

        return meanObliquity + (0.00256d * Math.toRadians(omega));
    }

    public SunTime(Calendar calendar, Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        JulianDate julianDate = new JulianDate(calendar);

        // TODO: Capire quale metodo, tra 'julianDate.getJulianDay();' e 'julianDate.getJulianDayTime();' è più adatto.
        double noonJulianTime = JulianDate.computeJulianCentury(julianDate.getJulianDay() + (longitude / 360));
        double equationOfTime = computeEquationOfTime(noonJulianTime);
    }

/*
    public float getNoonTime()
    {
        // TODO: Implementare!
    }
*/
}
