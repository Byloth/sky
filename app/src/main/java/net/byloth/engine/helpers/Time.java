package net.byloth.engine.helpers;

import net.byloth.engine.helpers.Maths;

/**
 * Created by Matteo on 23/10/2015.
 */
/*
 * TODO: Remove this funckin' class!
 */
final public class Time
{
    private Time() { }

    static public int dayOfTheYear(int day, int month, int year)
    {
        if (((day >= 1) && (day < 31)) && ((month >= 1) && (month <= 12)))
        {
            boolean isLeapYear = isLeapYear(year);

            int roughDaysCount;
            int leapDayWeight;

            switch (month)
            {
                case 2:
                    if ((isLeapYear == true) && (day > 29))
                    {
                        throw new IllegalArgumentException();
                    }
                    else if (day > 28)
                    {
                        throw new IllegalArgumentException();
                    }

                    break;

                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 30)
                    {
                        throw new IllegalArgumentException();
                    }

                    break;

                default:
                    throw new RuntimeException();
            }

            roughDaysCount = Maths.roundDown(275 * month / 9);
            leapDayWeight = Maths.roundDown((month + 9) / 12);

            if (isLeapYear == false)
            {
                leapDayWeight *= 2;
            }

            return roughDaysCount - leapDayWeight + day - 30;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    static public boolean isLeapYear(int year)
    {
        return ((Maths.isDivisible(year, 400) == true) || ((Maths.isDivisible(year, 4) == true) && (Maths.isDivisible(year, 100) == false)));
    }
}
