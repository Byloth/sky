package net.byloth.engine.helpers;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Matteo on 15/11/2015.
 */
final public class Time
{
    static private Definition[] definitions = new Definition[]
    {
        new Definition(12, "anno", "anni"),
        new Definition(4, "mese", "mesi"),
        new Definition(7, "settimana", "settimane"),
        new Definition(24, "giorno", "giorni"),
        new Definition(60, "ora", "ore"),
        new Definition(60, "minuto", "minuti"),
        new Definition(1000, "secondo", "secondi"),
        new Definition("ora")
    };

    private Time() { }

    static private String differenceToString(long timeDifference, long divisor)
    {
        return differenceToString(timeDifference, divisor, 0, false);
    }
    static private String differenceToString(long timeDifference, long divisor, int iterationCount, boolean applyModule)
    {
        if (iterationCount == (definitions.length - 1))
        {
            return definitions[iterationCount].singular;
        }
        else
        {
            int value;

            if ((applyModule == true) || (iterationCount > 0))
            {
                value = (int) (timeDifference / divisor) % definitions[iterationCount - 1].divisor;
            }
            else
            {
                value = (int) (timeDifference / divisor);
            }

            if (value > 0)
            {
                if (value > 1)
                {
                    return value + " " + definitions[iterationCount].plural;
                }
                else
                {
                    return "1 " + definitions[iterationCount].singular;
                }
            }
            else
            {
                divisor = divisor / definitions[iterationCount].divisor;

                return differenceToString(timeDifference, divisor, iterationCount + 1, true);
            }
        }
    }

    static public String getElapsedTimeAsString(Date pastDate)
    {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        return getTimeDifferenceAsString(pastDate, now);
    }
    static public String getTimeDifferenceAsString(Date firstDate, Date secondDate)
    {
        long firstDateTime = firstDate.getTime();
        long secondDateTime = secondDate.getTime();

        long timeDifference = secondDateTime - firstDateTime;
        long divisor = (long) 1000 * 60 * 60 * 24 * 7 * 4 * 12;

        return differenceToString(timeDifference, divisor);
    }

    static public class Definition
    {
        public int divisor;
        public String singular;
        public String plural;

        public Definition(String singularAndPluralValue)
        {
            divisor = 1;
            singular = singularAndPluralValue;
            plural = singularAndPluralValue;
        }
        public Definition(int divisorValue, String singularAndPluralValue)
        {
            divisor = divisorValue;
            singular = singularAndPluralValue;
            plural = singularAndPluralValue;
        }
        public Definition(String singularValue, String pluralValue)
        {
            divisor = 1;
            singular = singularValue;
            plural = pluralValue;
        }
        public Definition(int divisorValue, String singularValue, String pluralValue)
        {
            divisor = divisorValue;
            singular = singularValue;
            plural = pluralValue;
        }
    }
}
