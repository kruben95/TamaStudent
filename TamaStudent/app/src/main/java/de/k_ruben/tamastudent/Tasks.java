package de.k_ruben.tamastudent;

import android.content.Context;
import android.widget.Toast;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Ruben on 26.04.2016.
 */
public abstract class Tasks implements Executeable
{
    //Erstelldatum der Aufgabe
    long startDate;
    //Datum seit letztem ausführen von Methode execute
    long lastExecute;
    //Datum, wann die Aufgabe "abgearbeitet" ist
    long expireDate;
    //Ob Aufgabe ewig laufen soll
    boolean isInfinite = false;

    protected double elapsedTime()
    {
        return (System.currentTimeMillis() - lastExecute);
    }

    protected double taskDuration()
    {
        return (expireDate - startDate);
    }

    protected double calculateChangeValue(int value)
    {
        return value / (taskDuration()/elapsedTime());
    }
}
